package com.technoserv.rest.resources;

import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.jaxrs.annotation.JacksonFeatures;
import com.technoserv.db.model.objectmodel.StopList;
import com.technoserv.db.service.Service;
import com.technoserv.db.service.configuration.impl.SystemSettingsBean;
import com.technoserv.db.service.objectmodel.api.SkudResultService;
import com.technoserv.db.service.objectmodel.api.StopListService;
import com.technoserv.rest.model.CompareResponseBlackListObject;
import com.technoserv.rest.model.CompareResponsePhotoObject;
import com.technoserv.rest.model.SkudCompareRequest;
import com.technoserv.rest.model.SkudCompareResponse;
import com.technoserv.utils.HttpUtils;
import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.List;

import static com.technoserv.config.ConfigValues.SKUD_STOP_LIST_ID;


@Component
@Path("")
@Api(value = "Compare")
public class SkudResource extends BaseResource<Long, StopList> implements InitializingBean {

    private static final Logger log = LoggerFactory.getLogger(SkudResource.class);

    private static final String LIBRARY_TEVIAN_VECTOR_COMPARATOR = "TevianVectorComparator";

    @Autowired
    private SkudCompareListManager skudListManager;

    @Autowired
    private SkudResultService skudResultService;

    @Autowired
    private StopListService stopListService;

    @Autowired
    private SystemSettingsBean systemSettingsBean;

    @Value(SKUD_STOP_LIST_ID)
    private long skudStopListId;

    @Resource
    @Qualifier(value = "converters")
    private HashMap<String, String> compareRules;

    @Override
    protected Service<Long, StopList> getBaseService() {
        return stopListService;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        log.debug("Иницаиализация сервиса сравнения Bioskud");

        List<StopList> allLists = stopListService.getAll("owner", "owner.bioTemplates");
        log.debug("Number of stop lists is: {}", allLists.size());

        for (StopList stopList : allLists) {
            skudListManager.addList(stopList);
            log.debug("Stop list added: {} ", stopList);
        }

        // native library loading

//        log.info("Before load {}", LIBRARY_TEVIAN_VECTOR_COMPARATOR);
//
//        if (NativeLibraryHelper.isLibraryLoaded(LIBRARY_TEVIAN_VECTOR_COMPARATOR, ClassLoader.getSystemClassLoader())) {
//            log.warn("{} is already loaded.", LIBRARY_TEVIAN_VECTOR_COMPARATOR);
//
//        } else {
//            log.info("{} is not loaded. Loading...", LIBRARY_TEVIAN_VECTOR_COMPARATOR);
//            System.loadLibrary(LIBRARY_TEVIAN_VECTOR_COMPARATOR);
//        }
//
//        log.info("After load {}", LIBRARY_TEVIAN_VECTOR_COMPARATOR);

        log.debug("Конец инициализации сервиса сравнения Bioskud");
    }

    /**
     * Сравнить картинки с блеклистами и досье и вернуть отчет.
     */
    @PUT
    @Path("/skud")
    @Consumes(HttpUtils.APPLICATION_JSON_UTF8)
    @Produces(HttpUtils.APPLICATION_JSON_UTF8)
    public Response skudCompareImages(SkudCompareRequest message) {
        log.debug("skudCompareImages message: {}", message);

        if (message == null) {
            log.error("SkudCompareRequest is empty.");
            return Response.serverError().build();
        }

        SkudCompareResponse response = new SkudCompareResponse();
        CompareResponsePhotoObject po = new CompareResponsePhotoObject();
        po.setSimilarity(0d);
        response.setMatch(po);

        try {
            CompareResponseBlackListObject compareResponse = skudListManager.compare1(message.getTemplate(), skudStopListId);
            if (compareResponse == null) {
                return Response.ok(response).build();
            }

            List<CompareResponsePhotoObject> responsePhotos = compareResponse.getPhoto();
            if (!responsePhotos.isEmpty()) {
                // writing most similar element
                for (CompareResponsePhotoObject photo : responsePhotos) {
                    if (photo.getSimilarity() > response.getMatch().getSimilarity()) {
                        response.setMatch(photo);
                    }
                }
            }

            return Response.ok(response).build();
        } catch (Exception e) {
            log.error("Can't compare Bioskud images.", e);
            return Response.serverError().build();
        }
    }

    @Path("/results")
    @GET
    @Produces(HttpUtils.APPLICATION_JSON_UTF8)
    @Consumes(HttpUtils.APPLICATION_JSON_UTF8)
    @JacksonFeatures(serializationEnable = {SerializationFeature.INDENT_OUTPUT})
    public Response getSkudResults() {
        log.debug("getSkudResults");
        return Response.ok()
                .entity(skudResultService.findAll())
                .build();
    }

}