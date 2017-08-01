package com.technoserv.rest.resource;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.jaxrs.annotation.JacksonFeatures;
import com.technoserv.db.model.objectmodel.BioTemplate;
import com.technoserv.db.model.objectmodel.CompareResult;
import com.technoserv.db.model.objectmodel.Document;
import com.technoserv.db.model.objectmodel.Request;
import com.technoserv.db.service.objectmodel.api.*;
import com.technoserv.rest.model.CompareReport;
import com.technoserv.rest.model.CompareResponseDossierReport;
import com.technoserv.rest.model.CompareResponsePhotoObject;
import com.technoserv.rest.request.RequestSearchCriteria;
import com.technoserv.utils.HttpUtils;
import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import java.io.File;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

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

    @Autowired
    private BioTemplateService bioTemplateService;

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
    @Path("/list")
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

        if (compareResult == null && req == null){
            RestTemplate restTemplate = new RestTemplate();
            CompareReport report = restTemplate.getForEntity("http://localhost:9080/client/rest/isenqueued/"+id,CompareReport.class).getBody();
            return (report != null)
                    ? Response.ok(report).build()
                    : Response.status(Status.NOT_FOUND).build();
        }

        //В случае когда запрос в БД есть а результат не SUCCESS тоже надо показывать результат
        if(compareResult == null && req != null){
            Map<String,Object> report = new HashMap<>();
            List<Map<String,Object>> rules = new ArrayList<>();
            Map<String,Object> rule = new HashMap<>();
            report.put("wfNumber", null);

            if (req.getStatus().equals(Request.Status.NO_VECTOR)){
                rule.put("ruleId","4.2.9");
                rule.put("ruleName","Изображения недостаточного качества, просьба повторно отправить заявку "+req.getId());
            } else {
                rule.put("ruleId","4.2.8");
                rule.put("ruleName","Заявка "+req.getId()+" " + (req.getStatus().equals(Request.Status.FAILED) ? "не обработана! Системная ошибка!" : "ожидает полного набора данных от АРМ"));
            }
            rule.put("photo",null);
            report.put("timestamp",req.getTimestamp().toLocalDateTime().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss")));
            report.put("created-at",req.getTimestamp().toLocalDateTime().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss")));
            report.put("picSimilarity",null);
            report.put("picSimilarityThreshold",null);
            report.put("username",null);
            report.put("iin",null);

            report.put("scannedPicturePreviewURL",null);
            report.put("webCamPicturePreviewURL",null);
            report.put("scannedPictureURL",null);
            report.put("webCamPictureURL",null);
            report.put("IIN",null);

            report.put("scannedPicture",new HashMap<>());
            report.put("cameraPicture",new HashMap<>());
            report.put("othernessPictures",new HashMap<>());
            report.put("similarPictures",new HashMap<>());

            rules.add(rule);
            report.put("rules",rules);
            return (report != null)
                    ? Response.ok(report).build()
                    : Response.status(Status.NOT_FOUND).build();
        }

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

        //Убирать дубликаты из report.getOthernessPictures().getPhotos() и report.getSimilarPictures().getPhotos()
        if (report.getOthernessPictures() != null) {
            report.getOthernessPictures().setPhotos(
                    report.getOthernessPictures().getPhotos().stream().distinct().limit(10).collect(Collectors.toList())
            );
        }
        if(report.getSimilarPictures() != null) {
            report.getSimilarPictures().setPhotos(
                    report.getSimilarPictures().getPhotos().stream().distinct().limit(10).collect(Collectors.toList())
            );
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
        try {
            CompareReport report = objectMapper.readValue(json, CompareReport.class);
            result.setSimilarity(report.getPicSimilarity());
        } catch (IOException e) {
            log.debug("Wrong json");
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