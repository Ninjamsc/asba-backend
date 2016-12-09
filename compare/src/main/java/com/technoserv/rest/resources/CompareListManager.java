package com.technoserv.rest.resources;



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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.technoserv.db.model.objectmodel.Document;
import com.technoserv.db.model.objectmodel.StopList;
import com.technoserv.db.service.objectmodel.api.DocumentService;
import com.technoserv.rest.comparator.CompareServiceStopListElement;
import com.technoserv.rest.comparator.CompareServiceStopListVector;
import com.technoserv.rest.model.CompareResponseBlackListObject;
import com.technoserv.rest.model.CompareResponsePhotoObject;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

//@Component
//@PropertySource("classpath:compare-service.properties")



public class CompareListManager {

	//@Resource @Qualifier(value = "converters")
	private HashMap<String, String> compareRules;

	//@Autowired
     Environment env;
	 
    //@Value("${com.technoserv.bio.kernel.compare.multiplier}")
    private static String _mult;
    private double mult = 0.7f; //TODO from properties
    //@Value("${com.technoserv.bio.kernel.compare.power}")
    private static String _power;
    private int power = 4; //TODO from properties
    //@Value("${com.technoserv.bio.kernel.compare.similarity}")
    private static String _similarity;
    private double similarity = 0.00000123123d; //TODO from properties

    private static final Log log = LogFactory.getLog(CompareListManager.class);

    @Autowired
    private DocumentService documentService;    
    
	private HashMap<Long,CompareServiceStopListElement> managedStopLists;
	
	public CompareServiceStopListElement getList(Long id)
	{
		return this.managedStopLists.get(id);
	}
	public CompareListManager(DocumentService service) throws FileNotFoundException,IOException {
		this.managedStopLists = new HashMap<Long,CompareServiceStopListElement>();
		this.documentService = service;
		//Properties p = new Properties();
		//InputStream i = new FileInputStream("compare-service.properties");
		//p.load(i);
		
    	//this.mult = new Double(_mult);
    	//this.power = new Integer(_power);
    	//this.similarity = new Double(_similarity);
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

	public ArrayList<CompareResponseBlackListObject> compare(String vector) throws Exception
	{
		ObjectMapper mapper = new ObjectMapper();
		double[] array = mapper.readValue(vector, double[].class);
		return compare(array);
	}
	
	public ArrayList<CompareResponseBlackListObject> compare(double[] vector) throws Exception //TODO: specify exception
	{
            log.info("COMPARATOR vector is ='"+vector+"'");
            System.out.println("COMPARATOR vector is'"+vector+"'");

		//double mult = 0.7f;
		//int power = 4;
		ArrayList<CompareResponseBlackListObject> bl = new ArrayList<CompareResponseBlackListObject>();
		// Сериализуем строковое представление вектора в ArrayRealVector
		//ObjectMapper mapper = new ObjectMapper();
		//double[] array = mapper.readValue(vector, double[].class);
        //ArrayRealVector arv = new ArrayRealVector(array);
		ArrayRealVector arv = new ArrayRealVector(vector);
        Iterator<Map.Entry<Long,CompareServiceStopListElement>> it = managedStopLists.entrySet().iterator();
        while (it.hasNext())
        {
        	CompareServiceStopListElement list = it.next().getValue();
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
            		System.out.println(list.getListName()+" norm:"+norm+" similarity:"+similarity);
        			Long doc = vect.getDocId();
        			Document d = this.documentService.findById(doc);
        			CompareResponsePhotoObject po = new CompareResponsePhotoObject();
        			po.setUrl(d.getFaceSquare());
        			po.setSimilarity(norm);
        			report.addPhoto(po);
        			bl.add(report);
        				
        		}
        		System.out.println(list.getListName()+" norm:"+norm);
        		
        	}
        }
      return bl;  
	}
}
