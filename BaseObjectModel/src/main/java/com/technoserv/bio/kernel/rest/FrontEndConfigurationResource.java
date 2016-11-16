package com.technoserv.bio.kernel.rest;


import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.jaxrs.annotation.JacksonFeatures;
import com.technoserv.bio.kernel.model.configuration.FrontEndConfiguration;
import com.technoserv.bio.kernel.service.Service;
import com.technoserv.bio.kernel.service.configuration.api.FrontEndConfigurationService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Collection;

@Component
@Path("/v1/front-end-configuration")
@Api(value = "front-end-configuration")
public class FrontEndConfigurationResource extends BaseResource<Long, FrontEndConfiguration> {

    @Autowired
    private FrontEndConfigurationService service;

    @Override
    protected Service<Long, FrontEndConfiguration> getBaseService() {
        return service;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @JacksonFeatures( serializationEnable =  { SerializationFeature.INDENT_OUTPUT })
    public Collection<FrontEndConfiguration> list() {
        return super.list();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @JacksonFeatures( serializationEnable =  { SerializationFeature.INDENT_OUTPUT })
    @Path("/{id}")
    public FrontEndConfiguration get(@PathParam("id") Long id) {
        return super.get(id);
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @JacksonFeatures( serializationEnable =  { SerializationFeature.INDENT_OUTPUT })
    public Long add(FrontEndConfiguration entity) {
        return super.add(entity);
    }

    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @JacksonFeatures(serializationEnable =  { SerializationFeature.INDENT_OUTPUT })
    public Response update(FrontEndConfiguration entity) {
        return super.update(entity);
    }

    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @JacksonFeatures(serializationEnable =  { SerializationFeature.INDENT_OUTPUT })
    @Path("/{id}")
    public Response delete(@PathParam("id") Long id) {
        return super.delete(id);
    }
}