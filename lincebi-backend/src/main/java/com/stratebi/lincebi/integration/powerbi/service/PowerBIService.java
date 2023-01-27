package com.stratebi.lincebi.integration.powerbi.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.stratebi.lincebi.integration.powerbi.model.AvailableFeature;
import com.stratebi.lincebi.integration.powerbi.model.Dashboard;
import com.stratebi.lincebi.integration.powerbi.model.Dataset;
import com.stratebi.lincebi.integration.powerbi.model.EmbedConfig;
import com.stratebi.lincebi.integration.powerbi.model.EmbedToken;
import com.stratebi.lincebi.integration.powerbi.model.Report;
import com.stratebi.lincebi.integration.powerbi.model.Tile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriUtils;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Service with helper methods to get report's details and multi-resource embed token
 */
public class PowerBIService {

	private static final Logger LOGGER = LoggerFactory.getLogger(PowerBIService.class);

	/**
	 * Get an available feature
	 */
	public static AvailableFeature getAvailableFeature(String accessToken, String featureName) throws JsonProcessingException {
		// REST API URL to get report details
		final String endpointUrl = "https://api.powerbi.com/v1.0/myorg/availableFeatures(featureName='" + UriUtils.encodeQueryParam(featureName, "UTF-8") + "')";

		// Request headers
		HttpHeaders reqHeader = new HttpHeaders();
		reqHeader.put("Content-Type", Collections.singletonList("application/json"));
		reqHeader.put("Authorization", Collections.singletonList("Bearer " + accessToken));

		// HTTP entity object, holds header and body
		HttpEntity<String> reqEntity = new HttpEntity<>(reqHeader);

		// REST API get available feature
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<String> response = restTemplate.exchange(endpointUrl, HttpMethod.GET, reqEntity, String.class);

		HttpHeaders resHeader = response.getHeaders();
		String resBody = response.getBody();

		// Convert resBody string into AvailableFeature class object
		ObjectMapper mapper = new ObjectMapper();
		AvailableFeature availableFeature = mapper.readValue(resBody, AvailableFeature.class);

		if (PowerBIService.LOGGER.isDebugEnabled()) {
			// Log request id
			List<String> reqIdList = resHeader.get("RequestId");
			if ((reqIdList != null) && !reqIdList.isEmpty()) {
				for (String reqId : reqIdList) {
					PowerBIService.LOGGER.info("Request id: {}", reqId);
				}
			}

			// Log response
			PowerBIService.LOGGER.info("Retrieved available feature: {}", availableFeature);
		}

		return availableFeature;
	}

	/**
	 * Get a report for a single workspace
	 */
	public static Report getReport(String accessToken, String workspaceId, String reportId) throws JsonProcessingException {
		// REST API URL to get report details
		final String endpointUrl = "https://api.powerbi.com/v1.0/myorg/groups/" + workspaceId + "/reports/" + reportId;

		// Request headers
		HttpHeaders reqHeader = new HttpHeaders();
		reqHeader.put("Content-Type", Collections.singletonList("application/json"));
		reqHeader.put("Authorization", Collections.singletonList("Bearer " + accessToken));

		// HTTP entity object, holds header and body
		HttpEntity<String> reqEntity = new HttpEntity<>(reqHeader);

		// REST API get report details
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<String> response = restTemplate.exchange(endpointUrl, HttpMethod.GET, reqEntity, String.class);

		HttpHeaders resHeader = response.getHeaders();
		String resBody = response.getBody();

		// Convert resBody string into Report class object
		ObjectMapper mapper = new ObjectMapper();
		Report report = mapper.readValue(resBody, Report.class);

		if (PowerBIService.LOGGER.isDebugEnabled()) {
			// Log request id
			List<String> reqIdList = resHeader.get("RequestId");
			if ((reqIdList != null) && !reqIdList.isEmpty()) {
				for (String reqId : reqIdList) {
					PowerBIService.LOGGER.info("Request id: {}", reqId);
				}
			}

			// Log response
			PowerBIService.LOGGER.info("Retrieved report: {}", report);
		}

		return report;
	}

	/**
	 * Get a dashboard for a single workspace
	 */
	public static Dashboard getDashboard(String accessToken, String workspaceId, String dashboardId) throws JsonProcessingException {
		// REST API URL to get dashboard details
		final String endpointUrl = "https://api.powerbi.com/v1.0/myorg/groups/" + workspaceId + "/dashboards/" + dashboardId;

		// Request headers
		HttpHeaders reqHeader = new HttpHeaders();
		reqHeader.put("Content-Type", Collections.singletonList("application/json"));
		reqHeader.put("Authorization", Collections.singletonList("Bearer " + accessToken));

		// HTTP entity object, holds header and body
		HttpEntity<String> reqEntity = new HttpEntity<>(reqHeader);

		// REST API get dashboard details
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<String> response = restTemplate.exchange(endpointUrl, HttpMethod.GET, reqEntity, String.class);

		HttpHeaders resHeader = response.getHeaders();
		String resBody = response.getBody();

		// Convert resBody string into Dashboard class object
		ObjectMapper mapper = new ObjectMapper();
		Dashboard dashboard = mapper.readValue(resBody, Dashboard.class);

		if (PowerBIService.LOGGER.isDebugEnabled()) {
			// Log request id
			List<String> reqIdList = resHeader.get("RequestId");
			if ((reqIdList != null) && !reqIdList.isEmpty()) {
				for (String reqId : reqIdList) {
					PowerBIService.LOGGER.info("Request id: {}", reqId);
				}
			}

			// Log response
			PowerBIService.LOGGER.info("Retrieved dashboard: {}", dashboard);
		}

		return dashboard;
	}

	/**
	 * Get a dashboard for a single workspace
	 */
	public static List<Tile> getDashboardTiles(String accessToken, String workspaceId, String dashboardId) throws JsonProcessingException {
		// REST API URL to get dashboard tiles details
		final String endpointUrl = "https://api.powerbi.com/v1.0/myorg/groups/" + workspaceId + "/dashboards/" + dashboardId + "/tiles";

		// Request headers
		HttpHeaders reqHeader = new HttpHeaders();
		reqHeader.put("Content-Type", Collections.singletonList("application/json"));
		reqHeader.put("Authorization", Collections.singletonList("Bearer " + accessToken));

		// HTTP entity object, holds header and body
		HttpEntity<String> reqEntity = new HttpEntity<>(reqHeader);

		// REST API get dashboard details
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<String> response = restTemplate.exchange(endpointUrl, HttpMethod.GET, reqEntity, String.class);

		HttpHeaders resHeader = response.getHeaders();
		String resBody = response.getBody();

		// Convert resBody string into Dashboard class object
		ObjectMapper mapper = new ObjectMapper();
		ObjectNode resJson = mapper.readValue(resBody, ObjectNode.class);
		List<Tile> tiles = null;
		try {
			tiles = mapper.readValue(mapper.treeAsTokens(resJson.get("value")), new TypeReference<List<Tile>>(){});
		} catch (Exception e) {
			throw new JsonProcessingException("Error"){};
		}

		if (PowerBIService.LOGGER.isDebugEnabled()) {
			// Log request id
			List<String> reqIdList = resHeader.get("RequestId");
			if ((reqIdList != null) && !reqIdList.isEmpty()) {
				for (String reqId : reqIdList) {
					PowerBIService.LOGGER.info("Request id: {}", reqId);
				}
			}

			// Log response
			PowerBIService.LOGGER.info("Retrieved dashboard tiles: {}", tiles);
		}

		return tiles;
	}

	/**
	 * Get a dataset for a single workspace
	 */
	public static Dataset getDataset(String accessToken, String workspaceId, String datasetId) throws JsonProcessingException {
		// REST API URL to get dataset details
		final String endpointUrl = "https://api.powerbi.com/v1.0/myorg/groups/" + workspaceId + "/datasets/" + datasetId;

		// Request headers
		HttpHeaders reqHeader = new HttpHeaders();
		reqHeader.put("Content-Type", Collections.singletonList("application/json"));
		reqHeader.put("Authorization", Collections.singletonList("Bearer " + accessToken));

		// HTTP entity object, holds header and body
		HttpEntity<String> reqEntity = new HttpEntity<>(reqHeader);

		// REST API get dataset details
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<String> response = restTemplate.exchange(endpointUrl, HttpMethod.GET, reqEntity, String.class);

		HttpHeaders resHeader = response.getHeaders();
		String resBody = response.getBody();

		// Convert resBody string into Dataset class object
		ObjectMapper mapper = new ObjectMapper();
		Dataset dataset = mapper.readValue(resBody, Dataset.class);

		if (PowerBIService.LOGGER.isDebugEnabled()) {
			// Log request id
			List<String> reqIdList = resHeader.get("RequestId");
			if ((reqIdList != null) && !reqIdList.isEmpty()) {
				for (String reqId : reqIdList) {
					PowerBIService.LOGGER.info("Request id: {}", reqId);
				}
			}

			// Log response
			PowerBIService.LOGGER.info("Retrieved dataset: {}", dataset);
		}

		return dataset;
	}

	/**
	 * Get embed params for a report for a single workspace
	 */
	public static EmbedConfig getEmbedReportConfig(String accessToken, String workspaceId, String reportId) throws JsonProcessingException {
		EmbedConfig embedConfig = new EmbedConfig();

		embedConfig.setWorkspaceId(workspaceId);

		Report report = getReport(accessToken, workspaceId, reportId);
		embedConfig.setReport(report);

		Set<Dataset> datasets = new HashSet<>();
		embedConfig.setDatasets(datasets);

		Dataset dataset = getDataset(accessToken, workspaceId, report.getDatasetId());
		datasets.add(dataset);

		EmbedToken embedToken = PowerBIService.getEmbedToken(accessToken, workspaceId, report, datasets);
		embedConfig.setEmbedToken(embedToken);

		return embedConfig;
	}

	/**
	 * Get embed params for a dashboard for a single workspace
	 */
	public static EmbedConfig getEmbedDasboardConfig(String accessToken, String workspaceId, String dashboardId) throws JsonProcessingException {
		EmbedConfig embedConfig = new EmbedConfig();

		embedConfig.setWorkspaceId(workspaceId);

		Dashboard dashboard = getDashboard(accessToken, workspaceId, dashboardId);
		embedConfig.setDashboard(dashboard);

		Set<Dataset> datasets = new HashSet<>();
		embedConfig.setDatasets(datasets);

		List<Tile> tiles = getDashboardTiles(accessToken, workspaceId, dashboardId);
		Set<String> datasetIds = new HashSet<>();
		for (Tile tile : tiles) {
			datasetIds.add(tile.getDatasetId());
		}
		for (String datasetId : datasetIds) {
			Dataset dataset = getDataset(accessToken, workspaceId, datasetId);
			datasets.add(dataset);
		}

		EmbedToken embedToken = PowerBIService.getEmbedToken(accessToken, workspaceId, dashboard, datasets);
		embedConfig.setEmbedToken(embedToken);

		return embedConfig;
	}

	/**
	 * Get embed token for a report for a single workspace
	 */
	public static EmbedToken getEmbedToken(String accessToken, String workspaceId, Report report, Set<Dataset> datasets) throws JsonProcessingException {
		// REST API URL to get the embed token
		final String endpointUrl = "https://api.powerbi.com/v1.0/myorg/groups/" + workspaceId + "/reports/" + report.getId() + "/GenerateToken";

		ObjectMapper mapper = new ObjectMapper();

		// Request headers
		HttpHeaders reqHeader = new HttpHeaders();
		reqHeader.put("Content-Type", Collections.singletonList("application/json"));
		reqHeader.put("Authorization", Collections.singletonList("Bearer " + accessToken));

		// Request body
		ObjectNode reqBody = mapper.createObjectNode();
		reqBody.put("accessLevel", "View");

		// Add identity to body
		ArrayNode jsonIdentities = reqBody.putArray("identities");
		for (Dataset dataset : datasets) {
			if (dataset.getIsEffectiveIdentityRequired()) {
				ObjectNode jsonIdentity = jsonIdentities.addObject();

				ArrayNode jsonIdentityDatasets = jsonIdentity.putArray("datasets");
				jsonIdentityDatasets.add(dataset.getId());

				String username = BIServerService.getUser();
				jsonIdentity.put("username", username);

				if (dataset.getIsEffectiveIdentityRolesRequired()) {
					ArrayNode jsonIdentityRoles = jsonIdentity.putArray("roles");
					for (String role : BIServerService.getRoles()) {
						jsonIdentityRoles.add(role);
					}
				}
			}
		}

		// HTTP entity object, holds header and body
		HttpEntity<String> reqEntity = new HttpEntity<>(reqBody.toString(), reqHeader);

		// REST API get embed token
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<String> response = restTemplate.exchange(endpointUrl, HttpMethod.POST, reqEntity, String.class);

		HttpHeaders resHeader = response.getHeaders();
		String resBody = response.getBody();

		// Convert resBody string into EmbedToken class object
		EmbedToken embedToken = mapper.readValue(resBody, EmbedToken.class);

		if (PowerBIService.LOGGER.isDebugEnabled()) {
			// Log request id
			List<String> reqIdList = resHeader.get("RequestId");
			if ((reqIdList != null) && !reqIdList.isEmpty()) {
				for (String reqId : reqIdList) {
					PowerBIService.LOGGER.debug("Request id: {}", reqId);
				}
			}

			// Log response
			PowerBIService.LOGGER.info("Retrieved embed token: {}", embedToken);
		}

		return embedToken;
	}

	/**
	 * Get embed token for a dashboard for a single workspace
	 */
	public static EmbedToken getEmbedToken(String accessToken, String workspaceId, Dashboard dashboard, Set<Dataset> datasets) throws JsonProcessingException {
		// REST API URL to get the embed token
		final String endpointUrl = "https://api.powerbi.com/v1.0/myorg/groups/" + workspaceId + "/dashboards/" + dashboard.getId() + "/GenerateToken";

		ObjectMapper mapper = new ObjectMapper();

		// Request headers
		HttpHeaders reqHeader = new HttpHeaders();
		reqHeader.put("Content-Type", Collections.singletonList("application/json"));
		reqHeader.put("Authorization", Collections.singletonList("Bearer " + accessToken));

		// Request body
		ObjectNode reqBody = mapper.createObjectNode();
		reqBody.put("accessLevel", "View");

		// Add identity to body
		ArrayNode jsonIdentities = reqBody.putArray("identities");
		for (Dataset dataset : datasets) {
			if (dataset.getIsEffectiveIdentityRequired()) {
				ObjectNode jsonIdentity = jsonIdentities.addObject();

				ArrayNode jsonIdentityDatasets = jsonIdentity.putArray("datasets");
				jsonIdentityDatasets.add(dataset.getId());

				String username = BIServerService.getUser();
				jsonIdentity.put("username", username);

				if (dataset.getIsEffectiveIdentityRolesRequired()) {
					ArrayNode jsonIdentityRoles = jsonIdentity.putArray("roles");
					for (String role : BIServerService.getRoles()) {
						jsonIdentityRoles.add(role);
					}
				}
			}
		}

		// HTTP entity object, holds header and body
		HttpEntity<String> reqEntity = new HttpEntity<>(reqBody.toString(), reqHeader);

		// REST API get embed token
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<String> response = restTemplate.exchange(endpointUrl, HttpMethod.POST, reqEntity, String.class);

		HttpHeaders resHeader = response.getHeaders();
		String resBody = response.getBody();

		// Convert resBody string into EmbedToken class object
		EmbedToken embedToken = mapper.readValue(resBody, EmbedToken.class);

		if (PowerBIService.LOGGER.isDebugEnabled()) {
			// Log request id
			List<String> reqIdList = resHeader.get("RequestId");
			if ((reqIdList != null) && !reqIdList.isEmpty()) {
				for (String reqId : reqIdList) {
					PowerBIService.LOGGER.debug("Request id: {}", reqId);
				}
			}

			// Log response
			PowerBIService.LOGGER.info("Retrieved embed token: {}", embedToken);
		}

		return embedToken;
	}

}
