package com.technoserv.rest.model;

import com.google.common.base.MoreObjects;

import java.util.List;

public class CompareResponseDossierReport {

    private List<CompareResponsePhotoObject> photos;

    private Double similarity;

    public Double getSimilarity() {
        return similarity;
    }

    public void setSimilarity(Double similarity) {
        this.similarity = similarity;
    }

    public List<CompareResponsePhotoObject> getPhotos() {
        return photos;
    }

    public void setPhotos(List<CompareResponsePhotoObject> photos) {
        this.photos = photos;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("photos", photos)
                .add("similarity", similarity)
                .toString();
    }
}
