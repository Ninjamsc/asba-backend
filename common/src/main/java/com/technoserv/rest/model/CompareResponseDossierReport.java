package com.technoserv.rest.model;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;

public class CompareResponseDossierReport {
	
	private ArrayList<CompareResponsePhotoObject> photos;
	private Double similarity;


	public Double getSimilarity() {
		return similarity;
	}

	public void setSimilarity(Double similarity) {
		this.similarity = similarity;
	}

	public ArrayList<CompareResponsePhotoObject> getPhotos() {
	return photos;
}

	public void setPhotos(ArrayList<CompareResponsePhotoObject> photos) {
	this.photos = photos;
}

@Override
public String toString() {
	return "CompareResponseDossierReport  [photos=" + photos + "]";
}
}
