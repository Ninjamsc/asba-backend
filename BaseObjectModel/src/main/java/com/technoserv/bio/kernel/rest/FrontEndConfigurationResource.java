package com.technoserv.bio.kernel.rest;


import com.technoserv.bio.kernel.model.configuration.FrontEndConfiguration;
import com.technoserv.bio.kernel.service.Service;
import com.technoserv.bio.kernel.service.configuration.api.FrontEndConfigurationService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;

@Component
@Path("/v1/front-end-configuration")
@Api(value = "front-end-configuration")
public class FrontEndConfigurationResource extends BaseResource<Long, FrontEndConfiguration> {

    @Autowired
    private FrontEndConfigurationService service;

    @Override
    protected Service<Long, FrontEndConfiguration> getBaseService() {
        return service;
    }
}