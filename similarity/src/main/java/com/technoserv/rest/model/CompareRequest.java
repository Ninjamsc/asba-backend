package com.technoserv.rest.model;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;

public class CompareRequest {

    @JsonProperty("pictureA")
    private String pictureA;

    @JsonProperty("pictureB")
    private String pictureB;

    public String getPictureB() {
        return pictureB;
    }

    public void setPictureB(String pictureB) {
        this.pictureB = pictureB;
    }

    public String getPictureA() {
        return pictureA;
    }

    public void setPictureA(String pictureA) {
        this.pictureA = pictureA;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("pictureA", pictureA)
                .add("pictureB", pictureB)
                .toString();
    }

}
