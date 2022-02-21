package com.stratebi.lincebi.push.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.stratebi.lincebi.push.model.Notification;
import com.stratebi.lincebi.push.model.Registration;
import org.pentaho.platform.api.engine.IAuthorizationPolicy;
import org.pentaho.platform.api.repository2.unified.IUnifiedRepository;
import org.pentaho.platform.engine.core.system.PentahoSessionHolder;
import org.pentaho.platform.engine.core.system.PentahoSystem;
import org.pentaho.platform.engine.security.SecurityHelper;
import org.pentaho.platform.repository.usersettings.UserSettingService;
import org.pentaho.platform.repository2.ClientRepositoryPaths;
import org.pentaho.platform.security.policy.rolebased.actions.AdministerSecurityAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.io.PrintWriter;
import java.io.Serializable;
import java.io.StringWriter;
import java.util.*;
import java.util.concurrent.Callable;

public class PushService {

	private static final Logger LOGGER = LoggerFactory.getLogger(PushService.class);

	private static final String REGISTRATIONS_SETTING_NAME = UserSettingService.SETTING_PREFIX + "lincebi.push.registrations";

	private static final String NOTIFY_ENDPOINT_URL = "https://push.lincebi.com/v1/notify/";

	private static final byte[] LOCK = new byte[0];

	private static boolean canAdminister() {
		IAuthorizationPolicy authorizationPolicy = PentahoSystem.get(IAuthorizationPolicy.class, PentahoSessionHolder.getSession());
		return authorizationPolicy.isAllowed(AdministerSecurityAction.NAME);
	}

	private static Map<String, Registration> getRegistrations(String username) {
		try {
			if (username == null) {
				username = PentahoSessionHolder.getSession().getName();
			} else if (!PushService.canAdminister()) {
				throw new SecurityException("Unauthorized user");
			}

			IUnifiedRepository repository = PentahoSystem.get(IUnifiedRepository.class);
			String homePath = ClientRepositoryPaths.getUserHomeFolderPath(username);
			Serializable userHomeId = repository.getFile(homePath).getId();
			Map<String, Serializable> userMetadata = repository.getFileMetadata(userHomeId);

			for (Map.Entry<String, Serializable> entry : userMetadata.entrySet()) {
				String key = entry.getKey();
				if (key.equals(PushService.REGISTRATIONS_SETTING_NAME)) {
					ObjectMapper mapper = new ObjectMapper();
					String rawRegistrations = entry.getValue().toString();
					return mapper.readValue(rawRegistrations, new TypeReference<HashMap<String, Registration>>(){});
				}
			}
		} catch (Exception ex) {
			StringWriter sw = new StringWriter();
			ex.printStackTrace(new PrintWriter(sw));
			PushService.LOGGER.error(sw.toString());
		}

		return new HashMap<String, Registration>(){};
	}

	private static void setRegistrations(String username, Map<String, Registration> registrations) {
		try {
			if (username == null) {
				username = PentahoSessionHolder.getSession().getName();
			} else if (!PushService.canAdminister()) {
				throw new SecurityException("Unauthorized user");
			}

			synchronized (PushService.LOCK) {
				IUnifiedRepository repository = PentahoSystem.get(IUnifiedRepository.class);
				String homePath = ClientRepositoryPaths.getUserHomeFolderPath(username);
				Serializable userHomeId = repository.getFile(homePath).getId();
				Map<String, Serializable> userMetadata = repository.getFileMetadata(userHomeId);

				ObjectMapper mapper = new ObjectMapper();
				String rawRegistrations = mapper.writeValueAsString(registrations);
				userMetadata.put(PushService.REGISTRATIONS_SETTING_NAME, rawRegistrations);

				SecurityHelper.getInstance().runAsSystem((Callable<Void>) () -> {
					repository.setFileMetadata(userHomeId, userMetadata);
					return null;
				});
			}
		} catch (Exception ex) {
			StringWriter sw = new StringWriter();
			ex.printStackTrace(new PrintWriter(sw));
			PushService.LOGGER.error(sw.toString());
		}
	}

	public static List<Registration> list() {
		return PushService.list(null);
	}

	public static List<Registration> list(String username) {
		Map<String, Registration> registrations = PushService.getRegistrations(username);
		return new ArrayList<>(registrations.values());
	}

	public static void register(Registration registration) {
		PushService.register(null, registration);
	}

	public static void register(String username, Registration registration)  {
		Map<String, Registration> registrations = PushService.getRegistrations(username);
		registrations.put(registration.getDevice(), registration);
		PushService.setRegistrations(username, registrations);
	}

	public static void unregister(Registration registration) {
		PushService.unregister(null, registration);
	}

	public static void unregister(String username, Registration registration) {
		Map<String, Registration> registrations = PushService.getRegistrations(username);
		registrations.remove(registration.getDevice());
		PushService.setRegistrations(username, registrations);
	}

	public static void notify(Notification notification) {
		PushService.notify(null, notification);
	}

	public static void notify(String username, Notification notification) {
		Map<String, Registration> registrations = PushService.getRegistrations(username);
		for (Registration registration : registrations.values()) {
			HttpHeaders reqHeader = new HttpHeaders();
			reqHeader.put("Content-Type", Collections.singletonList("application/json"));

			ObjectMapper mapper = new ObjectMapper();
			ObjectNode reqBody = mapper.createObjectNode();
			reqBody.put("device", registration.getDevice());
			reqBody.put("instance", registration.getInstance());
			reqBody.put("secret", registration.getSecret());
			reqBody.set("notification", mapper.valueToTree(notification));

			HttpEntity<String> reqEntity = new HttpEntity<>(reqBody.toString(), reqHeader);

			try {
				RestTemplate restTemplate = new RestTemplate();
				restTemplate.exchange(PushService.NOTIFY_ENDPOINT_URL, HttpMethod.POST, reqEntity, String.class);
			} catch (HttpStatusCodeException ex) {
				PushService.LOGGER.error(ex.getMessage());
				if (ex.getStatusCode() == HttpStatus.UNAUTHORIZED || ex.getStatusCode() == HttpStatus.GONE) {
					PushService.unregister(username, registration);
				}
			}
		}
	}

}
