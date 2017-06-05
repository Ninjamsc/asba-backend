package com.technoserv.rest.model;

import java.util.ArrayList;
import java.util.List;

public class CompareResponseBlackListObject {

    private Long listId; // list identifier

    private String listName; // list name

    private List<CompareResponsePhotoObject> photo; // array of photos (templates) corresponding to the list

    private Double similarity; //

    public CompareResponseBlackListObject() {
        photo = new ArrayList<CompareResponsePhotoObject>();
    }

    public void addPhoto(CompareResponsePhotoObject photo) {
        this.photo.add(photo);
    }

    public Long getListId() {
        return listId;
    }

    public void setListId(Long listId) {
        this.listId = listId;
    }

    public String getListName() {
        return listName;
    }

    public void setListName(String listName) {
        this.listName = listName;
    }

    public List<CompareResponsePhotoObject> getPhoto() {
        return photo;
    }

    public void setPhoto(List<CompareResponsePhotoObject> photo) {
        this.photo = photo;
    }

    public Double getSimilarity() {
        return similarity;
    }

    public void setSimilarity(Double similarity) {
        this.similarity = similarity;
    }
}
