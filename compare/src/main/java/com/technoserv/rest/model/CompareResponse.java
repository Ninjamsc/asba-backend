package com.technoserv.rest.model;
import java.util.ArrayList;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

public class CompareResponse  {

	@JsonInclude(Include.NON_EMPTY)
	private ArrayList<CompareResponseRulesObject> Rules;
	private CompareResponsePictureReport scannedPicture;
	private CompareResponsePictureReport cameraPicture;
	private CompareResponseDossierReport othernessPictures;
	private CompareResponseDossierReport similarPictures;

	public CompareResponseDossierReport getSimilarPictures() {
		return similarPictures;
	}

	public void setSimilarPictures(CompareResponseDossierReport similarPictures) {
		this.similarPictures = similarPictures;
	}

	public CompareResponseDossierReport getOthernessPictures() {
		return othernessPictures;
	}

	public void setOthernessPictures(CompareResponseDossierReport othernessPictures) {
		this.othernessPictures = othernessPictures;
	}



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
