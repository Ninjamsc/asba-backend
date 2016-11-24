package com.technoserv.bio.kernel;

import com.technoserv.bio.kernel.rest.client.CompareServiceRestClient;
import com.technoserv.bio.kernel.rest.client.PhotoAnalyzerServiceRestClient;
import com.technoserv.bio.kernel.rest.client.TemplateBuilderServiceRestClient;
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

import java.util.Collection;

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

    public RequestProcessor() {
    }

    public Collection<Request> findRequestForProcessing(){
        return requestService.findNotProcessed();
    }

    public void process() {
        logger.debug("RequestProcessor process order");
        for (Request request : findRequestForProcessing()) {
            try {
                updateRequestStatus(request, Request.Status.IN_PROCESS);
                // шаг 4 построение шаблона
                // компонент 7. Сервис построения шаблонов(биометрическое ядро)
                Base64Photo scannedPhoto = photoPersistServiceRestClient.getPhoto(request.getScannedDocument().getOrigImageURL());
                Base64Photo webCamPhoto = photoPersistServiceRestClient.getPhoto(request.getCameraDocument().getOrigImageURL());
                PhotoTemplate scannedTemplate = templateBuilderServiceRestClient.getPhotoTemplate(scannedPhoto);
                PhotoTemplate webCamTemplate = templateBuilderServiceRestClient.getPhotoTemplate(webCamPhoto);
                //шаг 5 Построение фильтров
                // компонент 8 Сервис анализа изображений
                photoAnalyzerServiceRestClient.analizePhoto("scannedTemplate");
                photoAnalyzerServiceRestClient.analizePhoto("webCamTemplate");
                //шаг в 6 Сравнение со списками
                // компонент 9 Сервис сравнения
                CompareServiceRequest compareServiceRequest = new CompareServiceRequest();
                compareServiceRequest.setScanTemplate(scannedTemplate.template);
                compareServiceRequest.setWebTemplate(webCamTemplate.template);
                String compareResult = сompareServiceRestClient.compare(compareServiceRequest);
                jmsTemplate.convertAndSend(compareResult);
                updateRequestStatus(request, Request.Status.SUCCESS);
            } catch (RestClientException ex){
                updateRequestStatus(request, Request.Status.FAILED);
                jmsTemplate.convertAndSend(ex.toJSON());
            }
        }
    }

    private void updateRequestStatus(Request request, Request.Status status) {
        request.setStatus(status);
        requestService.saveOrUpdate(request);
    }
}
