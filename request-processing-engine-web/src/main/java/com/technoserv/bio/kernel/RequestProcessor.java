package com.technoserv.bio.kernel;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.technoserv.bio.kernel.rest.client.CompareServiceRestClient;
import com.technoserv.bio.kernel.rest.client.PhotoAnalyzerServiceRestClient;
import com.technoserv.bio.kernel.rest.client.TemplateBuilderServiceRestClient;
import com.technoserv.db.model.objectmodel.BioTemplate;
import com.technoserv.db.model.objectmodel.BioTemplateVersion;
import com.technoserv.db.model.objectmodel.Document;
import com.technoserv.db.service.objectmodel.api.BioTemplateService;
import com.technoserv.db.service.objectmodel.api.BioTemplateVersionService;
import com.technoserv.rest.exception.RestClientException;
import com.technoserv.bio.kernel.rest.request.CompareServiceRequest;
import com.technoserv.bio.kernel.rest.response.PhotoTemplate;
import com.technoserv.db.model.objectmodel.Request;
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

    private static final Log logger = LogFactory.getLog(RequestProcessor.class);

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

    public RequestProcessor(){};

    public Collection<Request> findRequestForProcessing(){
        return requestService.findNotProcessed();
    }

    public void process() {
        logger.debug("RequestProcessor process order");
        Collection<Request> requestList = findRequestForProcessing();
        for (Request request : requestList) {
            try {
                updateRequestStatus(request, Request.Status.IN_PROCESS);
                // шаг 4 построение шаблона
                // компонент 7. Сервис построения шаблонов(биометрическое ядро)
                Base64Photo scannedPhoto = photoPersistServiceRestClient.getPhoto(request.getScannedDocument().getOrigImageURL());
                Base64Photo webCamPhoto = photoPersistServiceRestClient.getPhoto(request.getCameraDocument().getOrigImageURL());

                PhotoTemplate scannedTemplate = templateBuilderServiceRestClient.getPhotoTemplate(scannedPhoto);
                addBioTemplateToDocument(request, request.getScannedDocument(), scannedTemplate);

                PhotoTemplate webCamTemplate = templateBuilderServiceRestClient.getPhotoTemplate(webCamPhoto);
                addBioTemplateToDocument(request, request.getCameraDocument(), webCamTemplate);
                //шаг 5 Построение фильтров
                // компонент 8 Сервис анализа изображений
                photoAnalyzerServiceRestClient.analizePhoto(scannedPhoto.photos);
                photoAnalyzerServiceRestClient.analizePhoto(webCamPhoto.photos);
                //шаг в 6 Сравнение со списками
                // компонент 9 Сервис сравнения
                CompareServiceRequest compareServiceRequest = new CompareServiceRequest();
                compareServiceRequest.setScanTemplate(scannedTemplate.template);
                compareServiceRequest.setWebTemplate(webCamTemplate.template);
                String compareResult = сompareServiceRestClient.compare(compareServiceRequest);
                jmsTemplate.convertAndSend(compareResult);
                updateRequestStatus(request, Request.Status.SUCCESS);
            } catch (RestClientException ex){
                ex.printStackTrace();
                updateRequestStatus(request, Request.Status.FAILED);
                jmsTemplate.convertAndSend(ex.toJSON());
            } catch (IOException e) {
                e.printStackTrace();
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
        BioTemplateVersion bioTemplateVersion = bioTemplateVersionService.findById(scannedTemplate.version);
        if(bioTemplateVersion==null) {
            bioTemplateVersion = new BioTemplateVersion();
            bioTemplateVersion.setId(scannedTemplate.version);
            bioTemplateVersion.setObjectDate(new Date());
            bioTemplateVersion.setDescription("Версия " + scannedTemplate.version );
        }
        bioTemplate.setBioTemplateVersion(bioTemplateVersion);
        bioTemplateVersionService.saveOrUpdate(bioTemplateVersion);
        bioTemplateService.saveOrUpdate(bioTemplate);
        document.getBioTemplates().add(bioTemplate);
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
