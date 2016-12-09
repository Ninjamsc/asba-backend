package com.technoserv.rest.comparator;

import java.util.ArrayList;

import org.apache.commons.math3.linear.ArrayRealVector;


import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.technoserv.db.model.objectmodel.Document;

public class CompareServiceStopListElement {
	private String listName;
	private Long id;
	private Double similarity;
	ArrayList<CompareServiceStopListVector> vectors;
	
public 	CompareServiceStopListElement(String listName, Long id, Double similarity)
{
	this.vectors = new ArrayList<CompareServiceStopListVector>();
	this.setId(id);
	this.setListName(listName);
	this.setSimilarity(similarity);
}

public boolean addVector(Document doc) 
{
	if(vectors == null) this.vectors = new ArrayList<CompareServiceStopListVector>();
	CompareServiceStopListVector v = new CompareServiceStopListVector();
	ObjectMapper mapper = new ObjectMapper();
	try {
	 double[] array = mapper.readValue(doc.getBioTemplates().get(0).getTemplateVector(), double[].class);
	 v.setVector(new ArrayRealVector(array));
	 v.setDocId(doc.getId());
	 vectors.add(v);
	}
	catch (Exception e)
	{
		System.out.println(e);
		return false;
	}
	 return true;
}

public void setVectors(ArrayList<CompareServiceStopListVector> vector)
{
	this.vectors = vector;
}

public ArrayList<CompareServiceStopListVector> getVectors()
{
	return vectors;
}

public Long getId() {
	return id;
}

public void setId(Long id) {
	this.id = id;
}

public Double getSimilarity() {
	return similarity;
}

public void setSimilarity(Double similarity) {
	this.similarity = similarity;
}

public String getListName() {
	return listName;
}

public void setListName(String listName) {
	this.listName = listName;
}


}

