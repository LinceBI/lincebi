package com.stratebi.lincebi.integration.powerbi.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriUtils;

import com.stratebi.lincebi.integration.powerbi.model.AvailableFeature;
import com.stratebi.lincebi.integration.powerbi.model.Dataset;
import com.stratebi.lincebi.integration.powerbi.model.EmbedConfig;
import com.stratebi.lincebi.integration.powerbi.model.Report;
import com.stratebi.lincebi.integration.powerbi.model.EmbedToken;

/**
 * Service with helper methods to get report's details and multi-resource embed token
 */
public class PowerBIService {

	private static final Logger LOGGER = LoggerFactory.getLogger(PowerBIService.class);

	/**
	 * Get an available feature
	 *
	 * @param {string} accessToken
	 * @param {string} featureName
	 * @return AvailableFeature object
	 * @throws JsonMappingException
	 * @throws JsonProcessingException
	 */
	public static AvailableFeature getAvailableFeature(String accessToken, String featureName) throws JsonMappingException, JsonProcessingException {
		StringBuilder urlStringBuilder = new StringBuilder("https://api.powerbi.com/v1.0/myorg/availableFeatures(featureName='");
		urlStringBuilder.append(UriUtils.encodeQueryParam(featureName, "UTF-8"));
		urlStringBuilder.append("')");

		// Request headers
		HttpHeaders reqHeader = new HttpHeaders();
		reqHeader.put("Content-Type", Arrays.asList("application/json"));
		reqHeader.put("Authorization", Arrays.asList("Bearer " + accessToken));

		// HTTP entity object, holds header and body
		HttpEntity<String> reqEntity = new HttpEntity<>(reqHeader);

		// REST API URL to get report details
		String endpointUrl = urlStringBuilder.toString();

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
	 *
	 * @param {string} accessToken
	 * @param {string} workspaceId
	 * @param {string} reportId
	 * @return Report object
	 * @throws JsonMappingException
	 * @throws JsonProcessingException
	 */
	public static Report getReport(String accessToken, String workspaceId, String reportId) throws JsonMappingException, JsonProcessingException {
		StringBuilder urlStringBuilder = new StringBuilder("https://api.powerbi.com/v1.0/myorg/groups/");
		urlStringBuilder.append(workspaceId);
		urlStringBuilder.append("/reports/");
		urlStringBuilder.append(reportId);

		// Request headers
		HttpHeaders reqHeader = new HttpHeaders();
		reqHeader.put("Content-Type", Arrays.asList("application/json"));
		reqHeader.put("Authorization", Arrays.asList("Bearer " + accessToken));

		// HTTP entity object, holds header and body
		HttpEntity<String> reqEntity = new HttpEntity<>(reqHeader);

		// REST API URL to get report details
		String endpointUrl = urlStringBuilder.toString();

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
	 * Get a dataset for a single workspace
	 *
	 * @param {string} accessToken
	 * @param {string} workspaceId
	 * @param {string} datasetId
	 * @return Dataset object
	 * @throws JsonMappingException
	 * @throws JsonProcessingException
	 */
	public static Dataset getDataset(String accessToken, String workspaceId, String datasetId) throws JsonMappingException, JsonProcessingException {
		StringBuilder urlStringBuilder = new StringBuilder("https://api.powerbi.com/v1.0/myorg/groups/");
		urlStringBuilder.append(workspaceId);
		urlStringBuilder.append("/datasets/");
		urlStringBuilder.append(datasetId);

		// Request headers
		HttpHeaders reqHeader = new HttpHeaders();
		reqHeader.put("Content-Type", Arrays.asList("application/json"));
		reqHeader.put("Authorization", Arrays.asList("Bearer " + accessToken));

		// HTTP entity object, holds header and body
		HttpEntity<String> reqEntity = new HttpEntity<>(reqHeader);

		// REST API URL to get dataset details
		String endpointUrl = urlStringBuilder.toString();

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
	 *
	 * @param {string} accessToken
	 * @param {string} workspaceId
	 * @param {string} reportId
	 * @return EmbedConfig object
	 * @throws JsonMappingException
	 * @throws JsonProcessingException
	 */
	public static EmbedConfig getEmbedConfig(String accessToken, String workspaceId, String reportId) throws JsonMappingException, JsonProcessingException {
		return PowerBIService.getEmbedConfig(accessToken, workspaceId, new ArrayList<String>(Arrays.asList(reportId)), new ArrayList<String>());
	}

	/**
	 * Get embed params for multiple reports for a single workspace
	 *
	 * @param {string}       accessToken
	 * @param {string}       workspaceId
	 * @param {List<string>} reportIds
	 * @return EmbedConfig object
	 * @throws JsonMappingException
	 * @throws JsonProcessingException
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
	 * @throws JsonMappingException
	 * @throws JsonProcessingException
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

		List<Report> reports = new ArrayList<Report>();
		embedConfig.setReports(reports);

		List<Dataset> datasets = new ArrayList<Dataset>();
		embedConfig.setDatasets(datasets);

		for (String reportId : reportIds) {
			Report report = getReport(accessToken, workspaceId, reportId);

			// Add report to client response object
			reports.add(report);

			// Add report dataset id to dataset id list
			datasetIds.add(report.getDatasetId());
		}

		for (String datasetId : datasetIds) {
			Dataset dataset = getDataset(accessToken, workspaceId, datasetId);

			// Add dataset to client response object
			datasets.add(dataset);
		}

		// Get embed token
		EmbedToken embedToken = PowerBIService.getEmbedToken(accessToken, reports, datasets);
		embedConfig.setEmbedToken(embedToken);

		return embedConfig;
	}

	/**
	 * Get embed token for multiple reports and datasets
	 *
	 * @param {string}       accessToken
	 * @param {List<string>} reportIds
	 * @param {List<string>} datasetIds
	 * @return EmbedToken object
	 * @throws JsonMappingException
	 * @throws JsonProcessingException
	 */
	public static EmbedToken getEmbedToken(String accessToken, List<Report> reports, List<Dataset> datasets) throws JsonMappingException, JsonProcessingException {
		final String endpointUrl = "https://api.powerbi.com/v1.0/myorg/GenerateToken";

		ObjectMapper mapper = new ObjectMapper();

		// Request headers
		HttpHeaders reqHeader = new HttpHeaders();
		reqHeader.put("Content-Type", Arrays.asList("application/json"));
		reqHeader.put("Authorization", Arrays.asList("Bearer " + accessToken));

		// Request body
		ObjectNode reqBody = mapper.createObjectNode();

		// Add dataset id to body
		ArrayNode jsonDatasets = reqBody.putArray("datasets");
		for (Dataset dataset : datasets) {
			ObjectNode jsonDataset = jsonDatasets.addObject();
			jsonDataset.put("id", dataset.getId());
		}

		// Add report id to body
		ArrayNode jsonReports = reqBody.putArray("reports");
		for (Report report : reports) {
			ObjectNode jsonReport = jsonReports.addObject();
			jsonReport.put("id", report.getId());
			jsonReport.put("allowEdit", false);
		}

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

		// Convert resBody string into Token class object
		EmbedToken token = mapper.readValue(resBody, EmbedToken.class);

		if (PowerBIService.LOGGER.isDebugEnabled()) {
			// Log request id
			List<String> reqIdList = resHeader.get("RequestId");
			if ((reqIdList != null) && !reqIdList.isEmpty()) {
				for (String reqId : reqIdList) {
					PowerBIService.LOGGER.debug("Request id: {}", reqId);
				}
			}

			// Log response
			PowerBIService.LOGGER.info("Retrieved embed token: {}", token);
		}

		return token;
	}

}
