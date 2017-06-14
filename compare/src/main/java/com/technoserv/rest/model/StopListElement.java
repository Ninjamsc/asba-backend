package com.technoserv.rest.model;


import com.google.common.base.MoreObjects;
import org.apache.commons.lang3.StringUtils;


public class StopListElement {

    private String photo;

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("photo length", StringUtils.length(photo))
                .toString();
    }
}
