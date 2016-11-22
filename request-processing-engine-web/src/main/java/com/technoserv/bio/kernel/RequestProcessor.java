package com.technoserv.bio.kernel;

import com.technoserv.bio.kernel.rest.TemplateBuilderServiceRestClient;
import com.technoserv.bio.kernel.rest.response.PhotoTemplate;
import com.technoserv.db.model.objectmodel.Request;
import com.technoserv.db.service.objectmodel.api.RequestService;
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

            //считываю с 11;
            // 4. построение шаблона;

            PhotoTemplate template = templateBuilderServiceRestClient.getPhotoTemplate("base 64 photo string");



            //иду в 5-8;
            //иду в 6-9;
            //кладу 2;
        }
    }
}
