package com.technoserv.rest.resources;

import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.jaxrs.annotation.JacksonFeatures;
import com.technoserv.db.model.security.User;
import com.technoserv.db.service.security.api.UserService;
import com.technoserv.db.service.security.impl.UserDTO;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

/**
 * Created by 90630 on 17.01.2017.
 */
@Component
@Path("")
@Api(value = "User")
public class UserResource {

    @Autowired
    private UserService userService;

    /**
     * Получить стоп лист по ID
     * @return заявка по ID
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @JacksonFeatures( serializationEnable =  { SerializationFeature.INDENT_OUTPUT })
    @Path("/user/info")
    public UserDTO getCurrentUser() {
        return (UserDTO) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
