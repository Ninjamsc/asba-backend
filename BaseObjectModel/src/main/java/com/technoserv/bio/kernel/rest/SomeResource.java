package com.technoserv.bio.kernel.rest;

import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.jaxrs.annotation.JacksonFeatures;
import com.technoserv.bio.kernel.rest.request.SomeRequest;
import com.technoserv.bio.kernel.rest.response.SomeResponse;
import io.swagger.annotations.Api;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/v1/sss")
@Api(value = "sss")
public class SomeResource {


    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @JacksonFeatures( serializationEnable =  { SerializationFeature.INDENT_OUTPUT })
    @Path("/qqq")
    public SomeResponse qqq(@QueryParam("aaa") String ddd) {
        SomeResponse response = new SomeResponse();
        response.setText(ddd);
        return response;
    }

    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @JacksonFeatures(serializationEnable =  { SerializationFeature.INDENT_OUTPUT })
    @Path("/bbb")
    public Response setReadStatus(SomeRequest someRequest) {
        return Response.ok().build();
    }
}