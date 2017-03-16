package com.technoserv.bio.kernel.rest.resource;

import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.jaxrs.annotation.JacksonFeatures;
import com.technoserv.db.model.configuration.FrontEnd;
import com.technoserv.db.service.configuration.api.FrontEndsService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Collection;

@Component
@Path("/rest/front-ends")
@Api(value = "System Settings Rest API")
public class FrontEndsResource {

    private static final int MAX_REGISETED_CLIENTS_NUMBER = 400;

    @Autowired
    private FrontEndsService frontEndsService;

    @Path("/list")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @JacksonFeatures( serializationEnable =  { SerializationFeature.INDENT_OUTPUT })
    public Collection<FrontEnd> list() {
        return frontEndsService.getAll();
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @JacksonFeatures( serializationEnable =  { SerializationFeature.INDENT_OUTPUT })
    public Response add(FrontEnd entity) {

        // check registered number
        int total = frontEndsService.getAll().size();

        // check already exists
        FrontEnd findObject = frontEndsService.findByUuid(entity.getUuid());
        if (findObject == null ) {
            if (total < MAX_REGISETED_CLIENTS_NUMBER) {
                frontEndsService.save(entity);
                return Response.status(Response.Status.CREATED).build();
            } else {
                return Response.status(Response.Status.PAYMENT_REQUIRED).build();
            }
        } else {
            return Response.status(Response.Status.ACCEPTED).build();
        }
    }

}
