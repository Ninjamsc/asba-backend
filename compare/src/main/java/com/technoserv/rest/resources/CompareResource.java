package com.technoserv.rest.resources;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.jaxrs.annotation.JacksonFeatures;
import com.technoserv.db.model.configuration.SystemSettingsType;
import com.technoserv.db.model.objectmodel.*;
import com.technoserv.db.service.Service;
import com.technoserv.db.service.configuration.impl.SystemSettingsBean;
import com.technoserv.db.service.objectmodel.api.*;
import com.technoserv.rest.client.PhotoPersistServiceRestClient;
import com.technoserv.rest.client.TemplateBuilderServiceRestClient;
import com.technoserv.rest.model.*;
import com.technoserv.rest.request.PhotoTemplate;
import com.technoserv.rest.request.RequestSearchCriteria;
import com.technoserv.utils.HttpUtils;
import com.technoserv.utils.JsonUtils;
import com.technoserv.utils.TevianVectorComparator;
import io.swagger.annotations.Api;
import org.apache.commons.lang.time.DateUtils;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.hibernate.Criteria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import java.io.IOException;
import java.util.*;


@Component
@Path("")
@Api(value = "Compare")
public class CompareResource extends BaseResource<Long, StopList> implements InitializingBean {

    private static final Logger log = LoggerFactory.getLogger(CompareResource.class);

    @Autowired
    private CompareListManager listManager;

    @Autowired
    private RequestService requestService;

    @Autowired
    private PersonService personService;

    @Autowired
    private StopListService stopListService;

    @Autowired
    private BioTemplateService templateService;

    @Autowired
    private TemplateBuilderServiceRestClient templateBuilderServiceRestClient;

    @Autowired
    private BioTemplateVersionService bioTemplateVersionService;

    @Autowired
    private BioTemplateTypeService bioTemplateTypeService;

    @Autowired
    private BioTemplateService bioTemplateService;

    @Autowired
    private PhotoPersistServiceRestClient photoServiceClient;

    @Autowired
    private DocumentService documentService;

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
        log.debug("Иницаиализация сервиса сравнения");

        List<StopList> allLists = stopListService.getAll("owner", "owner.bioTemplates");
        log.debug("Number of stop lists: {}", allLists.size());

        for (StopList stopList : allLists) {
            listManager.addList(stopList);
            log.debug("Stop list: {}", stopList);

            for (Document document : stopList.getOwner()) {
                log.debug("Stop list document: {}", document);

                for (BioTemplate bioTemplate : document.getBioTemplates()) {
                    ObjectMapper mapper = new ObjectMapper();
                    double[] array = mapper.readValue(bioTemplate.getTemplateVector(), double[].class);
                    ArrayRealVector arv = new ArrayRealVector(array);
                    log.debug("BioTemplate vector length: {}", arv.toString().length());
                }
            }
        }
        log.debug("Конец инициализации сервиса сравнения");
    }

    public List<CompareResponsePhotoObject> doCompare(Request r,
                                                      ArrayRealVector comparing_vector,
                                                      boolean less,
                                                      double delta) {

        List<CompareResponsePhotoObject> photos = new ArrayList<>();
        double mult = new Double(systemSettingsBean.get(SystemSettingsType.COMPARATOR_MULTIPLIER));
        int power = new Integer(systemSettingsBean.get(SystemSettingsType.COMPARATOR_POWER));
        List<BioTemplate> l1 = r.getScannedDocument().getBioTemplates();
        List<BioTemplate> l2 = r.getCameraDocument().getBioTemplates();

        for (BioTemplate t : l1) {
            ObjectMapper mapper = new ObjectMapper();
            try {
                double[] array = mapper.readValue(t.getTemplateVector(), double[].class);
                double norm = TevianVectorComparator.calculateSimilarityCliWrapper(
                        new String(org.springframework.security.crypto.codec.Base64.encode(TevianVectorComparator.getByteArrayFromVector(comparing_vector.getDataRef()))),
                        new String(org.springframework.security.crypto.codec.Base64.encode(TevianVectorComparator.getByteArrayFromVector(array))), "1");

                if ((less && (norm < delta))
                        || (!less && (norm > delta))) {

                    CompareResponsePhotoObject ph = new CompareResponsePhotoObject();
                    ph.setSimilarity(norm);
                    ph.setUrl(r.getScannedDocument().getFaceSquare());
                    photos.add(ph);
                }
            } catch (IOException e) {
                log.error("Can't retrieve a vector", e);
                return null;
            }
        }

        for (BioTemplate t : l2) {
            ObjectMapper mapper = new ObjectMapper();
            try {
                double[] array = mapper.readValue(t.getTemplateVector(), double[].class);

                double norm = TevianVectorComparator.calculateSimilarityCliWrapper(
                        new String(org.springframework.security.crypto.codec.Base64.encode(TevianVectorComparator.getByteArrayFromVector(comparing_vector.getDataRef()))),
                        new String(org.springframework.security.crypto.codec.Base64.encode(TevianVectorComparator.getByteArrayFromVector(array))), "1");

                if ((less && (norm < delta)) || (!less && (norm > delta))) {
                    CompareResponsePhotoObject ph = new CompareResponsePhotoObject();
                    ph.setSimilarity(norm);
                    ph.setUrl(r.getCameraDocument().getFaceSquare());
                    photos.add(ph);
                }
            } catch (IOException e) {
                log.error("Can't retrieve a vector.", e);
                return null;
            }
        }
        if (photos.size() > 0) return photos;
        return null;
    }

    public CompareResponseRulesObject historyDifference(Long wfmId, Long iin, double[] vector, double coeff, boolean less) {
        log.debug("historyDifference wfmId: {} iin: {} vector: {} coedff: {} less: {}",
                wfmId, iin, vector.length, coeff, less);


        ArrayRealVector comparing_vector = new ArrayRealVector(vector);
        CompareResponseRulesObject rule = new CompareResponseRulesObject();
        List<CompareResponsePhotoObject> photos = new ArrayList<>();

        Collection<Request> coll = requestService.findByIin(iin, "scannedDocument", "scannedDocument.bioTemplates", "cameraDocument", "cameraDocument.bioTemplates");
        if (coll.size() <= 0) return null;

        for (Request r : coll) {
            log.debug("historyDifference - check my id: {} hist_id: {}", wfmId, r.getId());

            if (r.getId().equals(wfmId)) {
                log.debug("historyDifference - skipping myself");
                continue;
            }

            List<BioTemplate> lw = r.getScannedDocument().getBioTemplates();
            List<CompareResponsePhotoObject> result = doCompare(r, comparing_vector, less, coeff);
            if (result != null) {
                log.debug("historyDifference - adding photo to collection:");

                // добавляем полученные объекты в общую коллекцию
                for (CompareResponsePhotoObject o : result) {
                    log.debug("historyDifference - url: {}, similarity: {}", o.getUrl(), o.getSimilarity());
                    photos.add(o);
                }
            }
        }
        if (photos.size() > 0) {
            rule.setRuleId("4.2.1");
            rule.setPhoto(photos);
            rule.setRuleName("Фотография, прикрепленная к заявке, существенно отличается от других фотографий заемщика, имеющихся в базе");
        }
        return rule;
    }

    public Long getCommonListId() {
        return new Long(systemSettingsBean.get(SystemSettingsType.COMPARATOR_COMMON_LIST_ID));
    }

    /**
     * Сравнить картинки с блеклистами и досье и вернуть отчет
     */
    @PUT
    @Path("/template")
    @Consumes(HttpUtils.APPLICATION_JSON_UTF8)
    @Produces(HttpUtils.APPLICATION_JSON_UTF8)
    public Response compareImages(CompareRequest message) {
        log.debug("compareImage message: {}", message);
        CompareResponse response = new CompareResponse();
        List<CompareResponseRulesObject> firedRules = new ArrayList<>();
        try {
            log.debug("compareImages 1.");
            // compare scanned pic
            List<CompareResponseBlackListObject> ls = listManager.compare2(message.getTemplate_scan(), getCommonListId());
            CompareResponseBlackListObject CommonScan = listManager.compare1(message.getTemplate_scan(), getCommonListId());
            CompareResponsePictureReport reportScan = new CompareResponsePictureReport();
            reportScan.setBlackLists(ls);
            reportScan.setPictureURL(message.getScanFullFrameURL());
            reportScan.setPreviewURL(message.getScanPreviewURL());

            log.debug("compareImages 2.");

            // compare webcam pic
            List<CompareResponseBlackListObject> lw = listManager.compare2(message.getTemplate_web(), getCommonListId());
            CompareResponseBlackListObject CommonWeb = listManager.compare1(message.getTemplate_web(), getCommonListId());
            CompareResponsePictureReport reportWeb = new CompareResponsePictureReport();
            reportWeb.setBlackLists(lw);
            reportWeb.setPictureURL(message.getWebFullFrameURL());
            reportWeb.setPreviewURL(message.getWebPreviewURL());
            log.debug("compareImages 3.");

            response.setCameraPicture(reportWeb);
            response.setScannedPicture(reportScan);
            response.setRules(new ArrayList<>()); //TODO: replace with rules
            if (ls.size() > 0 || lw.size() > 0) {
                log.debug("4.2.3 rule ls.size: {} lw.size: {}", ls.size(), lw.size());
                CompareResponseRulesObject rule = new CompareResponseRulesObject();
                rule.setRuleId("4.2.3");
                rule.setRuleName("Возможно соответствие с клиентом из банковского СТОП-ЛИСТА");
                firedRules.add(rule);
            }

            log.debug("compareImages 4.");
            if (CommonScan != null) ls.add(CommonScan);

            if (CommonWeb != null) lw.add(CommonWeb);

            if (CommonScan != null || CommonWeb != null) {
                CompareResponseRulesObject rule = new CompareResponseRulesObject();
                rule.setRuleId("4.2.4");
                rule.setRuleName("Возможно соответствие с клиентом из общего СТОП-ЛИСТА");
                firedRules.add(rule);
            }
            log.debug("compareImages 5.");

        } catch (Exception e) {
            log.error("Can't compare images.", e);
            throw new WebApplicationException(e, Status.INTERNAL_SERVER_ERROR);
        }

        try {
            CompareResponseRulesObject otherness_scan = historyDifference(message.getWfmId(), message.getIin(), message.getTemplate_scan(), new Double(systemSettingsBean.get(SystemSettingsType.DOSSIER_OTHERNESS)), true);
            CompareResponseRulesObject otherness_web = historyDifference(message.getWfmId(), message.getIin(), message.getTemplate_web(), new Double(systemSettingsBean.get(SystemSettingsType.DOSSIER_OTHERNESS)), true);
            List<CompareResponsePhotoObject> all = new ArrayList<>();

            if (otherness_scan != null && otherness_scan.getPhoto() != null && otherness_scan.getPhoto().size() > 0) {
                all.addAll(otherness_scan.getPhoto());
            }

            if (otherness_web != null && otherness_web.getPhoto() != null && otherness_web.getPhoto().size() > 0) {
                all.addAll(otherness_web.getPhoto());
            }

            if (all.size() > 0) {
                CompareResponseDossierReport oth_report = new CompareResponseDossierReport();
                oth_report.setSimilarity(new Double(systemSettingsBean.get(SystemSettingsType.DOSSIER_OTHERNESS)));
                oth_report.setPhotos(all);
                response.setOthernessPictures(oth_report);
                CompareResponseRulesObject rule = new CompareResponseRulesObject();
                rule.setRuleId("4.2.1");
                rule.setRuleName("Фотография, прикрепленная к заявке, существенно отличается от других фотографий заемщика, имеющихся в базе");
                firedRules.add(rule);
                log.debug("compareImages 6.");
            }

        } catch (Exception e) {
            log.error("Can't find dossier differences.", e);
            throw new WebApplicationException(e, Status.INTERNAL_SERVER_ERROR);
        }

        try {
            CompareResponseRulesObject similar_scan = historyDifference(message.getWfmId(), message.getIin(), message.getTemplate_scan(), new Double(systemSettingsBean.get(SystemSettingsType.DOSSIER_SIMILARITY)), false);
            CompareResponseRulesObject similar_web = historyDifference(message.getWfmId(), message.getIin(), message.getTemplate_web(), new Double(systemSettingsBean.get(SystemSettingsType.DOSSIER_SIMILARITY)), false);
            List<CompareResponsePhotoObject> all = new ArrayList<>();

            if (similar_scan != null && similar_scan.getPhoto() != null && similar_scan.getPhoto().size() > 0) {
                all.addAll(similar_scan.getPhoto());
            }

            if (similar_web != null && similar_web.getPhoto() != null && similar_web.getPhoto().size() > 0) {
                all.addAll(similar_web.getPhoto());
            }

            if (all.size() > 0) {
                CompareResponseDossierReport sim_report = new CompareResponseDossierReport();
                sim_report.setSimilarity(new Double(systemSettingsBean.get(SystemSettingsType.DOSSIER_SIMILARITY)));
                sim_report.setPhotos(all);
                response.setSimilarPictures(sim_report);
                CompareResponseRulesObject rule = new CompareResponseRulesObject();
                rule.setRuleId("4.2.2");
                rule.setRuleName("Фотография, прикрепленная к заявке, идентична имеющейся в базе");
                firedRules.add(rule);
                log.debug("compareImages 6.");
            }
        } catch (Exception e) {
            log.error("Can't find a dossier similarity.", e);
            throw new WebApplicationException(e, Status.INTERNAL_SERVER_ERROR);
        }

        // сравнение 2 шаблонов на совпадение
        log.debug("compareImages 7.");
        try {
            SelfCompareResult res = listManager.isSimilar(message.getTemplate_scan(), message.getTemplate_web());
            if (!res.isSimilar()) {
                CompareResponseRulesObject rule = new CompareResponseRulesObject();
                rule.setRuleId("4.2.5");
                rule.setRuleName("Возможно несоответствие фотографии в паспорте и фотографии, прикрепленной к заявке. Порог схожести=" + res.getThreshold() + " схожесть=" + res.getSimilarity());
                firedRules.add(rule);
            }
            response.setPicSimilarity(res.getSimilarity());
            response.setPicSimilarityThreshold(res.getThreshold());
        } catch (Exception e) {
            log.error("Can't perform a self-similarity check.", e);
            throw new WebApplicationException(e, Status.INTERNAL_SERVER_ERROR);
        }

        // add fired rule
        response.setRules(firedRules);
        return Response.ok(response).build();
    }

    /**
     * Обновить стоп лист.
     * @param entity Обновляемая заявка.
     * @return ок

     @Path("/stoplist")
     @PUT
     @Produces(MediaType.APPLICATION_JSON)
     @Consumes(MediaType.APPLICATION_JSON)
     @JacksonFeatures(serializationEnable =  { SerializationFeature.INDENT_OUTPUT })
     @Override public Response update(StopList entity) {
     return super.update(entity);
     }
     */
    /**
     * удалить стоп лист.
     *
     * @param id удаляемой заявки.
     * @return ок
     */
    @DELETE
    @Produces(HttpUtils.APPLICATION_JSON_UTF8)
    @Consumes(HttpUtils.APPLICATION_JSON_UTF8)
    @JacksonFeatures(serializationEnable = {SerializationFeature.INDENT_OUTPUT})
    @Path("/stoplist/{id}")
    @Override
    public Response delete(@PathParam("id") Long id) {
        listManager.delStopList(id);
        return super.delete(id);
    }

    /**
     * Получить стоп лист по ID
     *
     * @param id идентификатор.
     * @return заявка по ID
     */
    @GET
    @Produces(HttpUtils.APPLICATION_JSON_UTF8)
    @Consumes(HttpUtils.APPLICATION_JSON_UTF8)
    @JacksonFeatures(serializationEnable = {SerializationFeature.INDENT_OUTPUT})
    @Path("/stoplist/{id}")
    @Override
    public StopList get(@PathParam("id") Long id) {
        return stopListService.findById(id);
    }

    @GET
    @Produces(HttpUtils.APPLICATION_JSON_UTF8)
    @Consumes(HttpUtils.APPLICATION_JSON_UTF8)
    @JacksonFeatures(serializationEnable = {SerializationFeature.INDENT_OUTPUT})
    @Path("/requestcount")
    public Long getTodayRequestCount(@QueryParam("startDate") Long startDate,
                                     @QueryParam("endDate") Long endDate) {
        RequestSearchCriteria criteria = new RequestSearchCriteria();
        if(startDate==null && endDate==null) {
            //По умолчанию берем сегодгня
            Date d = DateUtils.truncate(new Date(), Calendar.DATE);
            criteria.setFrom(new Date(System.currentTimeMillis() + 86400000));
            criteria.setTo(d);
        } else {
            criteria.setFrom(DateUtils.truncate(new Date(startDate), Calendar.DATE));
            Date endOfDay = new Date(endDate);
            endOfDay.setHours(23);
            endOfDay.setMinutes(59);
            endOfDay.setSeconds(59);
            criteria.setTo(endOfDay);
        }
        return requestService.countByCriteria(criteria);
    }

    /**
     * Список всех стоп листов
     *
     * @return Список всех стоп листов
     */
    @Path("/stoplist")
    @GET
    @Produces(HttpUtils.APPLICATION_JSON_UTF8)
    @Consumes(HttpUtils.APPLICATION_JSON_UTF8)
    @JacksonFeatures(serializationEnable = {SerializationFeature.INDENT_OUTPUT})
    @Override
    public Collection<StopList> list() {
        return stopListService.getAll();
    }

//    /**
//     * Получить стоп лист по ID
//     * @param id идентификатор.
//     * @return заявка по ID
//     */
//	/*
// @GET
// @Produces(MediaType.APPLICATION_JSON)
// @Consumes(MediaType.APPLICATION_JSON)
// @JacksonFeatures( serializationEnable =  { SerializationFeature.INDENT_OUTPUT })
// @Path("/stoplist/{id}")
// @Override
// public StopList get(@PathParam("id") Long id) {
// 	StopList t = super.get(id);
// 	if (t == null)
// 		throw new WebApplicationException("Entity not found for id: " + id,Response.Status.NOT_FOUND);
// 		//return Response.status(Response.Status.NOT_FOUND).entity("Entity not found for id: " + id).build();
// 		System.out.println("No such list:"+id);
//     return t;
// }
// */
//    @GET
//    @Produces(MediaType.APPLICATION_JSON)
//    @Consumes(MediaType.APPLICATION_JSON)
//    @JacksonFeatures( serializationEnable =  { SerializationFeature.INDENT_OUTPUT })
//    @Path("/stoplist/{id}")
//    public CompareServiceStopListElement getElement(@PathParam("id") Long id) {
//        StopList t = super.get(id);
//        if (t == null)
//            throw new WebApplicationException("Entity not found for id: " + id,Response.Status.NOT_FOUND);
//        //return Response.status(Response.Status.NOT_FOUND).entity("Entity not found for id: " + id).build();
//        System.out.println("No such list:"+id);
//
//        return this.listManager.getList(id);
//    }

    /**
     * Добавить стоп лист.
     *
     * @param entity добавляемая конфигурация.
     * @return Идентификатор добавленной заявки.
     */
    @Path("/stoplist")
    @POST
    @Produces(HttpUtils.APPLICATION_JSON_UTF8)
    @Consumes(HttpUtils.APPLICATION_JSON_UTF8)
    @JacksonFeatures(serializationEnable = {SerializationFeature.INDENT_OUTPUT})
    @Override
    public Long add(StopList entity) {
        log.debug("add stopList: {}", entity);
        Long id = super.add(entity);
        listManager.addList(entity);
        return id;
    }

    @Path("/stoplist")
    @PUT
    @Produces(HttpUtils.APPLICATION_JSON_UTF8)
    @Consumes(HttpUtils.APPLICATION_JSON_UTF8)
    @JacksonFeatures(serializationEnable = {SerializationFeature.INDENT_OUTPUT})
    public Long edit(StopList entity) {
        log.debug("edit stopList: {}", entity);
        stopListService.update(entity);
        listManager.addList(entity);
        return entity.getId();
    }

    /*
     * Добавить новый элемент к заданному ID списку
     */
    @Path("/stoplist/{ID}/entry")
    @PUT
    @Consumes(HttpUtils.APPLICATION_JSON_UTF8)
    @JacksonFeatures(serializationEnable = {SerializationFeature.INDENT_OUTPUT})
    public Response add(@PathParam("ID") Long id, StopListElement element) {
        log.debug("add stopList: {} listElement: {}", id, element);

        // создали документ и установили тип
        Document aDocument = new Document();
        aDocument.setDocumentType(new DocumentType(DocumentType.Type.STOP_LIST));
        StopList stopList = stopListService.findById(id);
        if (stopList == null) {
            log.warn("StopList: {} not found.");
            return Response.status(Status.NOT_FOUND).build();
        }

        // 1. сходить в Template builder, построить шаблон
        byte a[] = Base64.getDecoder().decode(element.getPhoto());
        PhotoTemplate scannedTemplate = templateBuilderServiceRestClient.getPhotoTemplate(a);

        // 2. фотку в хранилку
        String scannedPictureURL = photoServiceClient.putPhoto(element.getPhoto(), UUID.randomUUID().toString());
        aDocument.setFaceSquare(scannedPictureURL);
        aDocument.setDescription("Stop list element");
        aDocument.setOrigImageURL(null);

        // 3. добавить документ в Documents
        Long docId = documentService.save(aDocument);

        // 4. добавить шаблоны
        //aDocument.setId(docId);

        try {
            addBioTemplateToDocument(aDocument, scannedTemplate);
            listManager.addElement(id, aDocument);
        } catch (Exception exc) {
            throw new WebApplicationException("Unable to ad template to the document: " + id, Status.INTERNAL_SERVER_ERROR);
        }

        // 5. добавить документ в стоплист
        stopList.getOwner().add(aDocument);
        stopListService.saveOrUpdate(stopList);

        log.debug("Element: {} added to list: {} with template: {}", element, id, scannedTemplate);
        return Response.ok().header("Access-Control-Allow-Origin", "*").build();
    }

    /*
     * Удалить элемент из заданного списка по номеру списка и номеру элемента
     */
    @Path("/stoplist/{listId}/entry/{itemId}")
    @DELETE
    @JacksonFeatures(serializationEnable = {SerializationFeature.INDENT_OUTPUT})
    public Response delete(@PathParam("listId") Long listId, @PathParam("itemId") Long itemId) {
        log.debug("delete (item from stop list) list: {} item: {}");

        try {
            listManager.delStopListElement(listId, itemId);
            StopList stopList = stopListService.findById(listId);

            if (stopList != null) {
                Document delDocument = null;
                for (Document document : stopList.getOwner()) {
                    if (document.getId().equals(itemId)) {
                        delDocument = document;
                    }
                }
                if (delDocument != null) {
                    stopList.getOwner().remove(delDocument);
                    stopListService.saveOrUpdate(stopList);
                }
            }

            return Response.ok().build();
        } catch (Exception e) {
            log.error("Can't delete element from stop list.", e);
            return Response.serverError().build();
        }
    }

    private void addBioTemplateToDocument(Document document, PhotoTemplate scannedTemplate) throws IOException {
        log.debug("addBioTemplateToDocument document: {} scannedTemplate: {}", document, scannedTemplate);
        BioTemplate bioTemplate = new BioTemplate();
        bioTemplate.setInsUser("StopLists");
        bioTemplate.setTemplateVector(JsonUtils.serializeJson(scannedTemplate.template));
        bioTemplate.setDocument(document);
        BioTemplateVersion bioTemplateVersion = bioTemplateVersionService.findById((long) scannedTemplate.version);
        if (bioTemplateVersion == null) {
            bioTemplateVersion = new BioTemplateVersion();
            bioTemplateVersion.setId((long) scannedTemplate.version);
            bioTemplateVersion.setObjectDate(new Date());
            bioTemplateVersion.setDescription("Версия " + scannedTemplate.version);
            bioTemplateVersionService.saveOrUpdate(bioTemplateVersion);
        }
        bioTemplate.setBioTemplateVersion(bioTemplateVersion);
        BioTemplateType bioTemplateType = bioTemplateTypeService.findById((long) scannedTemplate.type);
        if (bioTemplateType == null) {
            bioTemplateType = new BioTemplateType();
            bioTemplateType.setId((long) scannedTemplate.type);
            bioTemplateType.setDescription("Новый тип " + scannedTemplate.type);
            bioTemplateTypeService.saveOrUpdate(bioTemplateType);
        }
        bioTemplate.setBioTemplateType(bioTemplateType);

        bioTemplateService.saveOrUpdate(bioTemplate);
    }
}