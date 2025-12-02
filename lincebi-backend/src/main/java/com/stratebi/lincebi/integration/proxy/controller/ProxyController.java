package com.stratebi.lincebi.integration.proxy.controller;

import com.stratebi.lincebi.integration.proxy.config.ProxyConfig;
import com.stratebi.lincebi.integration.proxy.service.ProxyService;
import org.codehaus.enunciate.Facet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HEAD;
import javax.ws.rs.OPTIONS;
import javax.ws.rs.PATCH;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.POST;
import javax.ws.rs.Produces;
import javax.ws.rs.PUT;

@Path("/lincebi/api/integration/proxy")
@Facet(name = "Unsupported")
public class ProxyController {

	private static final Logger LOGGER = LoggerFactory.getLogger(ProxyController.class);

	private static final Set<String> ALLOWED_SCHEMES = Set.of("http", "https");

	@GET @Path("/{scheme}/{host}/{port}") @Produces(MediaType.WILDCARD) @Facet(name = "Unsupported")
	public Response getRoot(@PathParam("scheme") String scheme, @PathParam("host") String host, @PathParam("port") int port, @Context UriInfo uriInfo, @Context HttpHeaders headers) {
		return proxy("GET", scheme, host, port, "", uriInfo, headers, null);
	}

	@GET @Path("/{scheme}/{host}/{port}/{path:.+}") @Produces(MediaType.WILDCARD) @Facet(name = "Unsupported")
	public Response getPath(@PathParam("scheme") String scheme, @PathParam("host") String host, @PathParam("port") int port, @PathParam("path") String path, @Context UriInfo uriInfo, @Context HttpHeaders headers) {
		return proxy("GET", scheme, host, port, path, uriInfo, headers, null);
	}

	@HEAD @Path("/{scheme}/{host}/{port}") @Produces(MediaType.WILDCARD) @Facet(name = "Unsupported")
	public Response headRoot(@PathParam("scheme") String scheme, @PathParam("host") String host, @PathParam("port") int port, @Context UriInfo uriInfo, @Context HttpHeaders headers) {
		return proxy("HEAD", scheme, host, port, "", uriInfo, headers, null);
	}

	@HEAD @Path("/{scheme}/{host}/{port}/{path:.+}") @Produces(MediaType.WILDCARD) @Facet(name = "Unsupported")
	public Response headPath(@PathParam("scheme") String scheme, @PathParam("host") String host, @PathParam("port") int port, @PathParam("path") String path, @Context UriInfo uriInfo, @Context HttpHeaders headers) {
		return proxy("HEAD", scheme, host, port, path, uriInfo, headers, null);
	}

	@OPTIONS @Path("/{scheme}/{host}/{port}") @Produces(MediaType.WILDCARD) @Facet(name = "Unsupported")
	public Response optionsRoot(@PathParam("scheme") String scheme, @PathParam("host") String host, @PathParam("port") int port, @Context UriInfo uriInfo, @Context HttpHeaders headers) {
		return proxy("OPTIONS", scheme, host, port, "", uriInfo, headers, null);
	}

	@OPTIONS @Path("/{scheme}/{host}/{port}/{path:.+}") @Produces(MediaType.WILDCARD) @Facet(name = "Unsupported")
	public Response optionsPath(@PathParam("scheme") String scheme, @PathParam("host") String host, @PathParam("port") int port, @PathParam("path") String path, @Context UriInfo uriInfo, @Context HttpHeaders headers) {
		return proxy("OPTIONS", scheme, host, port, path, uriInfo, headers, null);
	}

	@POST @Path("/{scheme}/{host}/{port}") @Produces(MediaType.WILDCARD) @Facet(name = "Unsupported")
	public Response postRoot(@PathParam("scheme") String scheme, @PathParam("host") String host, @PathParam("port") int port, @Context UriInfo uriInfo, @Context HttpHeaders headers, byte[] body) {
		return proxy("POST", scheme, host, port, "", uriInfo, headers, body);
	}

	@POST @Path("/{scheme}/{host}/{port}/{path:.+}") @Produces(MediaType.WILDCARD) @Facet(name = "Unsupported")
	public Response postPath(@PathParam("scheme") String scheme, @PathParam("host") String host, @PathParam("port") int port, @PathParam("path") String path, @Context UriInfo uriInfo, @Context HttpHeaders headers, byte[] body) {
		return proxy("POST", scheme, host, port, path, uriInfo, headers, body);
	}

	@PUT @Path("/{scheme}/{host}/{port}") @Produces(MediaType.WILDCARD) @Facet(name = "Unsupported")
	public Response putRoot(@PathParam("scheme") String scheme, @PathParam("host") String host, @PathParam("port") int port, @Context UriInfo uriInfo, @Context HttpHeaders headers, byte[] body) {
		return proxy("PUT", scheme, host, port, "", uriInfo, headers, body);
	}

	@PUT @Path("/{scheme}/{host}/{port}/{path:.+}") @Produces(MediaType.WILDCARD) @Facet(name = "Unsupported")
	public Response putPath(@PathParam("scheme") String scheme, @PathParam("host") String host, @PathParam("port") int port, @PathParam("path") String path, @Context UriInfo uriInfo, @Context HttpHeaders headers, byte[] body) {
		return proxy("PUT", scheme, host, port, path, uriInfo, headers, body);
	}

	@PATCH @Path("/{scheme}/{host}/{port}") @Produces(MediaType.WILDCARD) @Facet(name = "Unsupported")
	public Response patchRoot(@PathParam("scheme") String scheme, @PathParam("host") String host, @PathParam("port") int port, @Context UriInfo uriInfo, @Context HttpHeaders headers, byte[] body) {
		return proxy("PATCH", scheme, host, port, "", uriInfo, headers, body);
	}

	@PATCH @Path("/{scheme}/{host}/{port}/{path:.+}") @Produces(MediaType.WILDCARD) @Facet(name = "Unsupported")
	public Response patchPath(@PathParam("scheme") String scheme, @PathParam("host") String host, @PathParam("port") int port, @PathParam("path") String path, @Context UriInfo uriInfo, @Context HttpHeaders headers, byte[] body) {
		return proxy("PATCH", scheme, host, port, path, uriInfo, headers, body);
	}

	@DELETE @Path("/{scheme}/{host}/{port}") @Produces(MediaType.WILDCARD) @Facet(name = "Unsupported")
	public Response deleteRoot(@PathParam("scheme") String scheme, @PathParam("host") String host, @PathParam("port") int port, @Context UriInfo uriInfo, @Context HttpHeaders headers) {
		return proxy("DELETE", scheme, host, port, "", uriInfo, headers, null);
	}

	@DELETE @Path("/{scheme}/{host}/{port}/{path:.+}") @Produces(MediaType.WILDCARD) @Facet(name = "Unsupported")
	public Response deletePath(@PathParam("scheme") String scheme, @PathParam("host") String host, @PathParam("port") int port, @PathParam("path") String path, @Context UriInfo uriInfo, @Context HttpHeaders headers) {
		return proxy("DELETE", scheme, host, port, path, uriInfo, headers, null);
	}

	private Response proxy(String method, String scheme, String host, int port, String path, UriInfo uriInfo, HttpHeaders jaxRsHeaders, byte[] body) {
		if (!ProxyController.ALLOWED_SCHEMES.contains(scheme.toLowerCase())) {
			ProxyController.LOGGER.error("Invalid scheme: {}", scheme);
			return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity("Invalid scheme").build();
		}

		if (port < 1 || port > 65535) {
			ProxyController.LOGGER.error("Invalid port: {}", port);
			return Response.status(Response.Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN).entity("Invalid port").build();
		}

		String origin = scheme + "://" + host + ":" + port;
		boolean isDefaultPort = (port == 80 && scheme.equalsIgnoreCase("http")) || (port == 443 && scheme.equalsIgnoreCase("https"));
		if (ProxyConfig.allowedOrigins.stream().noneMatch(p -> p.matcher(origin).matches() || (isDefaultPort && p.matcher(scheme + "://" + host).matches()))) {
			ProxyController.LOGGER.error("Origin not allowed: {}", origin);
			return Response.status(Response.Status.FORBIDDEN).type(MediaType.TEXT_PLAIN).entity("Origin not allowed").build();
		}

		String targetUrl = origin + "/" + path;
		String queryString = uriInfo.getRequestUri().getRawQuery();
		if (queryString != null && !queryString.isEmpty()) {
			targetUrl += "?" + queryString;
		}

		Map<String, List<String>> headers = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
		jaxRsHeaders.getRequestHeaders().forEach((key, values) -> headers.put(key, new ArrayList<>(values)));

		String proxyPrefix = uriInfo.getBaseUri().getPath() + "lincebi/api/integration/proxy/" + scheme + "/" + host + "/" + port;

		try {
			ProxyService.ProxyResponse proxyResponse = ProxyService.fetchUrl(proxyPrefix, method, targetUrl, headers, body);

			Response.ResponseBuilder responseBuilder = Response.status(proxyResponse.statusCode()).entity(proxyResponse.body());
			if (proxyResponse.headers() != null) {
				proxyResponse.headers().forEach((key, values) -> {
					if (values != null) {
						for (String value : values) {
							responseBuilder.header(key, value);
						}
					}
				});
			}
			return responseBuilder.build();
		} catch (Exception ex) {
			ProxyController.LOGGER.error("Proxy error", ex);
			return Response.status(Response.Status.BAD_GATEWAY)
				.type(MediaType.TEXT_PLAIN)
				.entity("Proxy error: " + ex.getMessage())
				.build();
		}
	}

}
