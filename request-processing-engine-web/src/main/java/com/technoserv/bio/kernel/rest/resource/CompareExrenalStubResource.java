package com.technoserv.bio.kernel.rest.resource;

import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.jaxrs.annotation.JacksonFeatures;
import com.technoserv.bio.kernel.rest.UtilTest;
import com.technoserv.bio.kernel.rest.request.CompareServiceRequest;
import com.technoserv.db.service.objectmodel.api.CompareResultService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

/**
 * Created by sergey on 27.11.2016.
 */
@Component
@Path("/rest/compare-stub")
@Api(value = "CompareStub Rest API")
public class CompareExrenalStubResource {

    public static final Long ID = 1l;

    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @JacksonFeatures( serializationEnable =  { SerializationFeature.INDENT_OUTPUT })
    @Path("/template")
    public String find(CompareServiceRequest request) {
        String response = "{\n" +
                "  \"rules\":[\n" +
                "    {\n" +
                "      \"ruleId\":\"4.2.1\",\n" +
                "      \"ruleDescription\" :\"Фотография, прикрепленная к заявке, существенно отличается от других фотографий заемщика, имеющихся в базе\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"ruleId\":\"4.2.2\",\n" +
                "      \"ruleDescription\" :\"Фотография, прикрепленная к заявке, идентична имеющейся в базе\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"ruleId\":\"4.2.3\",\n" +
                "      \"ruleDescription\" :\"Возможно соответствие с клиентом из банковского СТОП-ЛИСТА\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"ruleId\":\"4.2.4\",\n" +
                "      \"ruleDescription\" :\"Возможно соответствие с клиентом из общего СТОП-ЛИСТА\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"ruleId\":\"4.2.5\",\n" +
                "      \"ruleDescription\" :\"Возможно несоответствие фотографии в паспорте и фотографии, прикрепленной к заявке\"\n" +
                "    }\n" +
                "\n" +
                "  ],\n" +
                "  \"scannedPicture\":{\"pictureURL\":\"<url>\",\"previewURL\":\"<url>\",\"blackLists\":[{\"listId\":5,\"listName\":\"People\",\"photo\":[{\"url\":\"http://192.168.167.211:9080/storage/rest/image/78c3e345-490f-4320-803e-f5803fb82015.jpg\",\"similarity\":0.02994438539996796},{\"url\":\"http://192.168.167.211:9080/storage/rest/image/d63fe422-c073-4fe5-961e-83a2770b7f63.jpg\",\"similarity\":0.139474854710958},{\"url\":\"http://192.168.167.211:9080/storage/rest/image/f12ae341-9519-4c79-8774-72b0f0c11184.jpg\",\"similarity\":0.139474854710958}],\"similarity\":0.851},{\"listId\":5,\"listName\":\"People\",\"photo\":[{\"url\":\"http://192.168.167.211:9080/storage/rest/image/78c3e345-490f-4320-803e-f5803fb82015.jpg\",\"similarity\":0.02994438539996796},{\"url\":\"http://192.168.167.211:9080/storage/rest/image/d63fe422-c073-4fe5-961e-83a2770b7f63.jpg\",\"similarity\":0.139474854710958},{\"url\":\"http://192.168.167.211:9080/storage/rest/image/f12ae341-9519-4c79-8774-72b0f0c11184.jpg\",\"similarity\":0.139474854710958}],\"similarity\":0.851},{\"listId\":5,\"listName\":\"People\",\"photo\":[{\"url\":\"http://192.168.167.211:9080/storage/rest/image/78c3e345-490f-4320-803e-f5803fb82015.jpg\",\"similarity\":0.02994438539996796},{\"url\":\"http://192.168.167.211:9080/storage/rest/image/d63fe422-c073-4fe5-961e-83a2770b7f63.jpg\",\"similarity\":0.139474854710958},{\"url\":\"http://192.168.167.211:9080/storage/rest/image/f12ae341-9519-4c79-8774-72b0f0c11184.jpg\",\"similarity\":0.139474854710958}],\"similarity\":0.851}]},\n" +
                "  \"cameraPicture\":{\"pictureURL\":\"<url>\",\"previewURL\":\"<url>\",\"blackLists\":[{\"listId\":5,\"listName\":\"People\",\"photo\":[{\"url\":\"http://192.168.167.211:9080/storage/rest/image/78c3e345-490f-4320-803e-f5803fb82015.jpg\",\"similarity\":0.02994438539996796},{\"url\":\"http://192.168.167.211:9080/storage/rest/image/d63fe422-c073-4fe5-961e-83a2770b7f63.jpg\",\"similarity\":0.139474854710958},{\"url\":\"http://192.168.167.211:9080/storage/rest/image/f12ae341-9519-4c79-8774-72b0f0c11184.jpg\",\"similarity\":0.139474854710958}],\"similarity\":0.851},{\"listId\":5,\"listName\":\"People\",\"photo\":[{\"url\":\"http://192.168.167.211:9080/storage/rest/image/78c3e345-490f-4320-803e-f5803fb82015.jpg\",\"similarity\":0.02994438539996796},{\"url\":\"http://192.168.167.211:9080/storage/rest/image/d63fe422-c073-4fe5-961e-83a2770b7f63.jpg\",\"similarity\":0.139474854710958},{\"url\":\"http://192.168.167.211:9080/storage/rest/image/f12ae341-9519-4c79-8774-72b0f0c11184.jpg\",\"similarity\":0.139474854710958}],\"similarity\":0.851},{\"listId\":5,\"listName\":\"People\",\"photo\":[{\"url\":\"http://192.168.167.211:9080/storage/rest/image/78c3e345-490f-4320-803e-f5803fb82015.jpg\",\"similarity\":0.02994438539996796},{\"url\":\"http://192.168.167.211:9080/storage/rest/image/d63fe422-c073-4fe5-961e-83a2770b7f63.jpg\",\"similarity\":0.139474854710958},{\"url\":\"http://192.168.167.211:9080/storage/rest/image/f12ae341-9519-4c79-8774-72b0f0c11184.jpg\",\"similarity\":0.139474854710958}],\"similarity\":0.851}]}\n" +
                "}";
        return response;
    }
}