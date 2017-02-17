package com.technoserv.rest.resource;

import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.jaxrs.annotation.JacksonFeatures;
import com.technoserv.db.model.configuration.SystemSettings;
import com.technoserv.db.model.configuration.SystemSettingsType;
import com.technoserv.db.model.objectmodel.CompareResult;
import com.technoserv.db.model.objectmodel.Document;
import com.technoserv.db.model.objectmodel.StopList;
import com.technoserv.db.service.configuration.api.SystemSettingService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Collection;

/**
 * Created by 90630 on 14.12.2016.
 */
@Component
@Path("/rest/system-settings")
@Api(value = "System Settings Rest API")
public class SystemSettingsResource {

    @Autowired
    private SystemSettingService systemSettingService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @JacksonFeatures( serializationEnable =  { SerializationFeature.INDENT_OUTPUT })
    @Path("/{type}") //id as wfm
    public SystemSettings find(@PathParam("type") SystemSettingsType type) {
        return systemSettingService.findById(type);
    }

    @Path("/save")
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @JacksonFeatures(serializationEnable =  { SerializationFeature.INDENT_OUTPUT })
    public void add(SystemSettings systemSettings) {
        SystemSettings settings = systemSettingService.findById(systemSettings.getId());
        if(settings!=null) {
            settings.setValue(systemSettings.getValue());
        }
        systemSettingService.saveOrUpdate(settings);
    }


    @Path("/list")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @JacksonFeatures( serializationEnable =  { SerializationFeature.INDENT_OUTPUT })
    public Collection<SystemSettings> list() {
        return systemSettingService.getAll();
    }

}
