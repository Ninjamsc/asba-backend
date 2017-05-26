package com.technoserv.rest.resource;


import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.jaxrs.annotation.JacksonFeatures;
import com.technoserv.db.model.configuration.FrontEndConfiguration;
import com.technoserv.db.service.Service;
import com.technoserv.db.service.configuration.api.FrontEndConfigurationService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Collection;

@Component
@Path("/rest/front-end-configuration")
@Api(value = "Front End Configuration Rest API")
public class FrontEndConfigurationResource extends BaseResource<Long, FrontEndConfiguration> {

    @Autowired
    private FrontEndConfigurationService service;

    @Override
    protected Service<Long, FrontEndConfiguration> getBaseService() {
        return service;
    }

    /**
     * Список всех конфигураций
     *
     * @return Список всех конфигураций
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @JacksonFeatures(serializationEnable = {SerializationFeature.INDENT_OUTPUT})
    @Override
    public Collection<FrontEndConfiguration> list() {
        return super.list();
    }

    /**
     * Получить конфигурацию по ID
     *
     * @param id идентификатор.
     * @return Конфигурация по ID
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @JacksonFeatures(serializationEnable = {SerializationFeature.INDENT_OUTPUT})
    @Path("/{id}")
    @Override
    public FrontEndConfiguration get(@PathParam("id") Long id) {
        return super.get(id);
    }

    /**
     * Добавить конфигурацию.
     *
     * @param entity добавляемая конфигурация.
     * @return Идентификатор добавленной конфигурации.
     */
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @JacksonFeatures(serializationEnable = {SerializationFeature.INDENT_OUTPUT})
    @Override
    public Long add(FrontEndConfiguration entity) {
        return super.add(entity);
    }

    /**
     * Обновить конфигурацию.
     *
     * @param entity Обновляемая конфигурация.
     * @return ок
     */
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @JacksonFeatures(serializationEnable = {SerializationFeature.INDENT_OUTPUT})
    @Override
    public Response update(FrontEndConfiguration entity) {
        return super.update(entity);
    }

    /**
     * удалить конфигурацию.
     *
     * @param id удаляемой конфигурации.
     * @return ок
     */
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @JacksonFeatures(serializationEnable = {SerializationFeature.INDENT_OUTPUT})
    @Path("/{id}")
    @Override
    public Response delete(@PathParam("id") Long id) {
        return super.delete(id);
    }
}