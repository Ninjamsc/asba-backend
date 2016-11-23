package com.technoserv.bio.kernel.rest;

import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.jaxrs.annotation.JacksonFeatures;
import com.technoserv.db.model.objectmodel.Request;
import com.technoserv.db.model.objectmodel.StopList;
import com.technoserv.db.service.Service;
import com.technoserv.db.service.objectmodel.api.StopListService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Collection;

/**
 * Created by sergey on 23.11.2016.
 */
@Component
@Path("/rest/stoplist")
@Api(value = "Stop-list")
public class StopListsResource extends BaseResource<Long,StopList> {

    @Autowired
    private StopListService stopListService;
    @Override
    protected Service<Long, StopList> getBaseService() {
        return stopListService;
    }

    /**
     * Список всех конфигураций
     * @return Список всех конфигураций
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @JacksonFeatures( serializationEnable =  { SerializationFeature.INDENT_OUTPUT })
    @Override
    public Collection<StopList> list() {
        return super.list();
    }

    /**
     * Получить конфигурацию по ID
     * @param id идентификатор.
     * @return Конфигурация по ID
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @JacksonFeatures( serializationEnable =  { SerializationFeature.INDENT_OUTPUT })
    @Path("/{id}")
    @Override
    public StopList get(@PathParam("id") Long id) {
        return super.get(id);
    }

    /**
     * Добавить конфигурацию.
     * @param entity добавляемая конфигурация.
     * @return Идентификатор добавленной конфигурации.
     */
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @JacksonFeatures( serializationEnable =  { SerializationFeature.INDENT_OUTPUT })
    @Override
    public Long add(StopList entity) {
        return super.add(entity);
    }
    /**
     * Обновить конфигурацию.
     * @param entity Обновляемая конфигурация.
     * @return ок
     */
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @JacksonFeatures(serializationEnable =  { SerializationFeature.INDENT_OUTPUT })
    @Override
    public Response update(StopList entity) {
        return super.update(entity);
    }
    /**
     * удалить конфигурацию.
     * @param id удаляемой конфигурации.
     * @return ок
     */
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @JacksonFeatures(serializationEnable =  { SerializationFeature.INDENT_OUTPUT })
    @Path("/{id}")
    @Override
    public Response delete(@PathParam("id") Long id) {
        return super.delete(id);
    }
}
