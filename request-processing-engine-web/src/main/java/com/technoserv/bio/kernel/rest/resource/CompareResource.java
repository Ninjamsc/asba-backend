package com.technoserv.bio.kernel.rest.resource;

import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.jaxrs.annotation.JacksonFeatures;
import com.technoserv.db.service.objectmodel.api.BioTemplateService;
import com.technoserv.db.service.objectmodel.api.StopListService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

/**
 * Created by sergey on 27.11.2016.
 */
@Component
@Path("/compare/rest")
@Api(value = "Compare")
public class CompareResource implements InitializingBean {

    //Результат сравнения
    public static class CompareResult {
        //TODO описать ДТО результатов
    }

    public static class CompareRequest {
        //TODO ДТО входные параметры
    }


    //Пример инжекции сервисов
    @Autowired
    private BioTemplateService bioTemplateService;

    @Autowired
    private StopListService stopListService;


    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @JacksonFeatures( serializationEnable =  { SerializationFeature.INDENT_OUTPUT })
    @Path("/")
    private CompareResult compare(CompareRequest compareRequest) {


        return new CompareResult();//todo вернуть обюработаанные данные
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println("Иницаиализация сервиса сравнения");
        //Todo тут логика при старте приложенния
        //Фактически данный бин singleton и создаётся при старте приложения



        System.out.println("Конец иницаиализации сервиса сравнения");
    }
}
