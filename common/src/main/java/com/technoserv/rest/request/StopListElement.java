package com.technoserv.rest.request;

import com.google.common.base.MoreObjects;
import org.apache.commons.lang.StringUtils;

//TODO DTO from Compare module ...
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
                .add("photo", StringUtils.length(photo))
                .toString();
    }
}
