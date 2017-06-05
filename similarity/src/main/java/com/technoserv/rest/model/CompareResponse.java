package com.technoserv.rest.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.google.common.base.MoreObjects;

public class CompareResponse {

    @JsonInclude(Include.NON_EMPTY)
    private String pictureAURL;

    private String pictureBURL;

    private Double similarity;

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

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("pictureAURL", pictureAURL)
                .add("pictureBURL", pictureBURL)
                .add("similarity", similarity)
                .toString();
    }
}
