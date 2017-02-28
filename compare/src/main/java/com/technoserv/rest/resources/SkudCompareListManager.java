package com.technoserv.rest.resources;

import com.technoserv.db.model.configuration.SystemSettingsType;
import com.technoserv.db.model.objectmodel.Document;
import com.technoserv.db.model.objectmodel.StopList;
import com.technoserv.db.service.configuration.impl.SystemSettingsBean;
import com.technoserv.db.service.objectmodel.api.DocumentService;
import com.technoserv.rest.comparator.CompareServiceStopListElement;
import com.technoserv.rest.comparator.CompareServiceStopListVector;
import com.technoserv.rest.model.CompareResponseBlackListObject;
import com.technoserv.rest.model.CompareResponsePhotoObject;
import com.technoserv.rest.model.SelfCompareResult;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.math3.analysis.function.Exp;
import org.apache.commons.math3.analysis.function.Pow;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class SkudCompareListManager implements InitializingBean  {

	//@Autowired
	Environment env;
	//@Value("${com.technoserv.bio.kernel.compare.commonlist}")

	@Autowired
	private SystemSettingsBean systemSettingsBean;


	//private String _commonList;

	private static final Log log = LogFactory.getLog(SkudCompareListManager.class);

	@Autowired
	private DocumentService documentService;

	private HashMap<Long,CompareServiceStopListElement> managedStopLists;


	public boolean addList(StopList list)
	{
		log.info("addStopList(): Removing list id="+list.getId());

		if (list == null) return false;
		CompareServiceStopListElement e = new CompareServiceStopListElement(list.getStopListName(),list.getId(),list.getSimilarity());
		if(list.getOwner()!=null) {
			Iterator<Document> id = list.getOwner().iterator();
			while (id.hasNext()) {
				Document d = id.next();
				if (!e.addVector(d)) return false;
			}
		}
		managedStopLists.put(list.getId(), e);
		return true;
	}


	/*
 * Добавление элемента в существующий список по его ID
 */
	public void addElement (Long listId, Document vector) throws Exception
	{
		log.info("addElement(): adding element id="+vector.getId()+" to list id="+listId);
		if(vector.getId() == null) {
			log.error("addElement(): null document id. ignoring for the list_id="+listId);

		}
		CompareServiceStopListElement sl = managedStopLists.get(listId);
		if (sl != null) sl.addVector(vector);
	}

	// compare with exact list
	public CompareResponseBlackListObject compare1(double[] vector, Long listId) throws Exception //TODO: specify exception
	{
		log.info("compare(double, Long) list size is "+managedStopLists.size());
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
				log.debug("compare1 HIT" + list.getListName() + " norm:" + norm + " similarity:" + list.getSimilarity() + "doc=" + vect.getDocId());
				Long doc = vect.getDocId();
				Document d = this.documentService.findById(doc);
				CompareResponsePhotoObject po = new CompareResponsePhotoObject();
				po.setUrl(d.getFaceSquare());
				po.setSimilarity(norm);
				po.setIdentity(d.getId());
				report.addPhoto(po);
			}
			else
				log.debug("compare1 MISS " + list.getListName()+" norm:" + norm + " similarity:" + list.getSimilarity() + " doc="+vect.getDocId());
		}
		if (report.getPhoto().size() > 0) return report;
		return null;
	}

	// compare with all list except specified
	public ArrayList<CompareResponseBlackListObject> compare2(double[] vector, Long listId) throws Exception //TODO: specify exception
	{
		//log.info("COMPARATOR vector is ='"+vector+"'");
		//System.out.println("COMPARATOR vector is'"+vector+"'");

		log.info("compare(double) list size is "+managedStopLists.size());
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
					log.debug(list.getListName()+"compare2 HITT norm:"+norm+" similarity:"+similarity+" list id="+list.getId()+" doc_id="+vect.getDocId());
					Long doc = vect.getDocId();
					Document d = this.documentService.findById(doc);
					CompareResponsePhotoObject po = new CompareResponsePhotoObject();
					po.setUrl(d.getFaceSquare());
					po.setSimilarity(norm);
					report.addPhoto(po);
					bl.add(report);
				}
				else
					log.debug(list.getListName()+" compare2 MISS norm:"+norm+" similarity:"+similarity+" list id="+list.getId()+" doc_id="+vect.getDocId());
			}
		}
		return bl;
	}

	public SelfCompareResult isSimilar(double[] a1, double[] a2) {
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
		SelfCompareResult result = new SelfCompareResult();
		if (norm < similarity) result.setSimilar(false);
		else result.setSimilar(true);
		result.setThreshold(similarity);
		result.setSimilarity(norm);
		return result;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		//this._commonList= this.systemSettingsBean.get(SystemSettingsType.COMPARATOR_COMMON_LIST_ID);
		this.managedStopLists = new HashMap<Long,CompareServiceStopListElement>();
		//log.info("++++++++++++++++++++++++++++++++++++++++++++++++++");
		//log.info("list="+ _commonList);
		//log.info("++++++++++++++++++++++++++++++++++++++++++++++++++");

	}
}
