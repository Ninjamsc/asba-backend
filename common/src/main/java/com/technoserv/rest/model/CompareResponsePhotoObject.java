package com.technoserv.rest.model;

import com.google.common.base.MoreObjects;

public class CompareResponsePhotoObject {

    private String url;

    private double similarity;

    private Long identity;


    public Long getIdentity() {
        return identity;
    }

    public void setIdentity(Long identity) {
        this.identity = identity;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public double getSimilarity() {
        return similarity;
    }

    public void setSimilarity(double similarity) {
        this.similarity = similarity;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("url", url)
                .add("similarity", similarity)
                .add("identity", identity)
                .toString();
    }
}
