package com.technoserv.rest.resources;

import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.jaxrs.annotation.JacksonFeatures;
import com.technoserv.db.model.objectmodel.SkudResult;
import com.technoserv.db.service.objectmodel.api.SkudResultService;
import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.Collection;

@Component
@Path("")
@Api("Compare")
public class SkudResource implements InitializingBean {

    private static final Logger log = LoggerFactory.getLogger(SkudResource.class);

    @Autowired()
    @Qualifier("skudResultService")
    private SkudResultService skudResultService;

    @Override
    public void afterPropertiesSet() throws Exception {
        log.debug("afterPropertiesSet");
    }

    /**
     * Список всех стоп листов
     *
     * @return Список всех стоп листов
     */
    @Path("/results")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @JacksonFeatures(serializationEnable = {SerializationFeature.INDENT_OUTPUT})
    public Collection<SkudResult> list() {
        log.debug("list");
        return skudResultService.findAll();
    }

}