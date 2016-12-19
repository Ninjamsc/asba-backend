package com.technoserv.bio.kernel.rest.resource;

import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.jaxrs.annotation.JacksonFeatures;
import com.technoserv.db.model.objectmodel.*;
import com.technoserv.db.service.Service;
import com.technoserv.db.service.objectmodel.api.StopListService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * Created by sergey on 23.11.2016.
 */
@Component
@Path("/rest/stoplist")
@Api(value = "Stop List Rest API")
public class StopListsResource extends BaseResource<Long,StopList> {

    @Autowired
    private StopListService stopListService;
    @Override
    protected Service<Long, StopList> getBaseService() {
        return stopListService;
    }

    /**
     * Список всех стоп листов
     * @return Список всех стоп листов
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
     * Получить стоп лист по ID
     * @param id идентификатор.
     * @return заявка по ID
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
     * Список всех стоп листов
     * @return Список всех стоп листов
     */
    @GET
    @Path("/list/test/")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @JacksonFeatures( serializationEnable =  { SerializationFeature.INDENT_OUTPUT })
    public Response testList() {
        Collection<StopList> stopLists = new ArrayList<>();
        stopLists.add(createTestStopList(1));
        stopLists.add(createTestStopList(2));
        stopLists.add(createTestStopList(3));
        stopLists.add(createTestStopList(4));
        stopLists.add(createTestStopList(5));
        return Response.status(Response.Status.OK).header("Access-Control-Allow-Origin", "*")
//                .header("Access-Control-Allow-Headers", "origin, content-type, accept, authorization")
//                .header("Access-Control-Allow-Credentials", "true")
//                .header("Access-Control-Allow-Methods", "GET")
//                .header("Access-Control-Max-Age", "1209600")
                .entity(stopLists).build();
//        return stopLists;
    }
    /**
     * Получить стоп лист по ID
     * @param id идентификатор.
     * @return заявка по ID
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @JacksonFeatures( serializationEnable =  { SerializationFeature.INDENT_OUTPUT })
    @Path("/test/{id}")
    public Response getTest(@PathParam("id") Long id) {
        return Response.status(Response.Status.OK).header("Access-Control-Allow-Origin", "*")
//                .header("Access-Control-Allow-Headers", "origin, content-type, accept, authorization")
//                .header("Access-Control-Allow-Credentials", "true")
//                .header("Access-Control-Allow-Methods", "GET")
//                .header("Access-Control-Max-Age", "1209600")
                .entity(createTestStopList(1)).build();
    }

    private StopList createTestStopList(int i) {
        StopList stopList = new StopList();
        stopList.setId((long) i);
        stopList.setPersons(createTestPersons());
        stopList.setDescription("Тестовый стоп лист");
        stopList.setOwner(createTestOwners());
        stopList.setSimilarity(0.888d);
        stopList.setStopListName("Имя стоп листа");
        stopList.setType("Тип листа");
        stopList.setObjectDate(new Date());
        return stopList;
    }

    private List<Person> createTestPersons() {
        List<Person> people = new ArrayList<>();
        for (int i = 0; i < 15; i++) {
            people.add(createTestPerson(i));
        }
        return people;
    }

    private Person createTestPerson(int i) {
        Person person = new Person();
        person.setId((long) i);
        return person;
    }

    private List<Document> createTestOwners() {
        List<Document> documents = new ArrayList<>();
        for (int i = 0; i < 15; i++) {
            documents.add(createTestDocumnet(i));
        }
        return documents;
    }

    private Document createTestDocumnet(int i) {
        Document document = new Document();
        document.setObjectDate(new Date());
        document.setFaceSquare("http://www.sdorohov.ru/image-storage/rest/image/" + i + ".png");
        document.setOrigImageURL("http://www.sdorohov.ru/image-storage/rest/image/origin_" + i + ".png");
        document.setDescription("Описание");
        document.setBioTemplates(createTestBioTemplates(i));
        return document;
    }

    private List<BioTemplate> createTestBioTemplates(int i) {
        List<BioTemplate> bioTemplates = new ArrayList<>();
        BioTemplate bioTemplate = new BioTemplate();
        bioTemplate.setInsUser("user1");
        bioTemplate.setObjectDate(new Date());
        BioTemplateVersion bioTemplateVersion = new BioTemplateVersion();
        bioTemplateVersion.setDescription("Версия " + i);
        bioTemplateVersion.setObjectDate(new Date());
        bioTemplate.setBioTemplateVersion(bioTemplateVersion);
        bioTemplates.add(bioTemplate);
        return bioTemplates;
    }

    /**
     * Добавить стоп лист.
     * @param entity добавляемая конфигурация.
     * @return Идентификатор добавленной заявки.
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
     * Обновить стоп лист.
     * @param entity Обновляемая заявка.
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
     * удалить стоп лист.
     * @param id удаляемой заявки.
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
