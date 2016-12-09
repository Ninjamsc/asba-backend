package com.technoserv.rest.model;
import java.util.ArrayList;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class CompareResponse  {

	private ArrayList<CompareResponseRulesObject> Rules;
	private CompareResponsePictureReport scannedPicture;
	private CompareResponsePictureReport cameraPicture;


public ArrayList<CompareResponseRulesObject> getRules() {
	return Rules;
}
public void setRules(ArrayList<CompareResponseRulesObject> rules) {
	Rules = rules;
}

@Override
public String toString() {
	return "CompareRequest  [scannedPicture=" + scannedPicture + ",cameraPicture="+cameraPicture+", Rules="+Rules+"]";
}
public CompareResponsePictureReport getScannedPicture() {
	return scannedPicture;
}
public void setScannedPicture(CompareResponsePictureReport scannedPicture) {
	this.scannedPicture = scannedPicture;
}
public CompareResponsePictureReport getCameraPicture() {
	return cameraPicture;
}
public void setCameraPicture(CompareResponsePictureReport cameraPicture) {
	this.cameraPicture = cameraPicture;
}
}
