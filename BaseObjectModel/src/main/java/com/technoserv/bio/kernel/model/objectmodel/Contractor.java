package com.technoserv.bio.kernel.model.objectmodel;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "CONTRACTORS")
public class Contractor extends AbstractObject {

    @Column(name = "NAME")
    private String contractorName;
    @ManyToMany
    private List<StopList> contractorLists;
    /**
     * мера схожести
     */
    @Column(name = "SIMILARITY_THRESHOLD")
    private float similarityThreshold;
    /**
     * загруженные банком фотографии (со свертками итп)
     */
    @OneToMany
    private List<Document> loadedImages;

    public String getContractorName() {
        return contractorName;
    }
    public void setContractorName(String contractorName) {
        this.contractorName = contractorName;
    }

    public List<StopList> getContractorLists() {
        return contractorLists;
    }
    public void setContractorLists(List<StopList> contractorLists) {
        this.contractorLists = contractorLists;
    }

    public float getSimilarityThreshold() {
        return similarityThreshold;
    }
    public void setSimilarityThreshold(float similarityThreshold) {
        this.similarityThreshold = similarityThreshold;
    }

    public List<Document> getLoadedImages() {
        return loadedImages;
    }
    public void setLoadedImages(List<Document> loadedImages) {
        this.loadedImages = loadedImages;
    }
}