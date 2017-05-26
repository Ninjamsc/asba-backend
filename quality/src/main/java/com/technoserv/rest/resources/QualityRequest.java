
package com.technoserv.rest.resources;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class QualityRequest {
    private String photos;

    public String getPhotos() {
        return photos;
    }

    public void setPhotos(String photos) {
        this.photos = photos;
    }

    @Override
    public String toString() {
        return "QualityRequest  [photos=" + photos + "]";
    }
}
