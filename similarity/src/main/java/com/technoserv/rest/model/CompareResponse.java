package com.technoserv.rest.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

public class CompareResponse {

    public String getPictureAURL() {
        return pictureAURL;
    }

    public void setPictureAURL(String pictureAURL) {
        this.pictureAURL = pictureAURL;
    }

    public String getPictureBURL() {
        return pictureBURL;
    }

    public void setPictureBURL(String pictureBURL) {
        this.pictureBURL = pictureBURL;
    }

    public Double getSimilarity() {
        return similarity;
    }

    public void setSimilarity(Double similarity) {
        this.similarity = similarity;
    }

    @JsonInclude(Include.NON_EMPTY)

    private String pictureAURL;
    private String pictureBURL;
    private Double similarity;


    @Override
    public String toString() {
        return "CompareResponse [pictureA URL=" + pictureAURL + ", pictureB URL=" + pictureBURL + "]";
    }
}
