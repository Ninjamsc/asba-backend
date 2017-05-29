package com.technoserv.rest.model;

import com.google.common.base.MoreObjects;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement
public class CompareResponsePictureReport {

    private String pictureURL;

    private String previewURL;

    private List<CompareResponseBlackListObject> BlackLists;


    public List<CompareResponseBlackListObject> getBlackLists() {
        return BlackLists;
    }

    public void setBlackLists(ArrayList<CompareResponseBlackListObject> blackLists) {
        BlackLists = blackLists;
    }

    public String getPictureURL() {
        return pictureURL;
    }

    public void setPictureURL(String pictureURL) {
        this.pictureURL = pictureURL;
    }

    public String getPreviewURL() {
        return previewURL;
    }

    public void setPreviewURL(String previewURL) {
        this.previewURL = previewURL;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("pictureURL", pictureURL)
                .add("previewURL", previewURL)
                .add("BlackLists", BlackLists)
                .toString();
    }
}
