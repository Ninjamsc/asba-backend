package com.technoserv.rest.resource;

import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.jaxrs.annotation.JacksonFeatures;
import com.technoserv.db.model.configuration.FrontEnd;
import com.technoserv.db.service.configuration.api.FrontEndsService;
import com.technoserv.rest.exception.ErrorBean;
import io.swagger.annotations.Api;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
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

    private static final Log log = LogFactory.getLog(FrontEndsResource.class);

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
        FrontEnd clientByWorkstationName = frontEndsService.findByWorkstationName(entity.getWorkstationName());
        FrontEnd clientByUuid= frontEndsService.findByUuid(entity.getUuid());

        if (clientByWorkstationName == null && clientByUuid == null) {
            if (total < MAX_REGISETED_CLIENTS_NUMBER) {
                frontEndsService.save(entity);
                return Response.status(Response.Status.CREATED).build();
            } else {
                return Response.status(Response.Status.PAYMENT_REQUIRED).build();
            }
        } else {
            if (clientByWorkstationName == null || clientByUuid == null) {
                Response.ResponseBuilder responseBuilder = Response.status(Response.Status.NOT_ACCEPTABLE);
                String message = String.format("Can't register workstation: workstationName %s found, uuid %s found",
                        clientByWorkstationName == null ? "NOT" : "",
                        clientByUuid == null ? "NOT" : "");
                responseBuilder.entity(new ErrorBean(message));
                log.error(message);
                return responseBuilder.build();
            } else {
                return Response.status(Response.Status.ACCEPTED).build();
            }
        }
    }

}
