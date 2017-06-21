package com.technoserv.rest.resource;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.jaxrs.annotation.JacksonFeatures;
import com.technoserv.db.model.objectmodel.CompareResult;
import com.technoserv.db.model.objectmodel.Document;
import com.technoserv.db.model.objectmodel.Request;
import com.technoserv.db.service.objectmodel.api.CompareResultService;
import com.technoserv.db.service.objectmodel.api.DocumentService;
import com.technoserv.db.service.objectmodel.api.RequestService;
import com.technoserv.rest.model.CompareReport;
import com.technoserv.rest.model.CompareResponseDossierReport;
import com.technoserv.rest.model.CompareResponsePhotoObject;
import com.technoserv.utils.HttpUtils;
import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import java.io.IOException;
import java.util.*;

/**
 * Created by sergey on 27.11.2016.
 */
@Component
@Path("/rest/compare-result")
@Api(value = "Compare Result Rest API")
public class CompareResultResource {

    private static final Logger log = LoggerFactory.getLogger(CompareResultResource.class);

    @Autowired
    private CompareResultService compareResultService;

    @Autowired
    private RequestService requestService;

    @Autowired
    private DocumentService documentService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    void addToList(Collection list, String value){

        if (value != null){
            int lastSlashPosition = value.lastIndexOf("/");
            list.add(value.substring(lastSlashPosition+1,value.length()));
        }

    }

    @GET
    @Produces(HttpUtils.APPLICATION_JSON_UTF8)
    @Consumes(HttpUtils.APPLICATION_JSON_UTF8)
    @JacksonFeatures(serializationEnable = {SerializationFeature.INDENT_OUTPUT})
    @Path("/list") //id as wfm
    public Response findAll() throws IOException {
        List<CompareResult> compareResults = compareResultService.getAll();
        List<Request> req = requestService.getAll();
        List<CompareReport> compareReports = new ArrayList<>(compareResults.size());
        List<Document> documents = documentService.getAll();

        Set<String> finalResult = new TreeSet<>();

        for (CompareResult compareResult : compareResults) {
            CompareReport report = objectMapper.readValue(compareResult.getJson(), CompareReport.class);
            addToList(finalResult,report.getScannedPicture().getPictureURL()); //finalResult.add(report.getScannedPicture().getPictureURL());
            addToList(finalResult,report.getScannedPicture().getPreviewURL()); //finalResult.add(report.getScannedPicture().getPreviewURL());
            addToList(finalResult,report.getCameraPicture().getPictureURL()); //finalResult.add(report.getCameraPicture().getPictureURL());
            addToList(finalResult,report.getCameraPicture().getPreviewURL()); //finalResult.add(report.getCameraPicture().getPreviewURL());
            addToList(finalResult,report.getScannedPicturePreviewURL()); //finalResult.add(report.getScannedPicturePreviewURL());
            addToList(finalResult,report.getWebCamPicturePreviewURL()); //finalResult.add(report.getWebCamPicturePreviewURL());
            addToList(finalResult,report.getScannedPictureURL()); //finalResult.add(report.getScannedPictureURL());
            addToList(finalResult,report.getWebCamPictureURL()); //finalResult.add(report.getWebCamPictureURL());
            if(report.getOthernessPictures() != null) for (CompareResponsePhotoObject po : report.getOthernessPictures().getPhotos()){
                addToList(finalResult,po.getUrl()); //finalResult.add(po.getUrl());
            }
            if(report.getSimilarPictures() != null) for (CompareResponsePhotoObject po : report.getSimilarPictures().getPhotos()){
                addToList(finalResult,po.getUrl()); //finalResult.add(po.getUrl());
            }
        }

        for (Document d : documents) {
            addToList(finalResult,d.getFaceSquare()); //finalResult.add(d.getFaceSquare());
            addToList(finalResult,d.getOrigImageURL()); //finalResult.add(d.getOrigImageURL());
        }

 //       finalResult.add(String.valueOf(finalResult.size()));

        return (finalResult != null)
                ? Response.ok(finalResult).build()
                : Response.status(Status.NOT_FOUND).build();
    }

    @GET
    @Produces(HttpUtils.APPLICATION_JSON_UTF8)
    @Consumes(HttpUtils.APPLICATION_JSON_UTF8)
    @JacksonFeatures(serializationEnable = {SerializationFeature.INDENT_OUTPUT})
    @Path("/{id}") //id as wfm
    public Response find(@PathParam("id") Long id) throws IOException {
        log.debug("find id: {}", id);

        CompareResult compareResult = compareResultService.findById(id);
        Request req = requestService.findByOrderNumber(id);
        CompareReport report = objectMapper.readValue(compareResult.getJson(), CompareReport.class);

        if (req != null) {

            Document camera = req.getCameraDocument();
            if (camera != null && camera.getOrigImageURL() != null) {
                report.getCameraPicture().setPictureURL(camera.getOrigImageURL());
            }

            Document scan = req.getScannedDocument();
            if (scan != null && scan.getOrigImageURL() != null) {
                report.getScannedPicture().setPictureURL(scan.getOrigImageURL());
            }
        }

        return (compareResult != null)
                ? Response.ok(report).build()
                : Response.status(Status.NOT_FOUND).build();
    }

    @Path("/{id}")
    @POST
    @Produces(HttpUtils.APPLICATION_JSON_UTF8)
    @Consumes(HttpUtils.APPLICATION_JSON_UTF8)
    @JacksonFeatures(serializationEnable = {SerializationFeature.INDENT_OUTPUT})
    public Response save(@PathParam("id") Long id, String json) {
        log.debug("save id: {} json: {}", id, json);
        CompareResult result = compareResultService.findById(id);
        if (result == null) {
            result = new CompareResult();
            result.setId(id);
        }
        result.setJson(json);
        compareResultService.saveOrUpdate(result);
        return Response.ok().build();
    }

    @GET
    @Produces(HttpUtils.APPLICATION_JSON_UTF8)
    @Consumes(HttpUtils.APPLICATION_JSON_UTF8)
    @JacksonFeatures(serializationEnable = {SerializationFeature.INDENT_OUTPUT})
    public List<CompareResult> getAll() {
        log.debug("getAll");
        return compareResultService.getAll();
    }

}