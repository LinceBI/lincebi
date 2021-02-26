package com.stratebi.lincebi.integration.powerbi.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.stratebi.lincebi.integration.powerbi.model.EmbedConfig;
import com.stratebi.lincebi.integration.powerbi.model.ReportConfig;
import com.stratebi.lincebi.integration.powerbi.model.TokenConfig;

/**
 * Service with helper methods to get report's details and multi-resource embed token
 */
public class PowerBIService {

	private static final Logger LOGGER = LoggerFactory.getLogger(PowerBIService.class);

	/**
	 * Get embed params for a report for a single workspace
	 *
	 * @param {string} accessToken
	 * @param {string} workspaceId
	 * @param {string} reportId
	 * @return EmbedConfig object
	 * @throws JsonProcessingException
	 * @throws JsonMappingException
	 * @throws JsonSyntaxException
	 */
	public static EmbedConfig getEmbedConfig(String accessToken, String workspaceId, String reportId) throws JsonMappingException, JsonProcessingException {
		return PowerBIService.getEmbedConfig(accessToken, workspaceId, Arrays.asList(reportId));
	}

	/**
	 * Get embed params for multiple reports for a single workspace
	 *
	 * @param {string}       accessToken
	 * @param {string}       workspaceId
	 * @param {List<string>} reportIds
	 * @return EmbedConfig object
	 * @throws JsonProcessingException
	 * @throws JsonMappingException
	 * @throws JsonSyntaxException
	 */
	public static EmbedConfig getEmbedConfig(String accessToken, String workspaceId, List<String> reportIds) throws JsonMappingException, JsonProcessingException {
		return PowerBIService.getEmbedConfig(accessToken, workspaceId, reportIds, new ArrayList<String>());
	}

	/**
	 * Get embed params for multiple reports for a single workspace
	 *
	 * @param {string}       accessToken
	 * @param {string}       workspaceId
	 * @param {List<string>} reportIds
	 * @param {List<string>} datasetIds
	 * @return EmbedConfig object
	 * @throws JsonProcessingException
	 * @throws JsonMappingException
	 * @throws JsonSyntaxException
	 */
	public static EmbedConfig getEmbedConfig(String accessToken, String workspaceId, List<String> reportIds, List<String> datasetIds) throws JsonMappingException, JsonProcessingException {
		if ((workspaceId == null) || workspaceId.isEmpty()) {
			throw new RuntimeException("Empty workspace id");
		}

		if ((reportIds == null) || reportIds.isEmpty()) {
			throw new RuntimeException("Empty report ids");
		}

		// Create embedding configuration object
		EmbedConfig embedConfig = new EmbedConfig();

		List<ReportConfig> embedReports = new ArrayList<ReportConfig>();
		embedConfig.setEmbedReports(embedReports);

		for (String reportId : reportIds) {
			// Get report in group API: https://api.powerbi.com/v1.0/myorg/groups/{workspaceId}/reports/{reportId}
			StringBuilder urlStringBuilder = new StringBuilder("https://api.powerbi.com/v1.0/myorg/groups/");
			urlStringBuilder.append(workspaceId);
			urlStringBuilder.append("/reports/");
			urlStringBuilder.append(reportId);

			// Request header
			HttpHeaders reqHeader = new HttpHeaders();
			reqHeader.put("Content-Type", Arrays.asList("application/json"));
			reqHeader.put("Authorization", Arrays.asList("Bearer " + accessToken));

			// HTTP entity object - holds header and body
			HttpEntity<String> reqEntity = new HttpEntity<>(reqHeader);

			// REST API URL to get report details
			String endPointUrl = urlStringBuilder.toString();

			// Rest API get report's details
			RestTemplate getReportRestTemplate = new RestTemplate();
			ResponseEntity<String> response = getReportRestTemplate.exchange(endPointUrl, HttpMethod.GET, reqEntity, String.class);

			HttpHeaders responseHeader = response.getHeaders();
			String responseBody = response.getBody();

			// Convert responseBody string into ReportConfig class object
			ObjectMapper mapper = new ObjectMapper();
			ReportConfig embedReport = mapper.readValue(responseBody, ReportConfig.class);

			// Add embed config to client response object
			embedReports.add(embedReport);

			// Add datasetId in the datasetIds
			datasetIds.add(embedReport.getDatasetId());

			if (PowerBIService.LOGGER.isDebugEnabled()) {
				// Get the request id
				List<String> reqIdList = responseHeader.get("RequestId");

				// Log progress
				PowerBIService.LOGGER.info("Retrieved report details");

				// Log request id
				if ((reqIdList != null) && !reqIdList.isEmpty()) {
					for (String reqId : reqIdList) {
						PowerBIService.LOGGER.info("Request Id: {}", reqId);
					}
				}
			}
		}

		// Get embed token
		TokenConfig embedToken = PowerBIService.getEmbedToken(accessToken, reportIds, datasetIds);
		embedConfig.setEmbedToken(embedToken);

		return embedConfig;
	}

	/**
	 * Get embed token for single report, multiple datasetIds, and optional target workspaces
	 *
	 * @see <a href="https://aka.ms/MultiResourceEmbedToken">Multi-Resource Embed Token</a>
	 * @param {string}       accessToken
	 * @param {string}       reportId
	 * @param {List<string>} datasetId
	 * @param {string}       targetWorkspaceIds
	 * @return EmbedToken
	 * @throws JsonProcessingException
	 * @throws JsonMappingException
	 */
	public static TokenConfig getEmbedToken(String accessToken, String reportId, List<String> datasetIds, String... targetWorkspaceIds) throws JsonMappingException, JsonProcessingException {
		return PowerBIService.getEmbedToken(accessToken, Arrays.asList(reportId), datasetIds, targetWorkspaceIds);
	}

	/**
	 * Get embed token for multiple reports, multiple datasetIds, and optional target workspaces
	 *
	 * @see <a href="https://aka.ms/MultiResourceEmbedToken">Multi-Resource Embed Token</a>
	 * @param {string}       accessToken
	 * @param {List<string>} reportIds
	 * @param {List<string>} datasetIds
	 * @param {string}       targetWorkspaceIds
	 * @return EmbedToken
	 * @throws JsonProcessingException
	 * @throws JsonMappingException
	 */
	public static TokenConfig getEmbedToken(String accessToken, List<String> reportIds, List<String> datasetIds, String... targetWorkspaceIds) throws JsonMappingException, JsonProcessingException {
		// Embed Token - Generate Token REST API
		final String uri = "https://api.powerbi.com/v1.0/myorg/GenerateToken";

		RestTemplate restTemplate = new RestTemplate();

		// Create request header
		HttpHeaders headers = new HttpHeaders();
		headers.put("Content-Type", Arrays.asList("application/json"));
		headers.put("Authorization", Arrays.asList("Bearer " + accessToken));

		// Create request body
		JsonObject requestBody = new JsonObject();

		// Add dataset id in body
		JsonArray jsonDatasets = new JsonArray();
		for (String datasetId : datasetIds) {
			JsonObject jsonDataset = new JsonObject();
			jsonDataset.addProperty("id", datasetId);
			jsonDatasets.add(jsonDataset);
		}
		requestBody.add("datasets", jsonDatasets);

		// Add report id in body
		JsonArray jsonReports = new JsonArray();
		for (String reportId : reportIds) {
			JsonObject jsonReport = new JsonObject();
			jsonReport.addProperty("id", reportId);
			jsonReport.addProperty("allowEdit", false);
			jsonReports.add(jsonReport);
		}
		requestBody.add("reports", jsonReports);

		// Add target workspace id in body
		JsonArray jsonWorkspaces = new JsonArray();
		for (String targetWorkspaceId : targetWorkspaceIds) {
			JsonObject jsonWorkspace = new JsonObject();
			jsonWorkspace.addProperty("id", targetWorkspaceId);
			jsonWorkspaces.add(jsonWorkspace);
		}
		requestBody.add("targetWorkspaces", jsonWorkspaces);

		// Add identity in body
		JsonArray jsonIdentities = new JsonArray();
		JsonObject jsonIdentity = new JsonObject();
		String username = BIServerService.getUser();
		jsonIdentity.addProperty("username", username);
		JsonArray jsonIdentityRoles = new JsonArray();
		for (String role : BIServerService.getRoles()) {
			jsonIdentityRoles.add(role);
		}
		jsonIdentity.add("roles", jsonIdentityRoles);
		JsonArray jsonIdentityDatasets = new JsonArray();
		for (String datasetId : datasetIds) {
			jsonIdentityDatasets.add(datasetId);
		}
		jsonIdentity.add("datasets", jsonIdentityDatasets);
		jsonIdentities.add(jsonIdentity);
		requestBody.add("identities", jsonIdentities);

		// Add (body, header) to HTTP entity
		HttpEntity<String> httpEntity = new HttpEntity<>(requestBody.toString(), headers);

		// Call the API
		ResponseEntity<String> response = restTemplate.postForEntity(uri, httpEntity, String.class);
		HttpHeaders responseHeader = response.getHeaders();
		String responseBody = response.getBody();

		// Convert responseBody string into TokenConfig class object
		ObjectMapper mapper = new ObjectMapper();
		TokenConfig embedToken = mapper.readValue(responseBody, TokenConfig.class);

		if (PowerBIService.LOGGER.isDebugEnabled()) {
			// Get the request id
			List<String> reqIdList = responseHeader.get("RequestId");

			// Log progress
			PowerBIService.LOGGER.debug("Retrieved embed token\nEmbed token id: {}", embedToken.getTokenId());

			// Log Request id
			if ((reqIdList != null) && !reqIdList.isEmpty()) {
				for (String reqId : reqIdList) {
					PowerBIService.LOGGER.debug("Request id: {}", reqId);
				}
			}
		}

		return embedToken;
	}

}
