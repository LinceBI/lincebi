package com.stratebi.lincebi.serverinfo.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.stratebi.lincebi.serverinfo.model.ServerInfo;
import com.stratebi.lincebi.serverinfo.service.ServerInfoService;
import org.codehaus.enunciate.Facet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.PrintWriter;
import java.io.StringWriter;

@Path("/lincebi/api/serverinfo")
@Facet(name = "Unsupported")
public class ServerInfoController {

	private static final Logger LOGGER = LoggerFactory.getLogger(ServerInfoController.class);

	@GET
	@Path("/get")
	@Produces({ MediaType.APPLICATION_JSON })
	@Facet(name = "Unsupported")
	public Response serverInfoController() {
		try {
			ObjectMapper mapper = new ObjectMapper();
			ServerInfo serverInfo = ServerInfoService.get();
			String response = mapper.writeValueAsString(serverInfo);
			return Response.ok(response).build();
		} catch (Exception ex) {
			StringWriter sw = new StringWriter();
			ex.printStackTrace(new PrintWriter(sw));
			ServerInfoController.LOGGER.error(sw.toString());
			return Response.serverError().type(MediaType.TEXT_HTML).build();
		}
	}

}
