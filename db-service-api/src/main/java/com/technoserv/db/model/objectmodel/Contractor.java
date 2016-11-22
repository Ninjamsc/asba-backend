package com.technoserv.db.model.objectmodel;

import javax.persistence.*;
import java.util.List;

/**
 * подрядчик
 */
@Entity
@Table(name = "CONTRACTORS")
public class Contractor extends AbstractObject {

    @Column(name = "NAME")
    private String contractorName;

    @ManyToMany
    @JoinTable(name = "CONTRACTORS_STOP_LISTS", joinColumns = {@JoinColumn(name = "CONTRACTORS_ID")}, inverseJoinColumns = {@JoinColumn(name = "STOP_LISTS_ID")})
    private List<StopList> contractorLists;
    /**
     * мера схожести
     */
    @Column(name = "SIMILARITY_THRESHOLD")
    private float similarityThreshold;
    /**
     * загруженные банком фотографии (со свертками итп)
     */

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "LOADED_IMAGE_ID", referencedColumnName = "ID")
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