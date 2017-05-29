package com.technoserv.rest.resources;

import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.jaxrs.annotation.JacksonFeatures;
import com.technoserv.db.model.objectmodel.SkudResult;
import com.technoserv.db.model.objectmodel.StopList;
import com.technoserv.db.service.Service;
import com.technoserv.db.service.configuration.impl.SystemSettingsBean;
import com.technoserv.db.service.objectmodel.api.SkudResultService;
import com.technoserv.db.service.objectmodel.api.StopListService;
import com.technoserv.rest.model.CompareResponseBlackListObject;
import com.technoserv.rest.model.CompareResponsePhotoObject;
import com.technoserv.rest.model.SkudCompareRequest;
import com.technoserv.rest.model.SkudCompareResponse;
import com.technoserv.util.Misc;
import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.ws.rs.*;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;


@Component
@Path("")
@Api(value = "Skud")
public class SkudResource extends BaseResource<Long, StopList> implements InitializingBean {

    private static final Logger log = LoggerFactory.getLogger(SkudResource.class);

    // TODO: move to application.properties
    private static final long STOP_LIST_ID = 6119L;

    @Autowired
    private SkudCompareListManager skudListManager;

    @Autowired
    private SkudResultService skudResultService;

    @Autowired
    private StopListService stopListService;

    @Autowired
    private SystemSettingsBean systemSettingsBean;

    @Resource
    @Qualifier(value = "converters")
    private HashMap<String, String> compareRules;

    @Override
    protected Service<Long, StopList> getBaseService() {
        return stopListService;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        log.debug("Иницаиализация сервиса сравнения BIOSKUD");
        List<StopList> allLists = stopListService.getAll("owner", "owner.bioTemplates");
        log.debug("Number of stop lists is: {}", allLists.size());

        for (StopList element : allLists) {
            skudListManager.addList(element);
            log.debug("name: {} id: {} similarity: {}", element.getStopListName(), element.getId(), element.getSimilarity());
        }

        log.debug("BEFORE_LOAD_TevianVectorComparator");
        System.loadLibrary("TevianVectorComparator");
        log.debug("AFTER_LOAD_TevianVectorComparator");

        //Todo тут логика при старте приложенния
        //Фактически данный бин singleton и создаётся при старте приложения
        log.debug("Конец инициализации сервиса сравнения BIOSKUD");
    }

    /**
     * Сравнить картинки с блеклистами и досье и вернуть отчет.
     */
    @PUT
    @Path("/skud")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response skudCompareImages(SkudCompareRequest message) {
        if (message == null) {
            log.error("NULL Request");
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .header(HttpHeaders.CONTENT_TYPE, Misc.DEFAULT_MEDIA_TYPE)
                    .build();
        }

        SkudCompareResponse response = new SkudCompareResponse();
        CompareResponsePhotoObject po = new CompareResponsePhotoObject();
        po.setSimilarity(0d);
        response.setMatch(po);

        try {
            CompareResponseBlackListObject res = skudListManager.compare1(message.getTemplate(), STOP_LIST_ID);
            if (res == null) {
                return Response.status(Response.Status.OK)
                        .entity(response)
                        .header(HttpHeaders.CONTENT_TYPE, Misc.DEFAULT_MEDIA_TYPE)
                        .build();
            }

            List<CompareResponsePhotoObject> res1 = res.getPhoto();
            if (res1.size() > 0) {
                // writing most similar element
                for (CompareResponsePhotoObject el : res1) {
                    if (el.getSimilarity() > response.getMatch().getSimilarity()) {
                        response.setMatch(el);
                    }
                }
            }

            return Response.status(Response.Status.OK)
                    .entity(response).header(HttpHeaders.CONTENT_TYPE, Misc.DEFAULT_MEDIA_TYPE)
                    .build();

        } catch (Exception e) {
            log.error("Can't compare images BIOSKUD comparator request", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(e)
                    .header(HttpHeaders.CONTENT_TYPE, Misc.DEFAULT_MEDIA_TYPE)
                    .build();
        }
    }

    @Path("/results")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @JacksonFeatures(serializationEnable = {SerializationFeature.INDENT_OUTPUT})
    public Collection<SkudResult> skud() {
        if (skudResultService == null) {
            log.warn("skudResultService is null");
            return null;
        }
        return skudResultService.findAll();
    }

}