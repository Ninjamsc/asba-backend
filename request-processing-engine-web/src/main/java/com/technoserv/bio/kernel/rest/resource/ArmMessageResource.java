package com.technoserv.bio.kernel.rest.resource;

import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.jaxrs.annotation.JacksonFeatures;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created by 90630 on 14.12.2016.
 */
@Component
@Path("/rest")
@Api(value = "Arm Rest API")
public class ArmMessageResource {

    @Autowired
    private JmsTemplate jmsTemplate;

    @Path("/arm")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @JacksonFeatures(serializationEnable =  { SerializationFeature.INDENT_OUTPUT })
    public Response save(String json) {
        jmsTemplate.convertAndSend("arm.queue", json);
        return Response.status(Response.Status.OK).build();
    }
}
