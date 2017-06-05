package com.technoserv.rest.model;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;

public class SkudCompareRequest {

    @JsonProperty("template")
    private double[] template;

    @JsonProperty("pictureURL")
    private String pictureURL;

    public String getPictureURL() {
        return pictureURL;
    }

    public void setPictureURL(String pictureURL) {
        this.pictureURL = pictureURL;
    }

    public double[] getTemplate() {
        return template;
    }

    public void setTemplate(double[] template) {
        this.template = template;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("template", template.length)
                .add("pictureURL", pictureURL)
                .toString();
    }

}