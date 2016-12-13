package com.technoserv.bio.kernel.rest.resource;

import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.jaxrs.annotation.JacksonFeatures;
import com.technoserv.bio.kernel.rest.UtilTest;
import com.technoserv.bio.kernel.rest.request.CompareServiceRequest;
import com.technoserv.db.service.objectmodel.api.CompareResultService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

/**
 * Created by sergey on 27.11.2016.
 */
@Component
@Path("/rest/compare-stub")
@Api(value = "CompareStub")
public class CompareExrenalStubResource {

    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @JacksonFeatures( serializationEnable =  { SerializationFeature.INDENT_OUTPUT })
    @Path("/template")
    public String find(CompareServiceRequest request) {
        String response;
        try {
            response = UtilTest.readFile("compare-stub-response-1.json");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return response;
    }
}