package com.stratebi.lincebi.permissions.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.stratebi.lincebi.permissions.cache.PermissionsCache;
import com.stratebi.lincebi.permissions.model.Role;
import com.stratebi.lincebi.permissions.model.User;
import com.stratebi.lincebi.permissions.service.PermissionsService;
import com.stratebi.lincebi.util.KeyUtils;
import org.codehaus.enunciate.Facet;
import org.ehcache.Cache;
import org.pentaho.platform.engine.core.system.PentahoSessionHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.PrintWriter;
import java.io.StringWriter;

@Path("/lincebi/api/permissions")
@Facet(name = "Unsupported")
public class PermissionsController {

	private static final Logger LOGGER = LoggerFactory.getLogger(PermissionsController.class);

	private static final Cache<String, String> CACHE = PermissionsCache.getInstance().cache;

	@GET
	@Path("/all-users")
	@Produces({ MediaType.APPLICATION_JSON })
	@Facet(name = "Unsupported")
	public Response allUsersController() {
		try {
			String response;
			String cacheKey = KeyUtils.getKeyName("all-users");
			if (PermissionsController.CACHE.containsKey(cacheKey)) {
				response = PermissionsController.CACHE.get(cacheKey);
			} else {
				ObjectMapper mapper = new ObjectMapper();
				PermissionsService permissionsService = new PermissionsService();
				User[] users = permissionsService.getAllUsers();
				response = mapper.writeValueAsString(users);
				PermissionsController.CACHE.put(cacheKey, response);
			}
			return Response.ok(response).build();
		} catch (Exception ex) {
			StringWriter sw = new StringWriter();
			ex.printStackTrace(new PrintWriter(sw));
			PermissionsController.LOGGER.error(sw.toString());
			return Response.serverError().type(MediaType.TEXT_HTML).build();
		}
	}

	@GET
	@Path("/all-roles")
	@Produces({ MediaType.APPLICATION_JSON })
	@Facet(name = "Unsupported")
	public Response allRolesController() {
		try {
			String response;
			String cacheKey = KeyUtils.getKeyName("all-roles");
			if (PermissionsController.CACHE.containsKey(cacheKey)) {
				response = PermissionsController.CACHE.get(cacheKey);
			} else {
				ObjectMapper mapper = new ObjectMapper();
				PermissionsService permissionsService = new PermissionsService();
				Role[] roles = permissionsService.getAllRoles();
				response = mapper.writeValueAsString(roles);
				PermissionsController.CACHE.put(cacheKey, response);
			}
			return Response.ok(response).build();
		} catch (Exception ex) {
			StringWriter sw = new StringWriter();
			ex.printStackTrace(new PrintWriter(sw));
			PermissionsController.LOGGER.error(sw.toString());
			return Response.serverError().type(MediaType.TEXT_HTML).build();
		}
	}

	@GET
	@Path("/own-roles")
	@Produces({ MediaType.APPLICATION_JSON })
	@Facet(name = "Unsupported")
	public Response ownRolesController() {
		try {
			String response;
			String userName = PentahoSessionHolder.getSession().getName();
			String cacheKey = KeyUtils.getKeyName(userName, "own-roles");
			if (PermissionsController.CACHE.containsKey(cacheKey)) {
				response = PermissionsController.CACHE.get(cacheKey);
			} else {
				ObjectMapper mapper = new ObjectMapper();
				PermissionsService permissionsService = new PermissionsService();
				Role[] roles = permissionsService.getUserRoles(userName);
				response = mapper.writeValueAsString(roles);
				PermissionsController.CACHE.put(cacheKey, response);
			}
			return Response.ok(response).build();
		} catch (Exception ex) {
			StringWriter sw = new StringWriter();
			ex.printStackTrace(new PrintWriter(sw));
			PermissionsController.LOGGER.error(sw.toString());
			return Response.serverError().type(MediaType.TEXT_HTML).build();
		}
	}

}
