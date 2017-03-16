package com.technoserv.bio.kernel.rest.resource;

import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.jaxrs.annotation.JacksonFeatures;
import com.technoserv.db.model.configuration.FrontEnd;
import com.technoserv.db.service.Service;
import com.technoserv.db.service.configuration.api.FrontEndsService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.Collection;

@Component
@Path("/rest/front-ends")
@Api(value = "System Settings Rest API")
public class FrontEndsResource extends BaseResource<Long,FrontEnd>{

    @Autowired
    private FrontEndsService frontEndsService;

    @Override
    protected Service<Long, FrontEnd> getBaseService() {
        return frontEndsService;
    }

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
    @Override
    public Long add(FrontEnd entity) {
        return super.add(entity);
    }

}
