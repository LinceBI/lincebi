package com.stratebi.lincebi.controllers;

import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.codehaus.enunciate.Facet;

@Path("/lincebi/api")
@Facet(name = "Unsupported")
public class DummyController {

	@GET
	@Path("/dummy")
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces({ MediaType.APPLICATION_JSON })
	@Facet (name = "Unsupported")
	public Response dummyController() {
		return Response.ok().build();
	}

}
