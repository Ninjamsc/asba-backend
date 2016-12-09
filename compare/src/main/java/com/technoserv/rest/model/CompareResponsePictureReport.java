package com.technoserv.rest.model;
import java.util.ArrayList;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class CompareResponsePictureReport  {
	
private String pictureURL;
private String previewURL;
private ArrayList<CompareResponseBlackListObject> BlackLists;


public ArrayList<CompareResponseBlackListObject> getBlackLists() {
	return BlackLists;
}
public void setBlackLists(ArrayList<CompareResponseBlackListObject> blackLists) {
	BlackLists = blackLists;
}

@Override
public String toString() {
	return "CompareResponsePictureReport  [BlackLists=" + BlackLists + "]";
}
public String getPictureURL() {
	return pictureURL;
}
public void setPictureURL(String pictureURL) {
	this.pictureURL = pictureURL;
}
public String getPreviewURL() {
	return previewURL;
}
public void setPreviewURL(String previewURL) {
	this.previewURL = previewURL;
}
}
