package com.technoserv.bio.kernel.rest.resource;

import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.jaxrs.annotation.JacksonFeatures;
import com.technoserv.db.model.objectmodel.CompareResult;
import com.technoserv.db.service.Service;
import com.technoserv.db.service.objectmodel.api.CompareResultService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created by sergey on 27.11.2016.
 */
@Component
@Path("/rest/compare-result")
@Api(value = "Compare")
public class CompareResource {

    @Autowired
    private CompareResultService compareResultService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @JacksonFeatures( serializationEnable =  { SerializationFeature.INDENT_OUTPUT })
    @Path("/{id}") //id as wfm
    public Response find(@PathParam("id") Long id) {
        CompareResult compareResult = compareResultService.findById(id);
        Response response = compareResult !=null ? Response.ok(compareResult.getJson()).build()
        : Response.status(Response.Status.NOT_FOUND).build();
        return response;
    }
}