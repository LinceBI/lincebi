package com.stratebi.lincebi.integration.powerbi.controller;

import java.util.List;
import java.util.Set;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.codehaus.enunciate.Facet;
import org.ehcache.Cache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.thymeleaf.context.Context;

import com.stratebi.lincebi.integration.powerbi.cache.EmbedConfigCache;
import com.stratebi.lincebi.integration.powerbi.config.PowerBIConfig;
import com.stratebi.lincebi.integration.powerbi.model.AvailableFeature;
import com.stratebi.lincebi.integration.powerbi.model.EmbedConfig;
import com.stratebi.lincebi.integration.powerbi.service.AzureADService;
import com.stratebi.lincebi.integration.powerbi.service.BIServerService;
import com.stratebi.lincebi.integration.powerbi.service.PowerBIService;
import com.stratebi.lincebi.integration.powerbi.template.PowerBITemplateEngine;
import com.stratebi.lincebi.util.KeyUtils;
import com.stratebi.lincebi.util.UUIDUtils;

@Path("/lincebi/api/integration/powerbi/embed")
@Facet(name = "Unsupported")
public class EmbedController {

	private static final Logger LOGGER = LoggerFactory.getLogger(EmbedController.class);

	private static final Cache<String, EmbedConfig> CACHE = EmbedConfigCache.getInstance().cache;

	private static final PowerBITemplateEngine TEMPLATE_ENGINE = new PowerBITemplateEngine();

	@GET
	@Path("/info")
	@Produces({ MediaType.APPLICATION_JSON })
	@Facet(name = "Unsupported")
	public Response getInfoController(
		@QueryParam("configName") @DefaultValue("default") String configName,
		@QueryParam("workspaceId") String workspaceId,
		@QueryParam("reportId") String reportId,
		@QueryParam("datasetId") Set<String> datasetIds
	) {
		PowerBIConfig config = PowerBIConfig.get(configName);
		if (config == null) {
			EmbedController.LOGGER.error("Invalid config");
			return Response.serverError().type(MediaType.TEXT_HTML).build();
		}

		if (!UUIDUtils.isGUID(workspaceId) || !UUIDUtils.isGUID(reportId) || !UUIDUtils.isGUID(datasetIds)) {
			EmbedController.LOGGER.error("Invalid id");
			return Response.serverError().type(MediaType.TEXT_HTML).build();
		}

		String user = BIServerService.getUser();
		List<String> roles = BIServerService.getRoles();

		EmbedConfig embedConfig;
		String embedConfigCacheKey = KeyUtils.getKeyName(user, roles, workspaceId, reportId, datasetIds);

		if (EmbedController.CACHE.containsKey(embedConfigCacheKey)) {
			embedConfig = EmbedController.CACHE.get(embedConfigCacheKey);
		} else {
			String accessToken;

			try {
				accessToken = AzureADService.getAccessToken(config);
			} catch (InterruptedException ex) {
				EmbedController.LOGGER.error(ex.getMessage());
				Thread.currentThread().interrupt();
				return Response.serverError().type(MediaType.TEXT_HTML).build();
			} catch (Exception ex) {
				EmbedController.LOGGER.error(ex.getMessage());
				return Response.serverError().type(MediaType.TEXT_HTML).build();
			}

			try {
				embedConfig = PowerBIService.getEmbedConfig(accessToken, workspaceId, reportId, datasetIds);
			} catch (Exception ex) {
				EmbedController.LOGGER.error(ex.getMessage());
				return Response.serverError().type(MediaType.TEXT_HTML).build();
			}

			EmbedController.CACHE.put(embedConfigCacheKey, embedConfig);
		}

		String response;
		try {
			ObjectMapper mapper = new ObjectMapper();
			response = mapper.writeValueAsString(embedConfig);
		} catch (Exception ex) {
			EmbedController.LOGGER.error(ex.getMessage());
			return Response.serverError().type(MediaType.TEXT_HTML).build();
		}

		return Response.ok(response).build();
	}

	@GET
	@Path("/html")
	@Produces({ MediaType.TEXT_HTML })
	@Facet(name = "Unsupported")
	public Response getHTMLController(
		@QueryParam("configName") @DefaultValue("default") String configName,
		@QueryParam("workspaceId") String workspaceId,
		@QueryParam("reportId") String reportId,
		@QueryParam("datasetId") Set<String> datasetIds,
		@QueryParam("pageName") String pageName
	) {
		PowerBIConfig config = PowerBIConfig.get(configName);
		if (config == null) {
			EmbedController.LOGGER.error("Invalid config");
			return Response.serverError().type(MediaType.TEXT_HTML).build();
		}

		if (!UUIDUtils.isGUID(workspaceId) || !UUIDUtils.isGUID(reportId) || !UUIDUtils.isGUID(datasetIds)) {
			EmbedController.LOGGER.error("Invalid id");
			return Response.serverError().type(MediaType.TEXT_HTML).build();
		}

		String user = BIServerService.getUser();
		List<String> roles = BIServerService.getRoles();

		EmbedConfig embedConfig;
		String embedConfigCacheKey = KeyUtils.getKeyName(user, roles, workspaceId, reportId, datasetIds);

		if (EmbedController.CACHE.containsKey(embedConfigCacheKey)) {
			embedConfig = EmbedController.CACHE.get(embedConfigCacheKey);
		} else {
			String accessToken;

			try {
				accessToken = AzureADService.getAccessToken(config);
			} catch (InterruptedException ex) {
				EmbedController.LOGGER.error(ex.getMessage());
				Thread.currentThread().interrupt();
				return Response.serverError().type(MediaType.TEXT_HTML).build();
			} catch (Exception ex) {
				EmbedController.LOGGER.error(ex.getMessage());
				return Response.serverError().type(MediaType.TEXT_HTML).build();
			}

			try {
				embedConfig = PowerBIService.getEmbedConfig(accessToken, workspaceId, reportId, datasetIds);
			} catch (Exception ex) {
				EmbedController.LOGGER.error(ex.getMessage());
				return Response.serverError().type(MediaType.TEXT_HTML).build();
			}

			EmbedController.CACHE.put(embedConfigCacheKey, embedConfig);
		}

		String response;
		try {
			ObjectMapper mapper = new ObjectMapper();
			Context context = new Context();
			context.setVariable("embedConfig", mapper.writeValueAsString(embedConfig));
			context.setVariable("pageName", pageName);
			response = EmbedController.TEMPLATE_ENGINE.process("embed", context);
		} catch (Exception ex) {
			EmbedController.LOGGER.error(ex.getMessage());
			return Response.serverError().type(MediaType.TEXT_HTML).build();
		}

		return Response.ok(response).build();
	}

	@GET
	@Path("/available-feature")
	@Produces({ MediaType.APPLICATION_JSON })
	@Facet(name = "Unsupported")
	public Response getAvailableFeature(
		@QueryParam("configName") @DefaultValue("default") String configName,
		@QueryParam("featureName") String featureName
	) {
		if (!BIServerService.isAdmin()) {
			EmbedController.LOGGER.error("Unauthorized user");
			return Response.serverError().type(MediaType.TEXT_HTML).build();
		}

		PowerBIConfig config = PowerBIConfig.get(configName);
		if (config == null) {
			EmbedController.LOGGER.error("Invalid config");
			return Response.serverError().type(MediaType.TEXT_HTML).build();
		}

		if (featureName == null || featureName.isEmpty()) {
			EmbedController.LOGGER.error("Invalid feature name");
			return Response.serverError().type(MediaType.TEXT_HTML).build();
		}

		AvailableFeature availableFeature;
		String accessToken;

		try {
			accessToken = AzureADService.getAccessToken(config);
		} catch (InterruptedException ex) {
			EmbedController.LOGGER.error(ex.getMessage());
			Thread.currentThread().interrupt();
			return Response.serverError().type(MediaType.TEXT_HTML).build();
		} catch (Exception ex) {
			EmbedController.LOGGER.error(ex.getMessage());
			return Response.serverError().type(MediaType.TEXT_HTML).build();
		}

		try {
			availableFeature = PowerBIService.getAvailableFeature(accessToken, featureName);
		} catch (Exception ex) {
			EmbedController.LOGGER.error(ex.getMessage());
			return Response.serverError().type(MediaType.TEXT_HTML).build();
		}

		String response;
		try {
			ObjectMapper mapper = new ObjectMapper();
			response = mapper.writeValueAsString(availableFeature);
		} catch (Exception ex) {
			EmbedController.LOGGER.error(ex.getMessage());
			return Response.serverError().type(MediaType.TEXT_HTML).build();
		}

		return Response.ok(response).build();
	}

	@GET
	@Path("/clear-cache")
	@Produces({ MediaType.TEXT_PLAIN })
	@Facet(name = "Unsupported")
	public Response clearCacheController() {
		if (!BIServerService.isAdmin()) {
			EmbedController.LOGGER.error("Unauthorized user");
			return Response.serverError().type(MediaType.TEXT_HTML).build();
		}

		EmbedController.CACHE.clear();
		return Response.noContent().build();
	}

}
