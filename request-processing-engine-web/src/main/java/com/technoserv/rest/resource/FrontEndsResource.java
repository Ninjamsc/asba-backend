package com.technoserv.rest.resource;

import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.jaxrs.annotation.JacksonFeatures;
import com.technoserv.db.model.configuration.FrontEnd;
import com.technoserv.db.service.configuration.api.FrontEndsService;
import com.technoserv.rest.exception.ErrorBean;
import io.swagger.annotations.Api;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Collection;

@Component
@Path("/rest/front-ends")
@Api(value = "System Settings Rest API")
public class FrontEndsResource {

    private static final Log log = LogFactory.getLog(FrontEndsResource.class);

    private static final int MAX_REGISTERED_CLIENTS_NUMBER = 850;

    @Autowired
    private FrontEndsService frontEndsService;

    @Path("/list")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @JacksonFeatures(serializationEnable = {SerializationFeature.INDENT_OUTPUT})
    public Collection<FrontEnd> list() {
        return frontEndsService.getAll();
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @JacksonFeatures(serializationEnable = {SerializationFeature.INDENT_OUTPUT})
    public Response add(FrontEnd entity) {
        System.out.println("++++++++++++++++++FrontEnd=" + entity);

        // check registered number
        int total = frontEndsService.getAll().size();
        System.out.println("++++++++++++++++++total = " + total + " MAX_REGISTERED_CLIENTS_NUMBER=" + MAX_REGISTERED_CLIENTS_NUMBER);
        // check already exists
        FrontEnd clientByWorkstationName = frontEndsService.findByWorkstationName(entity.getWorkstationName());
        FrontEnd clientByUuid = frontEndsService.findByUuid(entity.getUuid());

        //uuid УНИКАЛЕН! В случае не совпадения НАСТОЯЩЕГО юзера с ЮЗЕРОМ из БД возможно лишь изменить запись
        if (clientByWorkstationName == null && clientByUuid == null) { //Если ни станции ни ключа машины не найдено, то смело пишем новую регистрацию
            if (total < MAX_REGISTERED_CLIENTS_NUMBER) {
                frontEndsService.save(entity);
                return Response.status(Response.Status.CREATED).build();
            } else {
                return Response.status(Response.Status.PAYMENT_REQUIRED).build();
            }
        } else {
            /*Здесь смотрим наличие ключа машины и перезаписываем нового чела на ней
                Проблемма возникла когда тестировщики поменялись компьютерами и поменяли WorkstationName на них
                Получилось что по отдельности мы находим либо человека, либо его АРМ. Поэтому было решено при наличии машины в нашей БД, но новом юзере или WorkstationName на ней
                перезаписываем регистрационную запись для этого компьютера.
             */
            if (clientByUuid != null) {
                //clientByUuid.setUuid(entity.getUuid());
                clientByUuid.setWorkstationName(entity.getWorkstationName());
                clientByUuid.setOsVersion(entity.getOsVersion());
                clientByUuid.setUsername(entity.getUsername());
                clientByUuid.setClientVersion(entity.getClientVersion());
                frontEndsService.update(clientByUuid);
                return Response.status(Response.Status.CREATED).build();
            } else if (clientByWorkstationName == null || clientByUuid == null) {
                Response.ResponseBuilder responseBuilder = Response.status(Response.Status.NOT_ACCEPTABLE);
                String message = String.format("Can't register workstation: workstationName %s found, uuid %s found",
                        clientByWorkstationName == null ? "NOT" : "",
                        clientByUuid == null ? "NOT" : "");
                responseBuilder.entity(new ErrorBean(message));
                log.error(message);
                return responseBuilder.build();
            } else {
                return Response.status(Response.Status.ACCEPTED).build();
            }
        }
    }

}
