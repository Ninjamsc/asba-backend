package com.technoserv.rest.resources;

import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.jaxrs.annotation.JacksonFeatures;
import com.technoserv.db.service.security.api.UserService;
import com.technoserv.db.service.security.impl.UserDTO;
import com.technoserv.utils.HttpUtils;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

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
     *
     * @return заявка по ID
     */
    @GET
    @Produces(HttpUtils.APPLICATION_JSON_UTF8)
    @Consumes(HttpUtils.APPLICATION_JSON_UTF8)
    @JacksonFeatures(serializationEnable = {SerializationFeature.INDENT_OUTPUT})
    @Path("/user/info")
    public UserDTO getCurrentUser() {
        return (UserDTO) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

}
