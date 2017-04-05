package com.technoserv.bio.kernel;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.technoserv.rest.client.CompareServiceRestClient;
import com.technoserv.rest.client.PhotoAnalyzerServiceRestClient;
import com.technoserv.db.model.configuration.SystemSettingsType;
import com.technoserv.db.model.objectmodel.*;
import com.technoserv.db.service.configuration.impl.SystemSettingsBean;
import com.technoserv.db.service.objectmodel.api.*;
import com.technoserv.rest.client.TemplateBuilderServiceRestClient;
import com.technoserv.rest.exception.RestClientException;
import com.technoserv.rest.request.CompareServiceRequest;
import com.technoserv.rest.client.PhotoPersistServiceRestClient;
import com.technoserv.rest.request.PhotoTemplate;
import com.technoserv.utils.JsonUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;

/**
 * Created by Adrey on 22.11.2016.
 */
//@Service(name = "requestProcessor")
public class RequestProcessor {

    private static final Log log = LogFactory.getLog(RequestProcessor.class);

    @Autowired
    private RequestService requestService;

    @Autowired
    private TemplateBuilderServiceRestClient templateBuilderServiceRestClient;

    @Autowired
    private PhotoPersistServiceRestClient photoPersistServiceRestClient;

    @Autowired
    private PhotoAnalyzerServiceRestClient photoAnalyzerServiceRestClient;

    @Autowired
    private CompareResultService compareResultService;

    @Autowired
    private CompareServiceRestClient сompareServiceRestClient;

    @Autowired
    private JmsTemplate jmsTemplate;

    @Autowired
    private BioTemplateService bioTemplateService;

    @Autowired
    private BioTemplateVersionService bioTemplateVersionService;

    @Autowired
    private BioTemplateTypeService bioTemplateTypeService;

    @Autowired
    private SystemSettingsBean systemSettingsBean;

    private ObjectMapper objectMapper = new ObjectMapper();

    private SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");

    public RequestProcessor() {}


    public Collection<Request> findRequestForProcessing() {
        return requestService.findNotProcessed();
    }

    private void writeLog(String message) {
        System.out.println(message);
        log.info(message);
    }

    public void process() {
        writeLog("RequestProcessor process order");
        Collection<Request> requestList = findRequestForProcessing();
        for (Request request : requestList) {
            try {
                writeLog("Update request status to IN_PROCESS for id = '" + request.getId() + "'");
                updateRequestStatus(request, Request.Status.IN_PROCESS);
                writeLog("Download photo");
                byte[] scannedPhoto = photoPersistServiceRestClient.getPhoto(request.getScannedDocument().getFaceSquare());
                writeLog("Downloaded scannedPhoto '" + scannedPhoto + "'");
                byte[] webCamPhoto = photoPersistServiceRestClient.getPhoto(request.getCameraDocument().getFaceSquare());
                writeLog("Downloaded webCamPhoto '" + webCamPhoto + "'");
                // шаг 4 построение шаблона
                // компонент 7. Сервис построения шаблонов(биометрическое ядро)
                writeLog("scannedPhoto -> scannedTemplate");
                PhotoTemplate scannedTemplate = templateBuilderServiceRestClient.getPhotoTemplate(scannedPhoto);
                writeLog("scannedPhoto -> scannedTemplate " + scannedTemplate);
                writeLog("saving scannedTemplate");
                addBioTemplateToDocument(request, request.getScannedDocument(), scannedTemplate);
                writeLog("saving scannedTemplate - done");

                writeLog("webCamPhoto -> webCamTemplate");
                PhotoTemplate webCamTemplate = templateBuilderServiceRestClient.getPhotoTemplate(webCamPhoto);
                writeLog("webCamPhoto -> webCamTemplate " + webCamTemplate);
                writeLog("saving webCamTemplate");
                addBioTemplateToDocument(request, request.getCameraDocument(), webCamTemplate);
                writeLog("saving webCamTemplate - done");
                //шаг 5 Построение фильтров
                // компонент 8 Сервис анализа изображений
                writeLog("analyze scannedPhoto " + scannedPhoto);
//                photoAnalyzerServiceRestClient.analyzePhoto(scannedPhoto);//todo uncomment me
                writeLog("analyze scannedPhoto Done");

                writeLog("analyze webCamPhoto");
//                photoAnalyzerServiceRestClient.analyzePhoto(webCamPhoto);//todo uncomment me
                writeLog("analyze webCamPhoto Done");
                //шаг в 6 Сравнение со списками
                // компонент 9 Сервис сравнения
                writeLog("compareServiceRequest - scannedTemplate +  webCamTemplate");
                CompareServiceRequest compareServiceRequest = new CompareServiceRequest();

                compareServiceRequest.setIin(request.getPerson().getId());
                compareServiceRequest.setWfmId(request.getId());
                compareServiceRequest.setWebFullFrameURL(request.getCameraDocument().getOrigImageURL());
                compareServiceRequest.setWebPreviewURL(request.getCameraDocument().getFaceSquare());

                compareServiceRequest.setScanFullFrameURL(request.getScannedDocument().getOrigImageURL());
                compareServiceRequest.setScanPreviewURL(request.getScannedDocument().getFaceSquare());

                if(scannedTemplate!=null) {
                    compareServiceRequest.setScanTemplate(scannedTemplate.template);
                } else {
                    compareServiceRequest.setScanTemplate(new double[]{});
                }
                if(webCamTemplate!=null) {
                    compareServiceRequest.setWebTemplate(webCamTemplate.template);
                } else {
                    compareServiceRequest.setWebTemplate(new double[]{});
                }
                String compareResult = сompareServiceRestClient.compare(compareServiceRequest);
                String jsonResult = enrich(compareResult, request);
                compareResultService.saveOrUpdate(new CompareResult(request.getId(), jsonResult));
                if(isNeedToSentToWorkflowQueue()) {
                    jmsTemplate.convertAndSend(jsonResult);
                }
                writeLog("Send compareServiceRequest Done");
                writeLog("Update request status to SUCCESS for id = '" + request.getId() + "'");
                updateRequestStatus(request, Request.Status.SUCCESS);
                writeLog("Update request status to SUCCESS for id = '" + request.getId() + "' Done");
            } catch (RestClientException ex) {
                ex.printStackTrace();
                writeLog("Update request status to FAILED for id = '" + request.getId() + "'");
                updateRequestStatus(request, Request.Status.FAILED);
                writeLog("Update request status to FAILED for id = '" + request.getId() + "' Done");
                if(isNeedToSentToWorkflowQueue()) {
                    jmsTemplate.convertAndSend(ex.toJSON());
                }
            } catch (Throwable e) {
                e.printStackTrace();
                writeLog("Update request status to SAVED for id = '" + request.getId() + " for retry");
                updateRequestStatus(request, Request.Status.SAVED);//выставляем статус для ретрая
                writeLog("Update request status to SAVED for id = '" + request.getId() + " Done");
            }
        }
    }

    private boolean isNeedToSentToWorkflowQueue () {
        return "true".equalsIgnoreCase(systemSettingsBean.get(SystemSettingsType.SEND_TO_WORKFLOW_QUEUE));
    }

    protected String enrich(String compareResult, Request request) throws Exception {
        writeLog("compareServiceRequest - scannedTemplate +  webCamTemplate Done: " + new String(compareResult.getBytes()));
        JsonNode  result = objectMapper.readValue(compareResult, JsonNode.class);
        Long iin = request.getPerson().getId();
        Long wfm = request.getId();
        ((ObjectNode) result).put("wfNumber", wfm);
        ((ObjectNode) result).put("IIN", iin);
        ((ObjectNode) result).put("username", request.getLogin());
        String timestamp = DATE_FORMAT.format(request.getTimestamp());
        ((ObjectNode) result).put("timestamp", timestamp);
        ((ObjectNode) result).put("created-at", DATE_FORMAT.format(new Date()));
/*
                ((ObjectNode) result).put("scannedPictureURL", request.getScannedDocument().getFaceSquare());
                ((ObjectNode) result).put("scannedPicturePreviewURL", request.getScannedDocument().getOrigImageURL());
                ((ObjectNode) result).put("webCamPictureURL", request.getCameraDocument().getFaceSquare());
                ((ObjectNode) result).put("webCamPicturePreviewURL", request.getCameraDocument().getOrigImageURL());
*/
        ((ObjectNode) result).put("scannedPictureURL", request.getScannedDocument().getOrigImageURL());
        ((ObjectNode) result).put("scannedPicturePreviewURL", request.getScannedDocument().getFaceSquare());
        ((ObjectNode) result).put("webCamPictureURL", request.getCameraDocument().getOrigImageURL());
        ((ObjectNode) result).put("webCamPicturePreviewURL", request.getCameraDocument().getFaceSquare());

        writeLog("Send compareServiceRequest");
        String jsonResult = result.toString();
        writeLog(jsonResult);

        return jsonResult;
    }

    private void addBioTemplateToDocument(Request request, Document document, PhotoTemplate scannedTemplate) throws IOException {
        if(scannedTemplate==null) {
            return;
        }
        BioTemplate bioTemplate = new BioTemplate();
        bioTemplate.setInsUser(request.getInsUser());
        bioTemplate.setTemplateVector(JsonUtils.serializeJson(scannedTemplate.template));
        bioTemplate.setDocument(document);
        BioTemplateVersion bioTemplateVersion = bioTemplateVersionService.findById((long) scannedTemplate.version);
        if (bioTemplateVersion == null) {
            bioTemplateVersion = new BioTemplateVersion();
            bioTemplateVersion.setId((long) scannedTemplate.version);
            bioTemplateVersion.setObjectDate(new Date());
            bioTemplateVersion.setDescription("Версия " + scannedTemplate.version);
            bioTemplateVersionService.saveOrUpdate(bioTemplateVersion);
        }
        bioTemplate.setBioTemplateVersion(bioTemplateVersion);
        BioTemplateType bioTemplateType = bioTemplateTypeService.findById((long) scannedTemplate.type);
        if (bioTemplateType == null) {
            bioTemplateType = new BioTemplateType();
            bioTemplateType.setId((long) scannedTemplate.type);
            bioTemplateType.setDescription("Новый тип " + scannedTemplate.type);
            bioTemplateTypeService.saveOrUpdate(bioTemplateType);
        }
        bioTemplate.setBioTemplateType(bioTemplateType);

        bioTemplateService.saveOrUpdate(bioTemplate);
    }

    private void updateRequestStatus(Request request, Request.Status status) {
        request.setStatus(status);
        requestService.saveOrUpdate(request);
    }

    public void setRequestService(RequestService requestService) {
        this.requestService = requestService;
    }

    public void setTemplateBuilderServiceRestClient(TemplateBuilderServiceRestClient templateBuilderServiceRestClient) {
        this.templateBuilderServiceRestClient = templateBuilderServiceRestClient;
    }

    public void setPhotoPersistServiceRestClient(PhotoPersistServiceRestClient photoPersistServiceRestClient) {
        this.photoPersistServiceRestClient = photoPersistServiceRestClient;
    }

    public void setPhotoAnalyzerServiceRestClient(PhotoAnalyzerServiceRestClient photoAnalyzerServiceRestClient) {
        this.photoAnalyzerServiceRestClient = photoAnalyzerServiceRestClient;
    }

    public void setСompareServiceRestClient(CompareServiceRestClient сompareServiceRestClient) {
        this.сompareServiceRestClient = сompareServiceRestClient;
    }

    public void setJmsTemplate(JmsTemplate jmsTemplate) {
        this.jmsTemplate = jmsTemplate;
    }

    public void setBioTemplateService(BioTemplateService bioTemplateService) {
        this.bioTemplateService = bioTemplateService;
    }

    public void setBioTemplateVersionService(BioTemplateVersionService bioTemplateVersionService) {
        this.bioTemplateVersionService = bioTemplateVersionService;
    }
}
