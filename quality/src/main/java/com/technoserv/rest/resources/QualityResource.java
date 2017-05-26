package com.technoserv.rest.resources;

import io.swagger.annotations.Api;
import org.springframework.stereotype.Component;

import javax.ws.rs.Consumes;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;


@Component
@Path("")
@Api(value = "Compare")

public class QualityResource {


    @PUT
    @Path("/test")
    //@Consumes(MediaType.APPLICATION_XML)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateMessage(QualityRequest message) {
        return Response.status(200).build();
    }


}