package com.technoserv.rest.resources;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import java.util.Base64;

import javax.annotation.Resource;
import javax.ws.rs.*;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
//import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response;

import com.technoserv.db.model.objectmodel.*;
import com.technoserv.db.service.objectmodel.api.*;
import com.technoserv.rest.client.PhotoPersistServiceRestClient;
import com.technoserv.db.model.configuration.SystemSettingsType;
import com.technoserv.db.service.configuration.impl.SystemSettingsBean;
import com.technoserv.rest.model.*;
import com.technoserv.rest.request.PhotoTemplate;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.math3.analysis.function.Exp;
import org.apache.commons.math3.analysis.function.Pow;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.jaxrs.annotation.JacksonFeatures;
import com.technoserv.db.service.Service;
import com.technoserv.rest.client.TemplateBuilderServiceRestClient;
import com.technoserv.utils.JsonUtils;
import com.technoserv.rest.comparator.CompareRule;
import com.technoserv.rest.comparator.CompareServiceStopListElement;

import io.swagger.annotations.Api;


@Component
@Path("")
@Api(value = "Skud")
public class SkudResource extends BaseResource<Long,StopList> implements InitializingBean  {

    private static final Log log = LogFactory.getLog(SkudResource.class);

    @Autowired
    private SkudCompareListManager skudListManager;

    @Autowired
    private SkudResultService skudResultService;

    @Autowired
    private StopListService stopListService;

    @Autowired
    private SystemSettingsBean systemSettingsBean;


    @Resource @Qualifier(value = "converters")
    private HashMap<String, String> compareRules;

    @Override
    protected Service<Long, StopList> getBaseService() {
        return stopListService;
    }

    @Override
    public void afterPropertiesSet() throws Exception {

        log.debug("---------------------\nИницаиализация сервиса сравнения BIOSKUD");
        //listManager = new CompareListManager(documentService);
        List<StopList> allLists = stopListService.getAll("owner","owner.bioTemplates");
        log.debug("Number of stop lists is:"+allLists.size());
        for (StopList element : allLists)
        {
            skudListManager.addList(element);
            System.out.println("name="+element.getStopListName()+" id="+element.getId()+" similarity="+element.getSimilarity());
            Iterator<Document> id = element.getOwner().iterator();

        }

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
    public Response skudCompareImages(SkudCompareRequest message)   {
        if(message == null)
        {
            log.info("++++++++++ NULL Request ++++++++++++");
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON+"; charset=UTF-8").build();
        }

        SkudCompareResponse response = new SkudCompareResponse();
        CompareResponsePhotoObject po = new CompareResponsePhotoObject();
        po.setSimilarity(0d);
        response.setMatch(po);

        try {
            CompareResponseBlackListObject res = skudListManager.compare1(message.getTemplate(), 2589l); //TODO move to parameters HARDCODED
            if (res == null)
                return Response.status(Response.Status.OK).entity(response).header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON+"; charset=UTF-8").build();

            ArrayList<CompareResponsePhotoObject> res1 = res.getPhoto();
            if (res1.size() > 0) //writing most similar element
            {
                Double simil = 0d;
                for(CompareResponsePhotoObject el : res1)
                {
                    if (el.getSimilarity() > response.getMatch().getSimilarity())
                         response.setMatch(el);
                }
            }
                //response.setMatch(res1.get(0));

            return Response.status(Response.Status.OK).entity(response).header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON+"; charset=UTF-8").build();
        }
        catch (Exception e)
        {
            log.info("++++++++++ Exception BIOSKUD Comparer Request ++++++++++++");
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e).header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON+"; charset=UTF-8").build();
        }

    }

    @Path("/results")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @JacksonFeatures( serializationEnable =  { SerializationFeature.INDENT_OUTPUT })
    public Collection<SkudResult> skud() {
        if(skudResultService == null)  {System.out.println("skudResultService is null"); return null;}
        return skudResultService.getAll();
    }


}