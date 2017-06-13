package com.technoserv.rest.resource;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.jaxrs.annotation.JacksonFeatures;
import com.technoserv.db.model.objectmodel.CompareResult;
import com.technoserv.db.model.objectmodel.Document;
import com.technoserv.db.model.objectmodel.Request;
import com.technoserv.db.service.objectmodel.api.CompareResultService;
import com.technoserv.db.service.objectmodel.api.RequestService;
import com.technoserv.rest.model.CompareReport;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.List;

/**
 * Created by sergey on 27.11.2016.
 */
@Component
@Path("/rest/compare-result")
@Api(value = "Compare Result Rest API")
public class CompareResultResource {

    @Autowired
    private CompareResultService compareResultService;

    @Autowired
    private RequestService requestService;

    private ObjectMapper objectMapper = new ObjectMapper();

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @JacksonFeatures(serializationEnable = {SerializationFeature.INDENT_OUTPUT})
    @Path("/{id}") //id as wfm
    public Response find(@PathParam("id") Long id) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        //objectMapper.setDateFormat(DATE_FORMAT);

        CompareResult compareResult = compareResultService.findById(id);
        Request req = requestService.findByOrderNumber(id);
        CompareReport report = objectMapper.readValue(compareResult.getJson(), CompareReport.class);
        if (req != null) {
            Document camera = req.getCameraDocument();
            if (camera != null && camera.getOrigImageURL() != null)
                report.getCameraPicture().setPictureURL(camera.getOrigImageURL());
            Document scan = req.getScannedDocument();
            if (scan != null && scan.getOrigImageURL() != null)
                report.getScannedPicture().setPictureURL(scan.getOrigImageURL());

        }
//        return compareResult !=null ? Response.ok(objectMapper.readValue(compareResult.getJson(),JsonNode.class)).header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON+"; charset=UTF-8").build()
//        : Response.status(Response.Status.NOT_FOUND).build();
        return compareResult != null ? Response.ok(report).header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON + "; charset=UTF-8").build()
                : Response.status(Response.Status.NOT_FOUND).build();
    }

    @Path("/{id}")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @JacksonFeatures(serializationEnable = {SerializationFeature.INDENT_OUTPUT})
    public Response save(@PathParam("id") Long id, String json) {
        CompareResult result = compareResultService.findById(id);
        if (result == null) {
            result = new CompareResult();
            result.setId(id);
        }
        result.setJson(json);
        compareResultService.saveOrUpdate(result);
        return Response.status(Response.Status.OK).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @JacksonFeatures(serializationEnable = {SerializationFeature.INDENT_OUTPUT})
    public List<CompareResult> getAll() {
        return compareResultService.getAll();
    }

}