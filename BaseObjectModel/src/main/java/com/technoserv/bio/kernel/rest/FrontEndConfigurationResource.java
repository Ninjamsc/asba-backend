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
    @Path("list")
    public Collection<FrontEndConfiguration> list() {
        return getBaseService().getAll();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @JacksonFeatures( serializationEnable =  { SerializationFeature.INDENT_OUTPUT })
    public FrontEndConfiguration get(@QueryParam("id") Long id) {
        return getBaseService().findById(id);
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @JacksonFeatures( serializationEnable =  { SerializationFeature.INDENT_OUTPUT })
    public Long add(FrontEndConfiguration entity) {
        return getBaseService().save(entity);
    }

    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @JacksonFeatures(serializationEnable =  { SerializationFeature.INDENT_OUTPUT })
    public Response update(FrontEndConfiguration entity) {
        getBaseService().saveOrUpdate(entity);
        return Response.ok().build();
    }

    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @JacksonFeatures(serializationEnable =  { SerializationFeature.INDENT_OUTPUT })
    public Response delete(@QueryParam("id") Long id) {
        getBaseService().delete(id);
        return Response.ok().build();
    }
}