package com.technoserv.bio.kernel;

import com.technoserv.bio.kernel.rest.CompareServiceRestClient;
import com.technoserv.bio.kernel.rest.PhotoAnalizerServiceRestClient;
import com.technoserv.bio.kernel.rest.TemplateBuilderServiceRestClient;
import com.technoserv.bio.kernel.rest.request.CompareServiceRequest;
import com.technoserv.bio.kernel.rest.response.PhotoAnalyzeResult;
import com.technoserv.bio.kernel.rest.response.PhotoTemplate;
import com.technoserv.db.model.objectmodel.Request;
import com.technoserv.db.service.objectmodel.api.RequestService;
import com.technoserv.rest.client.PhotoPersistServiceRestClient;
import com.technoserv.rest.request.Base64Photo;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jvnet.hk2.annotations.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;

import javax.inject.Inject;
import java.util.Collection;

/**
 * Created by Adrey on 22.11.2016.
 */
//@Service(name = "requestProcessor")
public class RequestProcessor implements Runnable{

    private static final Log logger = LogFactory.getLog(RequestProcessor.class);

    @Autowired
    private RequestService requestService;

    @Autowired
    private TemplateBuilderServiceRestClient templateBuilderServiceRestClient;

    @Autowired
    private PhotoPersistServiceRestClient photoPersistServiceRestClient;

    @Autowired
    private PhotoAnalizerServiceRestClient photoAnalizerServiceRestClient;

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
    }

    @Override
    public void run() {
        for (Request request : findRequestForProcessing()) {
            // шаг 4 построение шаблона
            // компонент 7. Сервис построения шаблонов(биометрическое ядро)
            Base64Photo scannedPhoto = photoPersistServiceRestClient.getPhoto(request.getScannedDocument().getOrigImageURL());
            Base64Photo webCamPhoto = photoPersistServiceRestClient.getPhoto(request.getCameraDocument().getOrigImageURL());
            PhotoTemplate scannedTemplate = templateBuilderServiceRestClient.getPhotoTemplate(scannedPhoto);
            PhotoTemplate webCamTemplate = templateBuilderServiceRestClient.getPhotoTemplate(webCamPhoto);
            //шаг 5 Построение фильтров
            // компонент 8 Сервис анализа изображений
            PhotoAnalyzeResult scannedAnalyze = photoAnalizerServiceRestClient.analizePhoto("scannedTemplate");
            PhotoAnalyzeResult analizedAnalyze  = photoAnalizerServiceRestClient.analizePhoto("webCamTemplate");
            if (scannedAnalyze != null || scannedAnalyze.problem != null){
                //todo обработка ошибки
            }
            if (analizedAnalyze != null || analizedAnalyze.problem != null){
                //todo обработка ошибки
            }
            //шаг в 6 Сравнение со списками
            // компонент 9 Сервис сравнения
            CompareServiceRequest compareServiceRequest = new CompareServiceRequest();
            compareServiceRequest.setScanTemplate(scannedTemplate.template);
            compareServiceRequest.setWebTemplate(webCamTemplate.template);
            String compareResult = сompareServiceRestClient.compare(compareServiceRequest);
            jmsTemplate.convertAndSend(compareResult);
            //todo обработка ошибок, уточнить
            updateRequestStatus(request, Request.Status.SUCCESS);
        }
    }

    private void updateRequestStatus(Request request, Request.Status status) {
        request.setStatus(status);
        requestService.saveOrUpdate(request);
    }
}
