package com.technoserv.bio.kernel;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.technoserv.bio.kernel.rest.client.CompareServiceRestClient;
import com.technoserv.bio.kernel.rest.client.PhotoAnalyzerServiceRestClient;
import com.technoserv.bio.kernel.rest.client.TemplateBuilderServiceRestClient;
import com.technoserv.db.model.objectmodel.*;
import com.technoserv.db.service.objectmodel.api.BioTemplateService;
import com.technoserv.db.service.objectmodel.api.BioTemplateTypeService;
import com.technoserv.db.service.objectmodel.api.BioTemplateVersionService;
import com.technoserv.rest.exception.RestClientException;
import com.technoserv.bio.kernel.rest.request.CompareServiceRequest;
import com.technoserv.bio.kernel.rest.response.PhotoTemplate;
import com.technoserv.db.service.objectmodel.api.RequestService;
import com.technoserv.rest.client.PhotoPersistServiceRestClient;
import com.technoserv.rest.request.Base64Photo;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.Date;
import java.util.List;

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
    private CompareServiceRestClient сompareServiceRestClient;

    @Autowired
    private JmsTemplate jmsTemplate;

    @Autowired
    private BioTemplateService bioTemplateService;

    @Autowired
    private BioTemplateVersionService bioTemplateVersionService;

    @Autowired
    private BioTemplateTypeService bioTemplateTypeService;

    public RequestProcessor(){};

    public Collection<Request> findRequestForProcessing(){
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
                byte[] scannedPhoto = photoPersistServiceRestClient.getPhoto(request.getScannedDocument().getOrigImageURL());
                writeLog("Downloaded scannedPhoto '" + scannedPhoto + "'");
                byte[] webCamPhoto = photoPersistServiceRestClient.getPhoto(request.getCameraDocument().getOrigImageURL());
                writeLog("Downloaded webCamPhoto '" + webCamPhoto + "'");
                // шаг 4 построение шаблона
                // компонент 7. Сервис построения шаблонов(биометрическое ядро)
                writeLog("scannedPhoto -> scannedTemplate");
                PhotoTemplate scannedTemplate = templateBuilderServiceRestClient.getPhotoTemplate(scannedPhoto);
                writeLog("scannedPhoto -> scannedTemplate " + scannedTemplate.template);
                writeLog("saving scannedTemplate");
                addBioTemplateToDocument(request, request.getScannedDocument(), scannedTemplate);
                writeLog("saving scannedTemplate - done");

                writeLog("webCamPhoto -> webCamTemplate");
                PhotoTemplate webCamTemplate = templateBuilderServiceRestClient.getPhotoTemplate(webCamPhoto);
                writeLog("webCamPhoto -> webCamTemplate " + webCamTemplate.template);
                writeLog("saving webCamTemplate");
                addBioTemplateToDocument(request, request.getCameraDocument(), webCamTemplate);
                writeLog("saving webCamTemplate - done");
                //шаг 5 Построение фильтров
                // компонент 8 Сервис анализа изображений
                writeLog("analyze scannedPhoto " + scannedPhoto);
                photoAnalyzerServiceRestClient.analyzePhoto(scannedPhoto);
                writeLog("analyze scannedPhoto Done");

                writeLog("analyze webCamPhoto");
                photoAnalyzerServiceRestClient.analyzePhoto(webCamPhoto);
                writeLog("analyze webCamPhoto Done");
                //шаг в 6 Сравнение со списками
                // компонент 9 Сервис сравнения
                writeLog("compareServiceRequest - scannedTemplate +  webCamTemplate");
                CompareServiceRequest compareServiceRequest = new CompareServiceRequest();
                compareServiceRequest.setScanTemplate(scannedTemplate.template);
                compareServiceRequest.setWebTemplate(webCamTemplate.template);
                String compareResult = сompareServiceRestClient.compare(compareServiceRequest);
                writeLog("compareServiceRequest - scannedTemplate +  webCamTemplate Done: " + new String(compareResult.getBytes()));
                writeLog("Send compareServiceRequest");
                jmsTemplate.convertAndSend(compareResult);
                writeLog("Send compareServiceRequest Done");
                writeLog("Update request status to SUCCESS for id = '" + request.getId() + "'");
                updateRequestStatus(request, Request.Status.SUCCESS);
                writeLog("Update request status to SUCCESS for id = '" + request.getId() + "' Done");
            } catch (RestClientException ex){
                ex.printStackTrace();
                writeLog("Update request status to FAILED for id = '" + request.getId() + "'");
                updateRequestStatus(request, Request.Status.FAILED);
                writeLog("Update request status to FAILED for id = '" + request.getId() + "' Done");
                jmsTemplate.convertAndSend(ex.toJSON());
            } catch (Throwable e) {
                e.printStackTrace();
                writeLog("Update request status to SAVED for id = '" + request.getId() + " for retry");
                updateRequestStatus(request, Request.Status.SAVED);//выставляем статус для ретрая
                writeLog("Update request status to SAVED for id = '" + request.getId() + " Done");
            }
        }
    }

    private void addBioTemplateToDocument(Request request,Document document, PhotoTemplate scannedTemplate) throws IOException {

        BioTemplate bioTemplate = new BioTemplate();
        bioTemplate.setInsUser(request.getInsUser());
        ObjectMapper objectMapper = new ObjectMapper();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        objectMapper.writeValue(byteArrayOutputStream,scannedTemplate.template);
        bioTemplate.setTemplateVector(byteArrayOutputStream.toByteArray());
        bioTemplate.setDocument(document);
        BioTemplateVersion bioTemplateVersion = bioTemplateVersionService.findById((long) scannedTemplate.version);
        if(bioTemplateVersion==null) {
            bioTemplateVersion = new BioTemplateVersion();
            bioTemplateVersion.setId((long) scannedTemplate.version);
            bioTemplateVersion.setObjectDate(new Date());
            bioTemplateVersion.setDescription("Версия " + scannedTemplate.version);
            bioTemplateVersionService.saveOrUpdate(bioTemplateVersion);
        }
        bioTemplate.setBioTemplateVersion(bioTemplateVersion);
        BioTemplateType bioTemplateType = bioTemplateTypeService.findById((long) scannedTemplate.type);
        if(bioTemplateType==null) {
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
