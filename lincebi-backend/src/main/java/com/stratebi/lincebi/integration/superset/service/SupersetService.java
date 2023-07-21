package com.stratebi.lincebi.integration.superset.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.stratebi.lincebi.integration.superset.config.SupersetConfig;
import com.stratebi.lincebi.integration.superset.model.AccessToken;
import com.stratebi.lincebi.integration.superset.model.CSRFToken;
import com.stratebi.lincebi.integration.superset.model.GuestToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

public class SupersetService {

	private static final Logger LOGGER = LoggerFactory.getLogger(SupersetService.class);

	private static final Pattern CSRF_COOKIE_PATTERN = Pattern.compile("^session=.+$", Pattern.CASE_INSENSITIVE);

	public static AccessToken getAccessToken(SupersetConfig config) throws JsonProcessingException {
		final String endpointUrl = config.url + "/api/v1/security/login";

		ObjectMapper mapper = new ObjectMapper();

		// Request headers
		HttpHeaders reqHeader = new HttpHeaders();
		reqHeader.put("Content-Type", Collections.singletonList("application/json"));
		reqHeader.put("Accept", Collections.singletonList("application/json"));

		// Request body
		ObjectNode reqBody = mapper.createObjectNode();
		reqBody.put("provider", config.provider);
		reqBody.put("username", config.username);
		reqBody.put("password", config.password);
		reqBody.put("refresh", false);

		// HTTP entity object, holds header and body
		HttpEntity<String> reqEntity = new HttpEntity<>(reqBody.toString(), reqHeader);

		// REST API get available feature
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<String> response = restTemplate.exchange(endpointUrl, HttpMethod.POST, reqEntity, String.class);

		HttpHeaders resHeader = response.getHeaders();
		String resBody = response.getBody();

		// Convert resBody string into AccessToken class object
		AccessToken accessToken = mapper.readValue(resBody, AccessToken.class);

		if (SupersetService.LOGGER.isDebugEnabled()) {
			// Log response
			SupersetService.LOGGER.info("Retrieved access token: {}", accessToken);
		}

		return accessToken;
	}

	public static CSRFToken getCSRFToken(SupersetConfig config, AccessToken accessToken) throws JsonProcessingException {
		final String endpointUrl = config.url + "/api/v1/security/csrf_token/";

		ObjectMapper mapper = new ObjectMapper();

		// Request headers
		HttpHeaders reqHeader = new HttpHeaders();
		reqHeader.put("Authorization", Collections.singletonList("Bearer " + accessToken.getToken()));
		reqHeader.put("Content-Type", Collections.singletonList("application/json"));
		reqHeader.put("Accept", Collections.singletonList("application/json"));

		// HTTP entity object, holds header and body
		HttpEntity<String> reqEntity = new HttpEntity<>(reqHeader);

		// REST API get available feature
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<String> response = restTemplate.exchange(endpointUrl, HttpMethod.GET, reqEntity, String.class);

		HttpHeaders resHeaders = response.getHeaders();
		String resBody = response.getBody();

		// Convert resBody string into CSRFToken class object
		CSRFToken csrfToken = mapper.readValue(resBody, CSRFToken.class);

		// Get CSRF cookie
		List<String> resHeadersSetCookie = resHeaders.get(HttpHeaders.SET_COOKIE);
		if (resHeadersSetCookie != null) {
			resHeadersSetCookie.stream()
					.filter(c -> CSRF_COOKIE_PATTERN.matcher(c).matches())
					.findFirst()
					.ifPresent(csrfToken::setCookie);
		}

		if (SupersetService.LOGGER.isDebugEnabled()) {
			// Log response
			SupersetService.LOGGER.info("Retrieved CSRF token: {}", csrfToken.getToken());
			SupersetService.LOGGER.info("Retrieved CSRF cookie: {}", csrfToken.getCookie());
		}

		return csrfToken;
	}

	public static GuestToken getGuestToken(SupersetConfig config, AccessToken accessToken, CSRFToken csrfToken, String dashboardId) throws JsonProcessingException {
		final String endpointUrl = config.url + "/api/v1/security/guest_token/";

		ObjectMapper mapper = new ObjectMapper();

		// Request headers
		HttpHeaders reqHeader = new HttpHeaders();
		reqHeader.put("Authorization", Collections.singletonList("Bearer " + accessToken.getToken()));
		reqHeader.put("X-CSRFToken", Collections.singletonList(csrfToken.getToken()));
		reqHeader.put("Cookie", Collections.singletonList(csrfToken.getCookie()));
		reqHeader.put("Content-Type", Collections.singletonList("application/json"));
		reqHeader.put("Accept", Collections.singletonList("application/json"));

		// Request body
		ObjectNode reqBody = mapper.createObjectNode();

		ArrayNode jsonResources = reqBody.putArray("resources");
		ObjectNode jsonResource = jsonResources.addObject();
		jsonResource.put("id", dashboardId);
		jsonResource.put("type", "dashboard");

		ObjectNode jsonUser = reqBody.putObject("user");
		jsonUser.put("username", config.guestUsername);
		jsonUser.put("first_name", config.guestFirstname);
		jsonUser.put("last_name", config.guestLastname);

		reqBody.putArray("rls");

		// HTTP entity object, holds header and body
		HttpEntity<String> reqEntity = new HttpEntity<>(reqBody.toString(), reqHeader);

		// REST API get available feature
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<String> response = restTemplate.exchange(endpointUrl, HttpMethod.POST, reqEntity, String.class);

		HttpHeaders resHeader = response.getHeaders();
		String resBody = response.getBody();

		// Convert resBody string into GuestToken class object
		GuestToken guestToken = mapper.readValue(resBody, GuestToken.class);
		guestToken.setExpSec(config.guestTokenExpSec);

		if (SupersetService.LOGGER.isDebugEnabled()) {
			// Log response
			SupersetService.LOGGER.info("Retrieved guest token: {}", guestToken);
		}

		return guestToken;
	}

}
