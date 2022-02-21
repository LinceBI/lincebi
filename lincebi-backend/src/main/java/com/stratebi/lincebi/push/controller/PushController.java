package com.stratebi.lincebi.push.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.stratebi.lincebi.push.model.Notification;
import com.stratebi.lincebi.push.model.Registration;
import com.stratebi.lincebi.push.service.PushService;
import org.codehaus.enunciate.Facet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;

@Path("/lincebi/api/push")
@Facet(name = "Unsupported")
public class PushController {

	private static final Logger LOGGER = LoggerFactory.getLogger(PushController.class);

	@GET
	@Path("/list")
	@Produces({ MediaType.APPLICATION_JSON })
	@Facet(name = "Unsupported")
	public Response listController(@QueryParam("username") String username) {
		try {
			ObjectMapper mapper = new ObjectMapper();
			List<Registration> registrations = PushService.list(username);
			String response = mapper.writeValueAsString(registrations);
			return Response.ok(response).build();
		} catch (Exception ex) {
			StringWriter sw = new StringWriter();
			ex.printStackTrace(new PrintWriter(sw));
			PushController.LOGGER.error(sw.toString());
			return Response.serverError().type(MediaType.TEXT_HTML).build();
		}
	}

	@POST
	@Path("/register")
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces({ MediaType.TEXT_PLAIN })
	@Facet(name = "Unsupported")
	public Response registerController(
		@QueryParam("username") String username,
		String rawRegistration
	) {
		try {
			ObjectMapper mapper = new ObjectMapper();
			Registration registration = mapper.readValue(rawRegistration, Registration.class);
			PushService.register(username, registration);
			return Response.noContent().build();
		} catch (Exception ex) {
			StringWriter sw = new StringWriter();
			ex.printStackTrace(new PrintWriter(sw));
			PushController.LOGGER.error(sw.toString());
			return Response.serverError().type(MediaType.TEXT_HTML).build();
		}
	}

	@POST
	@Path("/unregister")
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces({ MediaType.TEXT_PLAIN })
	@Facet(name = "Unsupported")
	public Response unregisterController(
		@QueryParam("username") String username,
		String rawRegistration
	) {
		try {
			ObjectMapper mapper = new ObjectMapper();
			Registration registration = mapper.readValue(rawRegistration, Registration.class);
			PushService.unregister(username, registration);
			return Response.noContent().build();
		} catch (Exception ex) {
			StringWriter sw = new StringWriter();
			ex.printStackTrace(new PrintWriter(sw));
			PushController.LOGGER.error(sw.toString());
			return Response.serverError().type(MediaType.TEXT_HTML).build();
		}
	}

	@POST
	@Path("/notify")
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces({ MediaType.TEXT_PLAIN })
	@Facet(name = "Unsupported")
	public Response notifyController(
		@QueryParam("username") String username,
		String rawNotification
	) {
		try {
			ObjectMapper mapper = new ObjectMapper();
			Notification notification = mapper.readValue(rawNotification, Notification.class);
			PushService.notify(username, notification);
			return Response.noContent().build();
		} catch (Exception ex) {
			StringWriter sw = new StringWriter();
			ex.printStackTrace(new PrintWriter(sw));
			PushController.LOGGER.error(sw.toString());
			return Response.serverError().type(MediaType.TEXT_HTML).build();
		}
	}

}
