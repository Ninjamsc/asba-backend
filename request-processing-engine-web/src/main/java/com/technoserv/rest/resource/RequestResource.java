package com.technoserv.rest.resource;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.jaxrs.annotation.JacksonFeatures;
import com.technoserv.db.model.objectmodel.Document;
import com.technoserv.db.model.objectmodel.DocumentType;
import com.technoserv.db.model.objectmodel.Person;
import com.technoserv.db.model.objectmodel.Request;
import com.technoserv.db.service.objectmodel.api.DocumentService;
import com.technoserv.db.service.objectmodel.api.DocumentTypeService;
import com.technoserv.db.service.objectmodel.api.PersonService;
import com.technoserv.db.service.objectmodel.api.RequestService;
import com.technoserv.rest.request.RequestSearchCriteria;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.sql.Timestamp;
import java.util.List;

/**
 * Created by sergey on 23.11.2016.
 */
@Component
@Path("")
@Api(value = "Request Rest API")
public class RequestResource {

    @Autowired
    private RequestService requestService;
    @Autowired
    private PersonService personService;

    public static class CreateOrderRequest {
        @JsonProperty("_comment")
        private String comment;
        private Long iin;
        private Long wfmId;
        private String username;

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

        public Long getWfmId() {
            return wfmId;
        }
        public void setWfmId(Long wfmId) {
            this.wfmId = wfmId;
        }

        public String getUsername() {
            return username;
        }
        public void setUsername(String username) {
            this.username = username;
        }
    }

    public static class GetRequestResponse {
        @JsonProperty("_comment")
        private String comment;
        private Long iin;
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

        public Long getIin() {
            return iin;
        }

        public void setIin(Long iin) {
            this.iin = iin;
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

    public static class AddScanPhotoRequest {
        @JsonProperty("_comment")
        private String comment;
        private Long iin;
        private String previewURL;
        private String fullframeURL;

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

        public String getPreviewURL() {
            return previewURL;
        }

        public void setPreviewURL(String previewURL) {
            this.previewURL = previewURL;
        }

        public String getFullframeURL() {
            return fullframeURL;
        }

        public void setFullframeURL(String fullframeURL) {
            this.fullframeURL = fullframeURL;
        }
    }

    public static class RequestSearchResult {

        private List<Request> result;

        private Integer total;

        public void setResult(List<Request> result) {
            this.result = result;
        }

        public List<Request> getResult() {
            return result;
        }

        public void setTotal(Integer total) {
            this.total = total;
        }

        public Integer getTotal() {
            return total;
        }
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @JacksonFeatures(serializationEnable = {SerializationFeature.INDENT_OUTPUT})
    @Path("/lists/rest/dossier/")
    public RequestSearchResult find(RequestSearchCriteria criteria) {
        List<Request> result = requestService.findByCriteria(criteria);
        RequestSearchResult searchResult = new RequestSearchResult();
        searchResult.setResult(result);
        searchResult.setTotal(requestService.countByCriteria(criteria));
        return searchResult;
    }

    /**
     * Получить заявку по номеру в Workflow (wfmId)
     * @param wfmId
     * @return
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @JacksonFeatures( serializationEnable =  { SerializationFeature.INDENT_OUTPUT })
    @Path("/lists/rest/dossier/{wfmId}")
    public GetRequestResponse get(@PathParam("wfmId") Long wfmId) {

        Request request = requestService.findById(wfmId);
        Person person = request.getPerson();

        GetRequestResponse getRequestResponse = new GetRequestResponse();
        getRequestResponse.setWfmid(wfmId);
        getRequestResponse.setIin(person.getId());
        getRequestResponse.setUsername(request.getLogin());
        getRequestResponse.setTimestamp(request.getTimestamp());
        getRequestResponse.setPreviewCamURL(request.getCameraDocument() != null ?
                request.getCameraDocument().getOrigImageURL() : "");
        getRequestResponse.setFullframeCamURL(request.getCameraDocument() != null ?
                request.getCameraDocument().getFaceSquare() : "");
        getRequestResponse.setPreviewScanURL(request.getScannedDocument() != null ?
                request.getScannedDocument().getOrigImageURL() : "");
        getRequestResponse.setFullframeScanURL(request.getScannedDocument() != null ?
                request.getScannedDocument().getFaceSquare() : "");
        return getRequestResponse;
    }

    /**
     * Создание заявки. Инициатор создания - Сервис обработки
     * @param createOrderRequest
     * @return
     */

    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @JacksonFeatures( serializationEnable =  { SerializationFeature.INDENT_OUTPUT })
    @Path("/db/rest/request/")
    public Long add(CreateOrderRequest createOrderRequest) {
        return requestService.createOrder(createOrderRequest.getIin(),
                createOrderRequest.getWfmId(),
                createOrderRequest.getUsername());
    }

    /**
     * Добавление к заявке фотографий, полученных со сканера
     * @param addScanPhotoRequest
     * @return
     */
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @JacksonFeatures( serializationEnable =  { SerializationFeature.INDENT_OUTPUT })
    @Path("/db/rest/request/{wfmId}/scan")
    public Response updateScan(@PathParam("wfmId") Long wfmId, AddScanPhotoRequest addScanPhotoRequest) {
        requestService.updateDocument(wfmId, addScanPhotoRequest.getPreviewURL(),
                addScanPhotoRequest.getFullframeURL(), DocumentType.Type.SCANNER);
        return Response.ok().build();
    }

    /**
     *  Добавление к заявке фотографий,полученных с веб-камеры
     */
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @JacksonFeatures( serializationEnable =  { SerializationFeature.INDENT_OUTPUT })
    @Path("/db/rest/request/{wfmId}/fullframe")
    public Response updateWebCamPicture(@PathParam("wfmId") Long wfmId, AddScanPhotoRequest addScanPhotoRequest) {
        requestService.updateDocument(wfmId, addScanPhotoRequest.getPreviewURL(),
                addScanPhotoRequest.getFullframeURL(), DocumentType.Type.WEB_CAM);
        return Response.ok().build();
    }
}