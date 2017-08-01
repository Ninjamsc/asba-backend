package com.technoserv.bio.kernel;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.base.Strings;
import com.technoserv.db.model.configuration.SystemSettingsType;
import com.technoserv.db.model.objectmodel.*;
import com.technoserv.db.service.configuration.impl.SystemSettingsBean;
import com.technoserv.db.service.objectmodel.api.*;
import com.technoserv.queue.Queues;
import com.technoserv.queue.RequestRetry;
import com.technoserv.rest.client.CompareServiceRestClient;
import com.technoserv.rest.client.PhotoAnalyzerServiceRestClient;
import com.technoserv.rest.client.PhotoPersistServiceRestClient;
import com.technoserv.rest.client.TemplateBuilderServiceRestClient;
import com.technoserv.rest.exception.RestClientException;
import com.technoserv.rest.model.CompareReport;
import com.technoserv.rest.request.CompareServiceRequest;
import com.technoserv.rest.request.PhotoTemplate;
import com.technoserv.utils.JsonUtils;
import org.apache.commons.lang.NullArgumentException;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Adrey on 22.11.2016.
 */
//@Service(name = "requestProcessor")
public class RequestProcessor {

    private static final Logger log = LoggerFactory.getLogger(RequestProcessor.class);

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
    private CompareServiceRestClient compareServiceRestClient;

    @Autowired
    @Qualifier(Queues.WORKFLOW_QUEUE_BEAN)
    private JmsTemplate jmsTemplate;

    @Autowired
    @Qualifier(Queues.RETRY_REQUEST_QUEUE_BEAN)
    private JmsTemplate retryRequestTemplate;

    @Autowired
    private BioTemplateService bioTemplateService;

    @Autowired
    private BioTemplateVersionService bioTemplateVersionService;

    @Autowired
    private BioTemplateTypeService bioTemplateTypeService;

    @Autowired
    private SystemSettingsBean systemSettingsBean;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");

    private static final int MAX_NUMBER_OF_REQUESTS_TO_RETRY_IN_ONE_TIME = 5;

    private static final int MAX_NUMBER_OF_RETRIES = 5;

    private final Object processLock = new Object();

    public RequestProcessor() {
    }

    public List<Request> findRequestForProcessing() {
        return requestService.findNotProcessed();
    }

    public void process() {
        synchronized (processLock) {
            List<Request> requestList = findRequestForProcessing();
            log.info("Number of requests to process: {}", requestList.size());
            for (Request request : requestList) {
                processRequest(request, 0);
            }
            log.debug("Requests cycle complete.");

//            retryFailedRequests();
            log.debug("Failed requests queue processed.");
        }
    }

    private PhotoTemplate preparePhotoTemplate(Document doc){
        PhotoTemplate template = null;
        byte[] photo = photoPersistServiceRestClient.getPhoto(doc.getFaceSquare());
        log.debug("Downloaded scannedPhoto size: {} URL: {}", photo.length, doc.getFaceSquare());
        try {
            template = templateBuilderServiceRestClient.getPhotoTemplate(photo);
        } catch (Exception e){
            log.debug("Cached exception on preview: "+e.getMessage());
            photo = photoPersistServiceRestClient.getPhoto(doc.getOrigImageURL());
            try {
                template = templateBuilderServiceRestClient.getPhotoTemplate(photo);
            } catch (Exception e2){
                log.debug("Cached exception on fullframe: "+e.getMessage());
            }
        }
        return template;
    }

    private void processRequest(Request request, int previousRetryCount) {
        try {
            log.debug("Process request: {}", request);
            updateRequestStatus(request, Request.Status.IN_PROCESS);
            log.debug("Status updated to IN_PROCESS for request id: {}", request.getId());

            PhotoTemplate scannedTemplate = preparePhotoTemplate(request.getScannedDocument());
            //PhotoTemplate scannedTemplate = templateBuilderServiceRestClient.getPhotoTemplate(scannedPhoto);
            log.debug("scannedPhoto -> scannedTemplate: {}", scannedTemplate);
            if (scannedTemplate == null) throw new NullArgumentException("scannedPhoto -> scannedTemplate CANT BUILT TEMPLATE");

            addBioTemplateToDocument(request, request.getScannedDocument(), scannedTemplate);
            log.debug("Saved scannedTemplate: {}", scannedTemplate);

            PhotoTemplate webCamTemplate = preparePhotoTemplate(request.getCameraDocument());
            //PhotoTemplate webCamTemplate = templateBuilderServiceRestClient.getPhotoTemplate(webCamPhoto);
            log.debug("webCamPhoto -> webCamTemplate: {}", webCamTemplate);
            if (webCamTemplate == null) throw new NullArgumentException("webCamPhoto -> webCamTemplate CANT BUILT TEMPLATE");

            addBioTemplateToDocument(request, request.getCameraDocument(), webCamTemplate);
            log.debug("saved webCamTemplate: {}", webCamTemplate);

            //шаг 5 Построение фильтров
            // компонент 8 Сервис анализа изображений
//                photoAnalyzerServiceRestClient.analyzePhoto(scannedPhoto);//todo uncomment me
//            log.debug("analyzed scannedPhoto size: {}", scannedPhoto.length);

//                photoAnalyzerServiceRestClient.analyzePhoto(webCamPhoto);//todo uncomment me
 //           log.debug("analyzed webCamPhoto size: {}", webCamPhoto.length);

            //шаг в 6 Сравнение со списками
            // компонент 9 Сервис сравнения
            log.debug("compareServiceRequest - scannedTemplate + webCamTemplate");

            CompareServiceRequest compareServiceRequest = new CompareServiceRequest();
            compareServiceRequest.setIin(request.getPerson().getId());
            compareServiceRequest.setWfmId(request.getId());
            compareServiceRequest.setWebFullFrameURL(request.getCameraDocument().getOrigImageURL());
            compareServiceRequest.setWebPreviewURL(request.getCameraDocument().getFaceSquare());
            compareServiceRequest.setScanFullFrameURL(request.getScannedDocument().getOrigImageURL());
            compareServiceRequest.setScanPreviewURL(request.getScannedDocument().getFaceSquare());

            if (scannedTemplate != null) {
                compareServiceRequest.setScanTemplate(scannedTemplate.template);
            } else {
                compareServiceRequest.setScanTemplate(new double[]{});
            }

            if (webCamTemplate != null) {
                compareServiceRequest.setWebTemplate(webCamTemplate.template);
            } else {
                compareServiceRequest.setWebTemplate(new double[]{});
            }

            log.debug("compareServiceRequest: {}", compareServiceRequest);

            String compareResult = compareServiceRestClient.compare(compareServiceRequest);
            String jsonResult = enrich(compareResult, request);

            CompareReport report = objectMapper.readValue(jsonResult, CompareReport.class);
            compareResultService.saveOrUpdate(new CompareResult(request.getId(), jsonResult, report.getPicSimilarity()));

            if (isNeedToSentToWorkflowQueue()) {
                jmsTemplate.convertAndSend(jsonResult);
                log.debug("compareResult: {} sent to queue.", compareResult);
            }

            updateRequestStatus(request, Request.Status.SUCCESS);
            log.debug("Successfully processed request: {}", request.getId());

        } catch (RestClientException rce) {
            log.error(String.format("Can't process request: %s", request.getId()), rce);

            updateRequestStatus(request, Request.Status.FAILED);
            log.warn("Status updated to FAILED for request: {}", request.getId());
            if (isNeedToSentToWorkflowQueue()) {
                jmsTemplate.convertAndSend(rce.toJSON());
            }

        } catch (NullArgumentException nae) {
            log.error(String.format("Can't process request: %d", request.getId()), nae);

            updateRequestStatus(request, Request.Status.NO_VECTOR); // выставляем статус для ретрая
            log.warn("Status updated to NO_VECTOR for request: {}", request.getId());
        } catch (Throwable e) {
            log.error(String.format("Can't process request: %d", request.getId()), e);

            updateRequestStatus(request, Request.Status.FAILED); // выставляем статус для ретрая
            log.warn("Status updated to FAILED for request: {}", request.getId());
        }
    }

    private void sendToRetryQueue(Request request, int previousRetryCount) {
        try {
            RequestRetry retry = new RequestRetry(request.getId(), previousRetryCount + 1, new Date());
            String retryJson = objectMapper.writeValueAsString(retry);
            retryRequestTemplate.convertAndSend(retryJson);
        } catch (JsonProcessingException e) {
            log.error(String.format("Can't send retry message to queue: %s for request: %d",
                    retryRequestTemplate.getDefaultDestinationName(), request.getId()), e);
        }
    }

    /**
     * Повторяет обработку сбойных заявок на основе сообщений в очереди повтора.
     */
    private void retryFailedRequests() {
        try {
            int numberOfRetriedRequests = 0;

            while (numberOfRetriedRequests < MAX_NUMBER_OF_REQUESTS_TO_RETRY_IN_ONE_TIME) {
                String retryJson = (String) retryRequestTemplate.receiveAndConvert();

                if (!Strings.isNullOrEmpty(retryJson)) {
                    log.debug("Retrying request: {}", retryJson);

                    RequestRetry retry = objectMapper.readValue(retryJson, RequestRetry.class);
                    if (retry.getRetryCount() > MAX_NUMBER_OF_RETRIES) {
                        log.warn("Maximum number of retries ({}) is reached for request: {}. Retry will NOT proceed.",
                                MAX_NUMBER_OF_RETRIES, retry.getRequestId());
                    } else {
                        Request request = requestService.findById(retry.getRequestId());
                        if (request.getStatus() == Request.Status.FAILED) {
                            processRequest(request, retry.getRetryCount());
                        }
                    }
                }

                numberOfRetriedRequests++;
            }

        } catch (IOException e) {
            log.error(String.format("Can't read retry message from queue: %s",
                    retryRequestTemplate.getDefaultDestinationName()), e);
        }
    }

    private boolean isNeedToSentToWorkflowQueue() {
        return "true".equalsIgnoreCase(systemSettingsBean.get(SystemSettingsType.SEND_TO_WORKFLOW_QUEUE));
    }

    private String enrich(String compareResult, Request request) throws Exception {
        log.debug("enrich compareResult length: {}  request: {}", StringUtils.length(compareResult), request);

        JsonNode result = objectMapper.readValue(compareResult, JsonNode.class);

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
        if (request.getScannedDocument().getOrigImageURL() != null) {
            ((ObjectNode) result).put("scannedPictureURL", request.getScannedDocument().getOrigImageURL());
        }

        ((ObjectNode) result).put("scannedPicturePreviewURL", request.getScannedDocument().getFaceSquare());

        if (request.getCameraDocument().getOrigImageURL() != null) {
            ((ObjectNode) result).put("webCamPictureURL", request.getCameraDocument().getOrigImageURL());
        }

        ((ObjectNode) result).put("webCamPicturePreviewURL", request.getCameraDocument().getFaceSquare());

        String jsonResult = result.toString();
        log.debug("jsonResult: {}", jsonResult);

        return jsonResult;
    }

    private void addBioTemplateToDocument(Request request, Document document, PhotoTemplate scannedTemplate) throws IOException {
        if (scannedTemplate == null) {
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
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.put("http://localhost:9080/client/rest/dequeue/"+request.getId(),new HashMap<>());
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

    public void setCompareServiceRestClient(CompareServiceRestClient compareServiceRestClient) {
        this.compareServiceRestClient = compareServiceRestClient;
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
