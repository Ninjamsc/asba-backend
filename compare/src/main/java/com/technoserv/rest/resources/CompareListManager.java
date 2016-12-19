package com.technoserv.rest.resources;
import com.technoserv.rest.client.PhotoPersistServiceRestClient;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.math3.analysis.function.Exp;
import org.apache.commons.math3.analysis.function.Pow;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.technoserv.db.model.configuration.SystemSettingsType;
import com.technoserv.db.model.objectmodel.Document;
import com.technoserv.db.model.objectmodel.StopList;
import com.technoserv.db.service.configuration.impl.SystemSettingsBean;
import com.technoserv.db.service.objectmodel.api.DocumentService;
import com.technoserv.rest.comparator.CompareServiceStopListElement;
import com.technoserv.rest.comparator.CompareServiceStopListVector;
import com.technoserv.rest.comparator.RuleResult;
import com.technoserv.rest.model.CompareResponseBlackListObject;
import com.technoserv.rest.model.CompareResponsePhotoObject;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

@Component
public class CompareListManager implements InitializingBean  {


	//@Resource @Qualifier(value = "converters")
	private HashMap<String, String> compareRules;

	//@Autowired
     Environment env;
     //@Value("${com.technoserv.bio.kernel.compare.commonlist}")
     
     @Autowired
     private SystemSettingsBean systemSettingsBean;
     
     
     private String _commonLIst;
	 
    private static final Log log = LogFactory.getLog(CompareListManager.class);

    @Autowired
    private DocumentService documentService;    
    
	private HashMap<Long,CompareServiceStopListElement> managedStopLists;
	
	public CompareServiceStopListElement getList(Long id)
	{
		return this.managedStopLists.get(id);
	}
	
	
	/*
	 * Добавление элемента в существующий список по его ID
	 */
	public void addElement (Long listId, Document vector) throws Exception
	{
		CompareServiceStopListElement sl = managedStopLists.get(listId);
		if (sl != null) sl.addVector(vector);
	}

	/*
	 * удаление списка по его ID
	 */
	public void delElement(Long listId)
	{
		if ( managedStopLists.get(listId) == null) {
			log.debug("++++++++++++++");
			log.debug("list id="+listId +" is absent");
			return;
		}
		managedStopLists.remove(listId);
	}
	/*
	 * Добавление нового стоплиста
	 */
	public boolean addList(StopList list) 
	{
		if (list == null) return false;
		CompareServiceStopListElement e = new CompareServiceStopListElement(list.getStopListName(),list.getId(),list.getSimilarity());
		Iterator<Document> id = list.getOwner().iterator();
		 while (id.hasNext())
         {
             Document d = id.next();
             if (!e.addVector(d)) return false;
         }		
		 managedStopLists.put(list.getId(), e);
		 return true;
	}
	/*	public ArrayList<CompareResponseBlackListObject> compare(String vector) throws Exception
	{
		ObjectMapper mapper = new ObjectMapper();
		double[] array = mapper.readValue(vector, double[].class);
		return compare(array);
	}
*/
    public CompareResponseBlackListObject compare1(double[] vector, Long listId) throws Exception //TODO: specify exception
    {
    	log.debug("compare(double, Long) list size is "+managedStopLists.size());
        double mult = new Double(systemSettingsBean.get(SystemSettingsType.COMPARATOR_MULTIPLIER));
        int power = new Integer(systemSettingsBean.get(SystemSettingsType.COMPARATOR_POWER));

		ArrayRealVector arv = new ArrayRealVector(vector);
		CompareServiceStopListElement list = managedStopLists.get(listId);
		CompareResponseBlackListObject report = new CompareResponseBlackListObject();
		if(list == null)
		{
			log.error("List id="+listId+" is null or unavailable");
			return null;
		}
		report.setListId(list.getId());
		report.setListName(list.getListName());
		report.setSimilarity(list.getSimilarity());
		ArrayList<CompareServiceStopListVector> vectors = list.getVectors();
		for ( CompareServiceStopListVector vect : vectors)
            {
                ArrayRealVector diff = arv.subtract(vect.getVector());
                double dot = diff.dotProduct(diff);
                double norm = 1 / new Exp().value(new Pow().value(mult*dot, power));
                if (norm > list.getSimilarity()) //HIT
                {
					log.debug("compare1 HITT" + list.getListName() + " norm:" + norm + " similarity:" + list.getSimilarity() + "doc=" + vect.getDocId());
					Long doc = vect.getDocId();
					Document d = this.documentService.findById(doc);
					CompareResponsePhotoObject po = new CompareResponsePhotoObject();
					po.setUrl(d.getFaceSquare());
					po.setSimilarity(norm);
					report.addPhoto(po);
                }
                else
					log.debug("compare1 MISS" + list.getListName()+" norm:" + norm + " similarity:" + list.getSimilarity() + "doc="+vect.getDocId());
            }
        if (report.getPhoto().size() > 0) return report;
		return null;
    }


	public ArrayList<CompareResponseBlackListObject> compare2(double[] vector, Long listId) throws Exception //TODO: specify exception
	{
            //log.info("COMPARATOR vector is ='"+vector+"'");
            //System.out.println("COMPARATOR vector is'"+vector+"'");

		log.debug("compare(double) list size is "+managedStopLists.size());
		double mult = new Double(systemSettingsBean.get(SystemSettingsType.COMPARATOR_MULTIPLIER));
    		int power = new Integer(systemSettingsBean.get(SystemSettingsType.COMPARATOR_POWER));
		ArrayList<CompareResponseBlackListObject> bl = new ArrayList<CompareResponseBlackListObject>();
		// Сериализуем строковое представление вектора в ArrayRealVector
		ArrayRealVector arv = new ArrayRealVector(vector);
        Iterator<Map.Entry<Long,CompareServiceStopListElement>> it = managedStopLists.entrySet().iterator();
        while (it.hasNext())
        {
        	CompareServiceStopListElement list = it.next().getValue();
        	log.debug("!!!! "+list.getId()+"="+listId);
			if(list.getId().longValue() == listId.longValue()) {
				log.debug("skipping common list id="+listId);
				continue;
			}
        	Double similarity = list.getSimilarity();
        	CompareResponseBlackListObject report = new CompareResponseBlackListObject();
        	report.setListId(list.getId());
        	report.setListName(list.getListName());
        	report.setSimilarity(list.getSimilarity());
        	ArrayList<CompareServiceStopListVector> vectors = list.getVectors();
        	for ( CompareServiceStopListVector vect : vectors)
        	{
        		ArrayRealVector diff =arv.subtract(vect.getVector());
        		double dot = diff.dotProduct(diff);
        		double norm = 1 / new Exp().value(new Pow().value(mult*dot, power));
        		if (norm > similarity) //HIT
        		{
					log.debug(list.getListName()+"compare(double, Long) HITT norm:"+norm+" similarity:"+similarity+" list id="+list.getId()+" doc_id="+vect.getDocId());
        			Long doc = vect.getDocId();
        			Document d = this.documentService.findById(doc);
        			CompareResponsePhotoObject po = new CompareResponsePhotoObject();
        			po.setUrl(d.getFaceSquare());
        			po.setSimilarity(norm);
        			report.addPhoto(po);
        			bl.add(report);        				
        		}
        		else
					log.debug(list.getListName()+" compare(double, Long) MISS norm:"+norm+" similarity:"+similarity+" list id="+list.getId()+" doc_id="+vect.getDocId());
        	}
        }
      return bl;  
	}
	
	public boolean isSimilar(double[] a1, double[] a2) {
		double similarity= new Double(systemSettingsBean.get(SystemSettingsType.RULE_SELF_SIMILARITY));
		double mult = new Double(systemSettingsBean.get(SystemSettingsType.COMPARATOR_MULTIPLIER));
		int power = new Integer(systemSettingsBean.get(SystemSettingsType.COMPARATOR_POWER));
		ArrayRealVector v1 = new ArrayRealVector(a1);
		ArrayRealVector v2 = new ArrayRealVector(a2);
   		ArrayRealVector diff =v1.subtract(v2);
		double dot = diff.dotProduct(diff);
		double norm = 1 / new Exp().value(new Pow().value(mult*dot, power));
		log.info("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
		log.info("SIMILARITY="+norm+" THRESHOLD+"+similarity);
		log.info("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
		
		if (norm < similarity) return false;
		return true;
}

	@Override
	public void afterPropertiesSet() throws Exception {
		this._commonLIst = this.systemSettingsBean.get(SystemSettingsType.COMPARATOR_COMMON_LIST_ID);
		this.managedStopLists = new HashMap<Long,CompareServiceStopListElement>();
		log.info("++++++++++++++++++++++++++++++++++++++++++++++++++");
		log.info("list="+ _commonLIst);
		log.info("++++++++++++++++++++++++++++++++++++++++++++++++++");
		
	}
}
