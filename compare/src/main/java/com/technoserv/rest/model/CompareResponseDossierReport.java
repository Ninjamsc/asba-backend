package com.technoserv.rest.model;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;

public class CompareResponseDossierReport {
	
	private ArrayList<CompareResponsePhotoObject> photos;
	private Long similarity;


	public Long getSimilarity() {
		return similarity;
	}

	public void setSimilarity(Long similarity) {
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
