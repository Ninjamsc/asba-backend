package com.technoserv.rest.resources;

import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.jaxrs.annotation.JacksonFeatures;
import com.technoserv.db.model.objectmodel.Document;
import com.technoserv.db.model.objectmodel.Person;
import com.technoserv.db.model.objectmodel.Request;
import com.technoserv.db.model.objectmodel.StopList;
import com.technoserv.db.service.objectmodel.api.StopListService;
import io.swagger.annotations.Api;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;

/**
 * Created by sergey on 23.11.2016.
 */
@Component
@Path("")
@Api(value = "Stop List Content Rest API")
public class StopListContentResource {

    @Autowired
    private StopListService stopListService;
    @Autowired
    private ImportImagesService importImagesService;

    @Path("/lists/rest/stoplist/{ID}/entry")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @JacksonFeatures(serializationEnable = {SerializationFeature.INDENT_OUTPUT})
    public StopListResponse get(@PathParam("ID") Long id) {
        StopList stopList = stopListService.findById(id);

        StopListResponse stopListResponse = new StopListResponse();
        stopListResponse.setSimilarity(stopList.getSimilarity());
        stopListResponse.setName(stopList.getStopListName());
        stopListResponse.setDescription(stopList.getDescription());
        stopListResponse.setType(stopList.getType());
        stopListResponse.setRequestResponses(new ArrayList<>());

        return stopListResponse;
    }

    @Path("/lists/rest/stoplist/{ID}/entry")
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @JacksonFeatures(serializationEnable = {SerializationFeature.INDENT_OUTPUT})
    public void add(@PathParam("ID") Long id, Document document) {
        StopList stopList = stopListService.findById(id);
        if (stopList != null) {
            stopList.getOwner().add(document);
            stopListService.saveOrUpdate(stopList);
        }
    }

    @Path("/partners/rest/stoplist/{listId}/entry/{itemId}")
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @JacksonFeatures(serializationEnable = {SerializationFeature.INDENT_OUTPUT})
    public void delete(@PathParam("listId") Long listId, @PathParam("itemId") Long itemId) {

        //todo write native sql

        StopList stopList = stopListService.findById(listId);
        if (stopList != null) {
            Document delDocument = null;
            for (Document document : stopList.getOwner()) {
                if (document.getId().equals(itemId)) {
                    delDocument = document;
                }
            }
            if (delDocument != null) {
                stopList.getOwner().remove(delDocument);
                stopListService.saveOrUpdate(stopList);
            }
        }

    }

    @POST
    @Path("/stop-list/{id}/upload")  //Your Path or URL to call this service
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public Response uploadZipFile(@PathParam("id") Long stopListId,
                                  @FormDataParam("file") InputStream uploadedInputStream,
                                  @FormDataParam("file") FormDataContentDisposition fileDetail) {
        Map<String, Map<String, Boolean>> report =
                importImagesService.importImageZip(stopListId, uploadedInputStream, fileDetail.getFileName());

        return Response.status(200).entity(report).header("Access-Control-Allow-Origin", "*").build();
    }

    @POST
    @Path("/stop-list/{id}/upload-photo")  //Your Path or URL to call this service
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public Response uploadFile(@PathParam("id") Long stopListId,
                               @FormDataParam("file") InputStream uploadedInputStream,
                               @FormDataParam("file") FormDataContentDisposition fileDetail) {
        importImagesService.importImage(stopListId, uploadedInputStream, fileDetail.getFileName());

        return Response.status(Response.Status.OK)
                .header("Access-Control-Allow-Origin", "*")
                .build();
    }

    private static class StopListResponse {
        //TODO ..
        private String type;
        //TODO ..
        private String description;
        //TODO ..
        private String name;
        //TODO ..
        private Double similarity;
        private Collection<PersonResource.HistoryRequestResponse> requestResponses = Collections.emptyList();

        public Collection<PersonResource.HistoryRequestResponse> getRequestResponses() {
            return requestResponses;
        }

        public void setRequestResponses(Collection<PersonResource.HistoryRequestResponse> requestResponses) {
            this.requestResponses = requestResponses;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Double getSimilarity() {
            return similarity;
        }

        public void setSimilarity(Double similarity) {
            this.similarity = similarity;
        }
    }

    private PersonResource.PersonResponse toResponse(Person person) {
        PersonResource.PersonResponse response = new PersonResource.PersonResponse();
        //response.setTimestamp(); TODO ...
        response.setIin(person.getId());

        for (Request request : person.getDossier()) {
            PersonResource.HistoryRequestResponse requestResponse = new PersonResource.HistoryRequestResponse();
            requestResponse.setWfmid(request.getId());
            requestResponse.setUsername(request.getLogin());
            requestResponse.setTimestamp(request.getTimestamp());

            requestResponse.setPreviewCamURL(
                    request.getCameraDocument() != null ? request.getCameraDocument().getOrigImageURL() : ""
            );

            requestResponse.setFullframeCamURL(
                    request.getCameraDocument() != null ? request.getCameraDocument().getFaceSquare() : ""
            );

            requestResponse.setPreviewScanURL(
                    request.getScannedDocument() != null ? request.getScannedDocument().getOrigImageURL() : ""
            );

            requestResponse.setFullframeScanURL(
                    request.getScannedDocument() != null ? request.getScannedDocument().getFaceSquare() : ""
            );

            response.getDossier().add(requestResponse);
        }
        return response;
    }
}
