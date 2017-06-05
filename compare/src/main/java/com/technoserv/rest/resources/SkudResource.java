package com.technoserv.rest.resources;

import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.jaxrs.annotation.JacksonFeatures;
import com.technoserv.db.model.objectmodel.Document;
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
import io.swagger.annotations.Api;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.ws.rs.*;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.*;

//import javax.ws.rs.core.Response;


@Component
@Path("")
@Api(value = "Skud")
public class SkudResource extends BaseResource<Long, StopList> implements InitializingBean {

    private static final Log log = LogFactory.getLog(SkudResource.class);

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
        log.debug("---------------------\nИницаиализация сервиса сравнения BIOSKUD");
        //listManager = new CompareListManager(documentService);
        List<StopList> allLists = stopListService.getAll("owner", "owner.bioTemplates");
        log.debug("Number of stop lists is:" + allLists.size());
        for (StopList element : allLists) {
            skudListManager.addList(element);
            System.out.println("name=" + element.getStopListName() + " id=" + element.getId() + " similarity=" + element.getSimilarity());
            Iterator<Document> id = element.getOwner().iterator();
        }
        System.out.println("++++++++BEFORE_LOAD_TevianVectorComparator+++++++++");
        System.loadLibrary("TevianVectorComparator");
        System.out.println("++++++++AFTER_LOAD_TevianVectorComparator+++++++++");
        //Todo тут логика при старте приложенния
        //Фактически данный бин singleton и создаётся при старте приложения
        System.out.println("Конец инициализации сервиса сравнения BIOSKUD\n-------------------------");
    }

    /*
    * Сравнить картинки с блеклистами и досье и вернуть отчет
    */
    @PUT
    @Path("/skud")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response skudCompareImages(SkudCompareRequest message) {
        if (message == null) {
            log.info("++++++++++ NULL Request ++++++++++++");
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON + "; charset=UTF-8").build();
        }

        SkudCompareResponse response = new SkudCompareResponse();
        CompareResponsePhotoObject po = new CompareResponsePhotoObject();
        po.setSimilarity(0d);
        response.setMatch(po);

        try {
            CompareResponseBlackListObject res = skudListManager.compare1(message.getTemplate(), 6119l); //TODO move to parameters HARDCODED
            if (res == null)
                return Response.status(Response.Status.OK).entity(response).header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON + "; charset=UTF-8").build();

           List<CompareResponsePhotoObject> res1 = res.getPhoto();
            if (res1.size() > 0) //writing most similar element
            {
                Double simil = 0d;
                for (CompareResponsePhotoObject el : res1) {
                    if (el.getSimilarity() > response.getMatch().getSimilarity())
                        response.setMatch(el);
                }
            }
            //response.setMatch(res1.get(0));

            return Response.status(Response.Status.OK).entity(response).header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON + "; charset=UTF-8").build();
        } catch (Exception e) {
            log.info("++++++++++ Exception BIOSKUD Comparer Request ++++++++++++");
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e).header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON + "; charset=UTF-8").build();
        }
    }

    @Path("/results")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @JacksonFeatures(serializationEnable = {SerializationFeature.INDENT_OUTPUT})
    public Collection<SkudResult> skud() {
        if (skudResultService == null) {
            System.out.println("skudResultService is null");
            return null;
        }
        //return skudResultService.getAll();
        return skudResultService.findAll();
    }

}