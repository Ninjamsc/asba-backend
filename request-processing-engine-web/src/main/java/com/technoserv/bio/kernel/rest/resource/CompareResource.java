package com.technoserv.bio.kernel.rest.resource;

import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.jaxrs.annotation.JacksonFeatures;
import com.technoserv.db.model.objectmodel.CompareResult;
import com.technoserv.db.service.Service;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

/**
 * Created by sergey on 27.11.2016.
 */
@Component
@Path("/rest/compare-result")
@Api(value = "Compare")
public class CompareResource {

    @Autowired
    private Service<Long, CompareResult> compareResultService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @JacksonFeatures( serializationEnable =  { SerializationFeature.INDENT_OUTPUT })
    @Path("/{id}")
    public String find(@PathParam("id") Long id) {
        return compareResultService.findById(id).getJson();
    }
}