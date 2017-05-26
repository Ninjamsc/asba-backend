package com.technoserv.rest.model;


import com.google.common.base.MoreObjects;

public class StopListElement {

    private String Photo;

    public StopListElement(String photo) {
        Photo = photo;
    }

    public StopListElement() {
        this("");
    }

    public String getPhoto() {
        return Photo;
    }

    public void setPhoto(String photo) {
        Photo = photo;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("Photo", Photo)
                .toString();
    }
}
