package com.stratebi.lincebi.integration.superset.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.stratebi.lincebi.integration.common.service.BIServerService;
import com.stratebi.lincebi.integration.superset.cache.EmbedConfigCache;
import com.stratebi.lincebi.integration.superset.config.SupersetConfig;
import com.stratebi.lincebi.integration.superset.model.AccessToken;
import com.stratebi.lincebi.integration.superset.model.CSRFToken;
import com.stratebi.lincebi.integration.superset.model.GuestToken;
import com.stratebi.lincebi.integration.superset.service.SupersetService;
import com.stratebi.lincebi.integration.superset.template.SupersetTemplateEngine;
import com.stratebi.lincebi.util.KeyUtils;
import com.stratebi.lincebi.util.UUIDUtils;
import org.codehaus.enunciate.Facet;
import org.ehcache.Cache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.thymeleaf.context.Context;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/lincebi/api/integration/superset/embed")
@Facet(name = "Unsupported")
public class EmbedController {

	private static final Logger LOGGER = LoggerFactory.getLogger(EmbedController.class);

	private static final Cache<String, GuestToken> CACHE = EmbedConfigCache.getInstance().cache;

	private static final SupersetTemplateEngine TEMPLATE_ENGINE = new SupersetTemplateEngine();

	@GET
	@Path("/info")
	@Produces({ MediaType.APPLICATION_JSON })
	@Facet(name = "Unsupported")
	public Response getInfoController(
		@QueryParam("configName") @DefaultValue("default") String configName,
		@QueryParam("dashboardId") String dashboardId
	) {
		SupersetConfig config = SupersetConfig.get(configName);
		if (config == null) {
			EmbedController.LOGGER.error("Invalid config");
			return Response.serverError().type(MediaType.TEXT_HTML).build();
		}

		if (!UUIDUtils.isGUID(dashboardId)) {
			EmbedController.LOGGER.error("Invalid id");
			return Response.serverError().type(MediaType.TEXT_HTML).build();
		}

		GuestToken guestToken;
		String cacheKey = KeyUtils.getKeyName(dashboardId);

		if (EmbedController.CACHE.containsKey(cacheKey)) {
			guestToken = EmbedController.CACHE.get(cacheKey);
		} else {
			try {
				AccessToken accessToken = SupersetService.getAccessToken(config);
				CSRFToken csrfToken = SupersetService.getCSRFToken(config, accessToken);
				guestToken = SupersetService.getGuestToken(config, accessToken, csrfToken, dashboardId);
			} catch (Exception ex) {
				EmbedController.LOGGER.error(ex.getMessage());
				return Response.serverError().type(MediaType.TEXT_HTML).build();
			}

			EmbedController.CACHE.put(cacheKey, guestToken);
		}

		String response;
		try {
			ObjectMapper mapper = new ObjectMapper();
			ObjectNode obj = mapper.createObjectNode();
			obj.put("externalUrl", config.externalUrl);
			obj.put("dashboardId", dashboardId);
			obj.put("guestToken", guestToken.getToken());
			response = obj.toString();
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
		@QueryParam("dashboardId") String dashboardId
	) {
		SupersetConfig config = SupersetConfig.get(configName);
		if (config == null) {
			EmbedController.LOGGER.error("Invalid config");
			return Response.serverError().type(MediaType.TEXT_HTML).build();
		}

		if (!UUIDUtils.isGUID(dashboardId)) {
			EmbedController.LOGGER.error("Invalid id");
			return Response.serverError().type(MediaType.TEXT_HTML).build();
		}

		GuestToken guestToken;
		String cacheKey = KeyUtils.getKeyName(dashboardId);

		if (EmbedController.CACHE.containsKey(cacheKey)) {
			guestToken = EmbedController.CACHE.get(cacheKey);
		} else {
			try {
				AccessToken accessToken = SupersetService.getAccessToken(config);
				CSRFToken csrfToken = SupersetService.getCSRFToken(config, accessToken);
				guestToken = SupersetService.getGuestToken(config, accessToken, csrfToken, dashboardId);
			} catch (Exception ex) {
				EmbedController.LOGGER.error(ex.getMessage());
				return Response.serverError().type(MediaType.TEXT_HTML).build();
			}

			EmbedController.CACHE.put(cacheKey, guestToken);
		}

		String response;
		try {
			Context context = new Context();
			context.setVariable("externalUrl", config.externalUrl);
			context.setVariable("dashboardId", dashboardId);
			context.setVariable("guestToken", guestToken.getToken());
			response = EmbedController.TEMPLATE_ENGINE.process("embed", context);
		} catch (Exception ex) {
			EmbedController.LOGGER.error(ex.getMessage());
			return Response.serverError().type(MediaType.TEXT_HTML).build();
		}

		return Response.ok(response).build();
	}

	@POST
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
