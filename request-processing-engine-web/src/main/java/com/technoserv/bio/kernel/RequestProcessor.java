package com.technoserv.bio.kernel;

import com.technoserv.bio.kernel.dao.configuration.rest.TemplateBuilderServiceRestClient;
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

    public RequestProcessor() {
    }

    public List<Request> findRequestForProcessing(){
        //считываю с 10. Сервис хранения БД;
        return requestService.getAll(); //todo criteria search
    }

    public void process() {
        logger.debug("RequestProcessor process order");
    }

    @Override
    public void run() {
        //todo cron
        for (Request request : findRequestForProcessing()) {

            //сохраняю с 11; Это должно быть в Consumer
            // 4. построение шаблона;
            Base64Photo base64photo = photoPersistServiceRestClient.getPhoto(request.getScannedDocument().getOrigImageURL());
            PhotoTemplate template = templateBuilderServiceRestClient.getPhotoTemplate(base64photo);
            //иду в 5-8; Analize

            //иду в 6-9;
            //кладу 2;
        }
    }
}
