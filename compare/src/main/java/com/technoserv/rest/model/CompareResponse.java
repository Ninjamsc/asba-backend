package com.technoserv.rest.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.technoserv.db.model.objectmodel.Document;

import java.util.ArrayList;

public class CompareResponse {

	@JsonInclude(Include.NON_EMPTY)
	private ArrayList<CompareResponseRulesObject> Rules;
	private CompareResponsePictureReport scannedPicture;
	private CompareResponsePictureReport cameraPicture;
	private CompareResponseDossierReport othernessPictures;
	private CompareResponseDossierReport similarPictures;
	private Double picSimilarity;
	private Double picSimilarityThreshold;

	public Double getPicSimilarity() {return picSimilarity;}
	public void setPicSimilarity(Double picSimilarity) {this.picSimilarity = picSimilarity;}
	public Double getPicSimilarityThreshold() {return picSimilarityThreshold;}
	public void setPicSimilarityThreshold(Double picSimilarityThreshold) {this.picSimilarityThreshold = picSimilarityThreshold;}
	public CompareResponseDossierReport getSimilarPictures() {
		return similarPictures;
	}
	public void setSimilarPictures(CompareResponseDossierReport similarPictures) {this.similarPictures = similarPictures;}
	public CompareResponseDossierReport getOthernessPictures() {
		return othernessPictures;
	}
	public void setOthernessPictures(CompareResponseDossierReport othernessPictures) {this.othernessPictures = othernessPictures;}
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
}
