package com.technoserv.rest.resources;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import javax.annotation.Resource;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
//import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response;

import com.technoserv.rest.client.PhotoPersistServiceRestClient;
import com.technoserv.db.model.configuration.SystemSettingsType;
import com.technoserv.db.service.configuration.impl.SystemSettingsBean;
import com.technoserv.rest.request.PhotoTemplate;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.glassfish.jersey.internal.util.Base64;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.jaxrs.annotation.JacksonFeatures;
import com.technoserv.db.model.objectmodel.BioTemplate;
import com.technoserv.db.model.objectmodel.BioTemplateType;
import com.technoserv.db.model.objectmodel.BioTemplateVersion;
import com.technoserv.db.model.objectmodel.Document;
import com.technoserv.db.model.objectmodel.DocumentType;
import com.technoserv.db.model.objectmodel.StopList;
import com.technoserv.db.service.Service;
import com.technoserv.db.service.objectmodel.api.BioTemplateService;
import com.technoserv.db.service.objectmodel.api.BioTemplateTypeService;
import com.technoserv.db.service.objectmodel.api.BioTemplateVersionService;
import com.technoserv.db.service.objectmodel.api.DocumentService;
import com.technoserv.db.service.objectmodel.api.PersonService;
import com.technoserv.db.service.objectmodel.api.StopListService;
import com.technoserv.rest.client.TemplateBuilderServiceRestClient;
import com.technoserv.utils.JsonUtils;
import com.technoserv.rest.comparator.CompareRule;
import com.technoserv.rest.comparator.CompareServiceStopListElement;
import com.technoserv.rest.model.CompareRequest;
import com.technoserv.rest.model.CompareResponse;
import com.technoserv.rest.model.CompareResponseBlackListObject;
import com.technoserv.rest.model.CompareResponsePictureReport;
import com.technoserv.rest.model.CompareResponseRulesObject;
import com.technoserv.rest.model.StopListElement;

import io.swagger.annotations.Api;


@Component
@Path("")
@Api(value = "Compare")
public class CompareResource extends BaseResource<Long,StopList> implements InitializingBean  {
	@Autowired
	private CompareListManager listManager;
	
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


    @Resource @Qualifier(value = "converters")
    private HashMap<String, String> compareRules;
    
    @Override
    protected Service<Long, StopList> getBaseService() {
        return stopListService;
    }
	
	@Override
	public void afterPropertiesSet() throws Exception {
        System.out.println("---------------------\nИницаиализация сервиса сравнения");
        //listManager = new CompareListManager(documentService);
        List<StopList> allLists = stopListService.getAll("owner","owner.bioTemplates");
        System.out.println("Number of stop lists is:"+allLists.size());
        for (StopList element : allLists)
        {
        	listManager.addList(element);
            System.out.println("name="+element.getStopListName()+" id="+element.getId()+" similarity="+element.getSimilarity());
            Iterator<Document> id = element.getOwner().iterator();
            while (id.hasNext())
            {
                Document d = id.next();
                //listManager.addElement(element.getId(), d);
                System.out.println(" doc_id="+d.getId());
                List<BioTemplate> blist = d.getBioTemplates();
                for(BioTemplate t: blist)
                {
                    ObjectMapper mapper = new ObjectMapper();
                    double[] array = mapper.readValue(t.getTemplateVector(), double[].class);
                    ArrayRealVector arv = new ArrayRealVector(array);
                    System.out.println("\tvector="+arv.toString());
                }
            }
        }        
        
        //Todo тут логика при старте приложенния
        //Фактически данный бин singleton и создаётся при старте приложения
        //this.listManager.compare("[0.020753301680088043, 0.04044751450419426, 0.13084986805915833, 0.059689026325941086, 0.15199658274650574, -0.15477296710014343, 0.10174626111984253, 0.09708714485168457, 0.05313556268811226, -0.003060600021854043, -0.027737755328416824, -0.09192277491092682, 0.024010395631194115, -0.061058711260557175, 0.046029217541217804, -0.1775650829076767, 0.07849898934364319, -0.04662546142935753, 0.07057669758796692, 0.08652392029762268, 0.0662936121225357, 0.03756343200802803, -0.04922910034656525, 0.03141292557120323, 0.08356206119060516, -0.1350899189710617, -0.20401452481746674, 0.10807696729898453, 0.04847602918744087, 0.030618079006671906, 0.1429709941148758, -0.08152655512094498, -0.05040675401687622, -0.0020790710113942623, 0.055735066533088684, -0.13398271799087524, -0.10305635631084442, 0.07367397099733353, -0.08056174218654633, 0.011274375021457672, 0.07565123587846756, 0.0006628360715694726, -0.14597639441490173, 0.06119852513074875, 0.10004027187824249, 0.023146357387304306, 0.11026181280612946, -0.0040884907357394695, -0.037800535559654236, 0.10853380709886551, -0.10188018530607224, -0.007757482118904591, -0.010982991196215153, 0.02395358681678772, 0.09733343869447708, -0.07628369331359863, 0.02649574540555477, 0.039745479822158813, -0.17949996888637543, 0.007255507633090019, 0.016987621784210205, 0.18098284304141998, -0.1025320366024971, -0.018973298370838165, 0.10926719754934311, 0.046763718128204346, 0.06041368842124939, 0.06826133280992508, 0.012208324857056141, -0.08431797474622726, 0.0037080312613397837, 0.20286907255649567, 0.03940027579665184, 0.03899642452597618, 0.014941767789423466, 0.17148782312870026, -0.0380895659327507, -0.019260890781879425, -0.19221830368041992, -0.03016120381653309, 0.08850887417793274, -0.012414127588272095, -0.004460429307073355, 0.04568830505013466, -0.12407730519771576, -0.1516706794500351, 0.0789976567029953, 0.04784555733203888, 0.0027379500679671764, -0.014989141374826431, -0.056807562708854675, -0.06459169834852219, -0.014003312215209007, 0.07949569821357727, -0.015189931727945805, 0.03384886682033539, 0.17762857675552368, -0.06609157472848892, -0.2093888819217682, -0.03780129551887512, 0.007640279829502106, -0.03648004308342934, 0.04399833083152771, 0.0316741056740284, 0.04010686278343201, 0.08813131600618362, -0.06487372517585754, -0.039993055164813995, 0.23097702860832214, -0.017569255083799362, -0.15538936853408813, 0.06488358229398727, 0.02634734846651554, -0.04101783409714699, -0.036427076905965805, 0.05655577406287193, -0.002288134302943945, -0.046763014048337936, -0.1639958918094635, 0.19583673775196075, 0.14950740337371826, -0.00714136241003871, -0.055665522813797, 0.043675411492586136, -0.018850823864340782, -0.01104491576552391, -0.04339916259050369, -0.03134331479668617]");
        String s = compareRules.get("scanAndWeb");
        
        Class cc = Class.forName(s);
        CompareRule r = (CompareRule)cc.newInstance();
        if (r!=null) r.doRule(new double[10] , new double[10] );
        System.out.println("Конец инициализации сервиса сравнения\n-------------------------");
	}

	public Long getCommonListId()
    {
        return new Long(systemSettingsBean.get(SystemSettingsType.COMPARATOR_COMMON_LIST_ID));
    }
	/*
	 * Сравнить картинки с блеклистами и досье и вернуть отчет
	 */
	@PUT
	@Path("/template")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response compareImages(CompareRequest message) {
		CompareResponse response = new CompareResponse();
		ArrayList<CompareResponseRulesObject> firedRules = new ArrayList<CompareResponseRulesObject>();
		try {
		    // compare scanned pic
			ArrayList<CompareResponseBlackListObject> ls = this.listManager.compare(message.getTemplate_scan());
			boolean isCommonS = listManager.compare(message.getTemplate_scan(),getCommonListId());
			CompareResponsePictureReport r1 = new CompareResponsePictureReport();
			r1.setBlackLists(ls);
			r1.setPictureURL(message.getScanFullFrameURL());
			r1.setPreviewURL(message.getScanPreviewURL());
			// compare webcam pic
			ArrayList<CompareResponseBlackListObject> lw = this.listManager.compare(message.getTemplate_web());
            boolean isCommonW = listManager.compare(message.getTemplate_scan(),getCommonListId());
			CompareResponsePictureReport r2 = new CompareResponsePictureReport();
			r2.setBlackLists(lw);
			r2.setPictureURL(message.getWebFullFrameURL());
			r2.setPreviewURL(message.getWebPreviewURL());
			response.setCameraPicture(r2);
			response.setScannedPicture(r1);
			response.setRules(new ArrayList<CompareResponseRulesObject>());//TODO (rplace with the rules
			if (ls.size() > 0 || lw.size() > 0)
			{
				CompareResponseRulesObject rule = new CompareResponseRulesObject();
				rule.setRuleId("4.2.3");
				//rule.setRuleName("Возможно соответствие с клиентом из банковского СТОП-ЛИСТА");
				rule.setRuleName("Perhaps photo is in stop-list.");
				firedRules.add(rule);
			}
            if (isCommonS || isCommonW)
            {
                CompareResponseRulesObject rule = new CompareResponseRulesObject();
                rule = new CompareResponseRulesObject();
                rule.setRuleId("4.2.4");
                //rule.setRuleName("Возможно соответствие с клиентом из общего СТОП-ЛИСТА");
		rule.setRuleName("Perhaps photo is in  common stop-list.");
                firedRules.add(rule);
            }
		} catch (Exception e) { throw new WebApplicationException(e,Response.Status.INTERNAL_SERVER_ERROR);}
		// сравнение 2 шаблонов на совпадение
		try {
			boolean similar = this.listManager.isSimilar(message.getTemplate_scan(), message.getTemplate_web());
			if (!similar)
			{
				CompareResponseRulesObject rule = new CompareResponseRulesObject();
				rule.setRuleId("4.2.5");
				//rule.setRuleName("Возможно несоответствие фотографии в паспорте и фотографии, прикрепленной к заявке.");
				rule.setRuleName("Perhaps the discrepancy in the passport photo and the photo from webcap.");
				firedRules.add(rule);
				}
		}catch (Exception e) { throw new WebApplicationException(e,Response.Status.INTERNAL_SERVER_ERROR);}
		// add fired rule
		response.setRules(firedRules);
		return Response.status(Response.Status.OK).entity(response).header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON+"; charset=UTF-8").build();
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
    @Override
    public Response update(StopList entity) {
        return super.update(entity);
    }
	*/
    /**
     * удалить стоп лист.
     * @param id удаляемой заявки.
     * @return ок
     */
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @JacksonFeatures(serializationEnable =  { SerializationFeature.INDENT_OUTPUT })
    @Path("/stoplist/{id}")
    @Override
    public Response delete(@PathParam("id") Long id) {
        return super.delete(id);
    }

 /**
  * Список всех стоп листов
  * @return Список всех стоп листов
  */
	@Path("/stoplist")
 @GET
 @Produces(MediaType.APPLICATION_JSON)
 @Consumes(MediaType.APPLICATION_JSON)
 @JacksonFeatures( serializationEnable =  { SerializationFeature.INDENT_OUTPUT })
 @Override
 public Collection<StopList> list() {
     return super.list();
 }

 /**
  * Получить стоп лист по ID
  * @param id идентификатор.
  * @return заявка по ID
  */
	/*
 @GET
 @Produces(MediaType.APPLICATION_JSON)
 @Consumes(MediaType.APPLICATION_JSON)
 @JacksonFeatures( serializationEnable =  { SerializationFeature.INDENT_OUTPUT })
 @Path("/stoplist/{id}")
 @Override
 public StopList get(@PathParam("id") Long id) {
 	StopList t = super.get(id);
 	if (t == null)
 		throw new WebApplicationException("Entity not found for id: " + id,Response.Status.NOT_FOUND);
 		//return Response.status(Response.Status.NOT_FOUND).entity("Entity not found for id: " + id).build();
 		System.out.println("No such list:"+id);
     return t;
 }
 */
@GET
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@JacksonFeatures( serializationEnable =  { SerializationFeature.INDENT_OUTPUT })@Path("/stoplist/{id}")
 public CompareServiceStopListElement getElement(@PathParam("id") Long id) {
 	StopList t = super.get(id);
 	if (t == null)
 		throw new WebApplicationException("Entity not found for id: " + id,Response.Status.NOT_FOUND);
 		//return Response.status(Response.Status.NOT_FOUND).entity("Entity not found for id: " + id).build();
 		System.out.println("No such list:"+id);
 		
     return this.listManager.getList(id);
 }

 /**
  * Добавить стоп лист.
  * @param entity добавляемая конфигурация.
  * @return Идентификатор добавленной заявки.
  */
	@Path("/stoplist")
 @PUT
 @Produces(MediaType.APPLICATION_JSON)
 @Consumes(MediaType.APPLICATION_JSON)
 @JacksonFeatures( serializationEnable =  { SerializationFeature.INDENT_OUTPUT })
 @Override
 public Long add(StopList entity) {
		Long id = super.add(entity);
		System.out.println("aded list id="+entity.getId());
		listManager.addList(entity);
		return id;
 }
	
	/*
	 * Добавить новый элемент к заданному ID списку
	 */
    @Path("/stoplist/{ID}/entry")
    @PUT
    //@Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @JacksonFeatures(serializationEnable =  { SerializationFeature.INDENT_OUTPUT })
    public Response add(@PathParam("ID")Long id, StopListElement element) {
    	// создали документ и установили тип
    	Document aDocument = new Document();
    	aDocument.setDocumentType(new DocumentType(DocumentType.Type.STOP_LIST));
        StopList stopList = stopListService.findById(id);
        if(stopList==null) {
              	System.out.println("StopList not found for ID="+id+" "+element);
              	return Response.status(404).build();
              }
        // 1. сходить в Template builder, построить шаблон
        //PhotoTemplate scannedTemplate = templateBuilderServiceRestClient.getPhotoTemplate(element.getPhoto().getBytes());
        StringBuilder sb = new StringBuilder();
        sb.append("data:image/jpg;base64,");
        sb.append(element.getPhoto());
        byte a[] = Base64.decode(sb.toString().getBytes());
        PhotoTemplate scannedTemplate = templateBuilderServiceRestClient.getPhotoTemplate(a);
        // 2. фотку в хранилку
        String scannedPictureURL = photoServiceClient.putPhoto(element.getPhoto(), UUID.randomUUID().toString());
        aDocument.setFaceSquare(scannedPictureURL);
        aDocument.setDescription("Stop list element");
        aDocument.setOrigImageURL(null);
        // 3. добавить документ в Documents
        Long docId = documentService.save(aDocument);
        // 4. добавить шшшаблоны
          //aDocument.setId(docId);
        try { addBioTemplateToDocument(aDocument, scannedTemplate);
            }
            catch (IOException exc) {
            	throw new WebApplicationException("Unable to ad template to the document: " + id,Response.Status.INTERNAL_SERVER_ERROR);
            }
        	// 5. добавить документ в стоплист
            stopList.getOwner().add(aDocument);
            stopListService.saveOrUpdate(stopList);
        	System.out.println("Adding list element for ID="+id+" "+element + " "+scannedTemplate);
        	return Response.ok().build();    
    }

	/*
	 * Удалить элемент из заданного списка по номеру списка и номеру элемента
	 */
    @Path("/stoplist/{listId}/entry/{itemId}")
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @JacksonFeatures(serializationEnable =  { SerializationFeature.INDENT_OUTPUT })
    public void delete(@PathParam("listId")Long listId,@PathParam("itemId")Long itemId) {

        //todo write native sql

        StopList stopList = stopListService.findById(listId);
        if(stopList!=null) {
            Document delDocument = null;
            for (Document document : stopList.getOwner()) {
                if(document.getId().equals(itemId)) {
                    delDocument = document;
                }
            }
            if(delDocument!=null) {
                stopList.getOwner().remove(delDocument);
                stopListService.saveOrUpdate(stopList);
            }
        }

    }

    private void addBioTemplateToDocument(Document document, PhotoTemplate scannedTemplate) throws IOException {

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