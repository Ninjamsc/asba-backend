package com.technoserv.rest.resources;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.jaxrs.annotation.JacksonFeatures;
import com.technoserv.db.model.objectmodel.Person;
import com.technoserv.db.model.objectmodel.Request;
import com.technoserv.db.service.objectmodel.api.PersonService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by sergey on 23.11.2016.
 */
@Component
@Path("/db/rest/IIN")
@Api(value = "Person Rest Api")
public class PersonResource {

    @Autowired
    private PersonService personService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @JacksonFeatures(serializationEnable = {SerializationFeature.INDENT_OUTPUT})
    @Path("/{iin}")
    public PersonResponse history(@PathParam("iin") Long id) {
        PersonResponse response = new PersonResponse();
        Person person = personService.history(id);
        if (person == null) {
            return null;
        }
        //response.setTimestamp(); TODO ...
        response.setIin(person.getId());

        for (Request request : person.getDossier()) {
            HistoryRequestResponse requestResponse = new HistoryRequestResponse();
            requestResponse.setWfmid(request.getId());
            requestResponse.setUsername(request.getLogin());
            requestResponse.setTimestamp(request.getTimestamp());
            requestResponse.setPreviewCamURL(request.getCameraDocument() != null ?
                    request.getCameraDocument().getOrigImageURL() : "");
            requestResponse.setFullframeCamURL(request.getCameraDocument() != null ?
                    request.getCameraDocument().getFaceSquare() : "");
            requestResponse.setPreviewScanURL(request.getScannedDocument() != null ?
                    request.getScannedDocument().getOrigImageURL() : "");
            requestResponse.setFullframeScanURL(request.getScannedDocument() != null ?
                    request.getScannedDocument().getFaceSquare() : "");
            response.getDossier().add(requestResponse);
        }
        return response;
    }

    public static class HistoryRequestResponse {
        @JsonProperty("_comment")
        private String comment;
        private String username;
        private Long wfmid;
        private Timestamp timestamp;
        private String previewCamURL;
        private String fullframeCamURL;
        private String previewScanURL;
        private String fullframeScanURL;

        public String getComment() {
            return comment;
        }

        public void setComment(String comment) {
            this.comment = comment;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public Long getWfmid() {
            return wfmid;
        }

        public void setWfmid(Long wfmid) {
            this.wfmid = wfmid;
        }

        public Timestamp getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(Timestamp timestamp) {
            this.timestamp = timestamp;
        }

        public String getPreviewCamURL() {
            return previewCamURL;
        }

        public void setPreviewCamURL(String previewCamURL) {
            this.previewCamURL = previewCamURL;
        }

        public String getFullframeCamURL() {
            return fullframeCamURL;
        }

        public void setFullframeCamURL(String fullframeCamURL) {
            this.fullframeCamURL = fullframeCamURL;
        }

        public String getPreviewScanURL() {
            return previewScanURL;
        }

        public void setPreviewScanURL(String previewScanURL) {
            this.previewScanURL = previewScanURL;
        }

        public String getFullframeScanURL() {
            return fullframeScanURL;
        }

        public void setFullframeScanURL(String fullframeScanURL) {
            this.fullframeScanURL = fullframeScanURL;
        }
    }

    public static class PersonResponse {

        @JsonProperty("_comment")
        private String comment;

        private Long iin;

        private Timestamp timestamp;

        private Collection<HistoryRequestResponse> dossier = new ArrayList<>();

        public String getComment() {
            return comment;
        }

        public void setComment(String comment) {
            this.comment = comment;
        }

        public Long getIin() {
            return iin;
        }

        public void setIin(Long iin) {
            this.iin = iin;
        }

        public Timestamp getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(Timestamp timestamp) {
            this.timestamp = timestamp;
        }

        public Collection<HistoryRequestResponse> getDossier() {
            return dossier;
        }

        public void setDossier(Collection<HistoryRequestResponse> dossier) {
            this.dossier = dossier;
        }
    }

}
