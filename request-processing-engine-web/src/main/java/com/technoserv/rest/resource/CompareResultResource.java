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
import java.util.List;

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

    private final ObjectMapper objectMapper = new ObjectMapper();

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