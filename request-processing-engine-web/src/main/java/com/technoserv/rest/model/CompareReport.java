package com.technoserv.rest.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.technoserv.db.model.objectmodel.Document;

import java.util.ArrayList;

public class CompareReport {

    @JsonInclude(Include.NON_EMPTY)
    private ArrayList<CompareResponseRulesObject> Rules;
    private CompareResponsePictureReport scannedPicture;
    private CompareResponsePictureReport cameraPicture;
    private CompareResponseDossierReport othernessPictures;
    private CompareResponseDossierReport similarPictures;
    private Double picSimilarity;
    private Double picSimilarityThreshold;
    // injected after request processing
    private Long wfNumber;
    @JsonProperty("IIN")
    private Long IIN;
    private String username;
    private String timestamp;
    @JsonProperty("created-at")
    private String createdAt;
    private String scannedPicturePreviewURL;
    private String webCamPicturePreviewURL;
    private String scannedPictureURL;
    private String webCamPictureURL;


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
    public Long getWfNumber() {
        return wfNumber;
    }

    public void setWfNumber(Long wfNumber) {
        this.wfNumber = wfNumber;
    }

    public Long getIIN() {
        return IIN;
    }

    public void setIIN(Long IIN) {
        this.IIN = IIN;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getScannedPicturePreviewURL() {
        return scannedPicturePreviewURL;
    }

    public void setScannedPicturePreviewURL(String scannedPicturePreviewURL) {
        this.scannedPicturePreviewURL = scannedPicturePreviewURL;
    }

    public String getWebCamPicturePreviewURL() {
        return webCamPicturePreviewURL;
    }

    public void setWebCamPicturePreviewURL(String webCamPicturePreviewURL) {
        this.webCamPicturePreviewURL = webCamPicturePreviewURL;
    }

    public String getScannedPictureURL() {
        return scannedPictureURL;
    }

    public void setScannedPictureURL(String scannedPictureURL) {
        this.scannedPictureURL = scannedPictureURL;
    }

    public String getWebCamPictureURL() {
        return webCamPictureURL;
    }

    public void setWebCamPictureURL(String webCamPictureURL) {
        this.webCamPictureURL = webCamPictureURL;
    }
    @Override
    public String toString() {
        return "CompareRequest  [scannedPicture=" + scannedPicture + ",cameraPicture="+cameraPicture+", Rules="+Rules+"]";
    }
}
