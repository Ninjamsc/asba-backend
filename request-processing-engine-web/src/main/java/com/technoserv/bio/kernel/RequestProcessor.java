package com.technoserv.bio.kernel;

import com.technoserv.bio.kernel.dao.configuration.rest.CompareServiceRestClient;
import com.technoserv.bio.kernel.dao.configuration.rest.PhotoAnalizerServiceRestClient;
import com.technoserv.bio.kernel.dao.configuration.rest.TemplateBuilderServiceRestClient;
import com.technoserv.bio.kernel.dao.configuration.rest.request.CompareServiceRequest;
import com.technoserv.bio.kernel.dao.configuration.rest.response.PhotoTemplate;
import com.technoserv.db.model.objectmodel.Request;
import com.technoserv.db.service.objectmodel.api.RequestService;
import com.technoserv.rest.client.PhotoPersistServiceRestClient;
import com.technoserv.rest.request.Base64Photo;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.inject.Inject;
import java.util.List;

/**
 * Created by Adrey on 22.11.2016.
 */
public class RequestProcessor implements Runnable{

    private static final Log logger = LogFactory.getLog(RequestProcessor.class);

    @Inject
    private RequestService requestService;

    @Inject
    private TemplateBuilderServiceRestClient templateBuilderServiceRestClient;

    @Inject
    private PhotoPersistServiceRestClient photoPersistServiceRestClient;

    @Inject
    private PhotoAnalizerServiceRestClient photoAnalizerServiceRestClient;

    @Inject
    private CompareServiceRestClient сompareServiceRestClient;

    public RequestProcessor() {
    }

    public List<Request> findRequestForProcessing(){
        //todo Найти заявки для обработки;
        return requestService.getAll(); //todo criteria search
    }

    public void process() {
        logger.debug("RequestProcessor process order");
    }

    @Override
    public void run() {
        //todo cron
        for (Request request : findRequestForProcessing()) {
            // шаг 4 построение шаблона
            // компонент 7. Сервис построения шаблонов(биометрическое ядро)
            Base64Photo scannedPhoto = photoPersistServiceRestClient.getPhoto(request.getScannedDocument().getOrigImageURL());
            Base64Photo webCamPhoto = photoPersistServiceRestClient.getPhoto(request.getCameraDocument().getOrigImageURL());
            PhotoTemplate scannedTemplate = templateBuilderServiceRestClient.getPhotoTemplate(scannedPhoto);
            PhotoTemplate webCamTemplate = templateBuilderServiceRestClient.getPhotoTemplate(webCamPhoto);
            //шаг 5 Построение фильтров
            // компонент 8 Сервис анализа изображений
            PhotoTemplate analizedScannedTemplate = photoAnalizerServiceRestClient.analizePhoto("scannedTemplate");
            PhotoTemplate analizedWebCamTemplate = photoAnalizerServiceRestClient.analizePhoto("webCamTemplate");
            //шаг в 6 Сравнение со списками
            // компонент 9 Сервис сравнения
            CompareServiceRequest compareServiceRequest = new CompareServiceRequest();
            compareServiceRequest.setScanTemplate(analizedScannedTemplate.template);
            compareServiceRequest.setWebTemplate(analizedWebCamTemplate.template);
            String result = сompareServiceRestClient.compare(compareServiceRequest);
        }
    }
}
