package com.stratebi.lincebi.push.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.stratebi.lincebi.push.model.Notification;
import com.stratebi.lincebi.push.model.Registration;
import org.pentaho.platform.api.usersettings.IUserSettingService;
import org.pentaho.platform.engine.core.system.PentahoSessionHolder;
import org.pentaho.platform.engine.core.system.PentahoSystem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.util.*;

public class PushService {

	private static final Logger LOGGER = LoggerFactory.getLogger(PushService.class);

	private static final String REGISTRATIONS_SETTING_NAME = "lincebi.push.registrations";

	private static IUserSettingService getSettingService() {
		return PentahoSystem.get(IUserSettingService.class, PentahoSessionHolder.getSession());
	}

	private static Map<String, Registration> getRegistrations() throws JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		String rawRegistrations = PushService.getSettingService().getUserSetting(PushService.REGISTRATIONS_SETTING_NAME, "{}").getSettingValue();
		return mapper.readValue(rawRegistrations, new TypeReference<HashMap<String, Registration>>(){});
	}

	private static void setRegistrations(Map<String, Registration> registrations) throws JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		String rawRegistrations = mapper.writeValueAsString(registrations);
		PushService.getSettingService().setUserSetting(PushService.REGISTRATIONS_SETTING_NAME, rawRegistrations);
	}

	public static List<Registration> list() throws JsonProcessingException {
		Map<String, Registration> registrations = PushService.getRegistrations();
		return new ArrayList<>(registrations.values());
	}

	public static void register(Registration registration) throws JsonProcessingException {
		Map<String, Registration> registrations = PushService.getRegistrations();
		registrations.put(registration.getDevice(), registration);
		PushService.setRegistrations(registrations);
	}

	public static void unregister(Registration registration) throws JsonProcessingException {
		Map<String, Registration> registrations = PushService.getRegistrations();
		registrations.remove(registration.getDevice());
		PushService.setRegistrations(registrations);
	}

	public static void notify(Notification notification) throws JsonProcessingException {
		final String endpointUrl = "https://push.lincebi.com/v1/notify/";

		ObjectMapper mapper = new ObjectMapper();

		Map<String, Registration> registrations = PushService.getRegistrations();
		for (Registration registration : registrations.values()) {
			HttpHeaders reqHeader = new HttpHeaders();
			reqHeader.put("Content-Type", Collections.singletonList("application/json"));

			ObjectNode reqBody = mapper.createObjectNode();
			reqBody.put("device", registration.getDevice());
			reqBody.put("instance", registration.getInstance());
			reqBody.put("secret", registration.getSecret());
			reqBody.set("notification", mapper.valueToTree(notification));

			HttpEntity<String> reqEntity = new HttpEntity<>(reqBody.toString(), reqHeader);

			try {
				RestTemplate restTemplate = new RestTemplate();
				restTemplate.exchange(endpointUrl, HttpMethod.POST, reqEntity, String.class);
			} catch (HttpStatusCodeException ex) {
				PushService.LOGGER.error(ex.getMessage());
				if (ex.getStatusCode() == HttpStatus.UNAUTHORIZED || ex.getStatusCode() == HttpStatus.GONE) {
					PushService.unregister(registration);
				}
			}
		}
	}

}
