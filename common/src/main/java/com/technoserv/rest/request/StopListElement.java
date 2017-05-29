package com.technoserv.rest.request;

import com.google.common.base.MoreObjects;

//TODO DTO from Compare module ...
public class StopListElement {

    private String Photo;

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
