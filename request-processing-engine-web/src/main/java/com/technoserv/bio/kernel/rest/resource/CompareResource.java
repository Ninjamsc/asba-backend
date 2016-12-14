package com.technoserv.bio.kernel.rest.resource;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.jaxrs.annotation.JacksonFeatures;
import com.technoserv.db.model.objectmodel.CompareResult;
import com.technoserv.db.service.objectmodel.api.CompareResultService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;

/**
 * Created by sergey on 27.11.2016.
 */
@Component
@Path("/rest/compare-result")
@Api(value = "Compare Result Rest API")
public class CompareResource {

    @Autowired
    private CompareResultService compareResultService;

    private ObjectMapper objectMapper = new ObjectMapper();

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @JacksonFeatures( serializationEnable =  { SerializationFeature.INDENT_OUTPUT })
    @Path("/{id}") //id as wfm
    public Response find(@PathParam("id") Long id) throws IOException {
        CompareResult compareResult = compareResultService.findById(id);
        return compareResult !=null ? Response.ok(objectMapper.readValue(compareResult.getJson(),JsonNode.class)).build()
        : Response.status(Response.Status.NOT_FOUND).build();
    }

    @Path("/{id}")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @JacksonFeatures( serializationEnable =  { SerializationFeature.INDENT_OUTPUT })
    public Response save(@PathParam("id") Long id, String json) {
        CompareResult result = compareResultService.findById(id);
        if(result==null) {
            result = new CompareResult();
            result.setId(id);
        }
        result.setJson(json);
        compareResultService.saveOrUpdate(result);
        return Response.status(Response.Status.OK).build();
    }
}