package com.stratebi.lincebi.integration.powerbi.controller;

import com.stratebi.lincebi.integration.powerbi.config.PowerBIConfig;
import com.stratebi.lincebi.integration.powerbi.template.PowerBITemplateEngine;
import com.stratebi.lincebi.util.UUIDUtils;
import org.codehaus.enunciate.Facet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.thymeleaf.context.Context;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/lincebi/api/integration/powerbi/pro")
@Facet(name = "Unsupported")
public class ProController {

	private static final Logger LOGGER = LoggerFactory.getLogger(ProController.class);

	private static final PowerBITemplateEngine TEMPLATE_ENGINE = new PowerBITemplateEngine();

	@GET
	@Path("/html")
	@Produces({ MediaType.TEXT_HTML })
	@Facet(name = "Unsupported")
	public Response getHTMLController(
		@QueryParam("configName") @DefaultValue("default") String configName,
		@QueryParam("workspaceId") String workspaceId,
		@QueryParam("reportId") String reportId,
		@QueryParam("reportPageName") String reportPageName,
		@QueryParam("dashboardId") String dashboardId
	) {
		PowerBIConfig config = PowerBIConfig.get(configName);
		if (config == null) {
			ProController.LOGGER.error("Invalid config");
			return Response.serverError().type(MediaType.TEXT_HTML).build();
		}

		if (!UUIDUtils.isGUID(workspaceId) || (!UUIDUtils.isGUID(reportId) && !UUIDUtils.isGUID(dashboardId))) {
			ProController.LOGGER.error("Invalid id");
			return Response.serverError().type(MediaType.TEXT_HTML).build();
		}

		String response;
		try {
			Context context = new Context();
			context.setVariable("clientId", config.clientId);
			context.setVariable("tenantId", config.serviceprincipalTenantId);
			context.setVariable("workspaceId", workspaceId);
			context.setVariable("reportId", reportId);
			context.setVariable("reportPageName", reportPageName);
			context.setVariable("dashboardId", dashboardId);
			response = ProController.TEMPLATE_ENGINE.process("pro", context);
		} catch (Exception ex) {
			ProController.LOGGER.error(ex.getMessage());
			return Response.serverError().type(MediaType.TEXT_HTML).build();
		}

		return Response.ok(response).build();
	}

}
