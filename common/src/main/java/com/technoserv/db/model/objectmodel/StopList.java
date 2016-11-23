package com.technoserv.db.model.objectmodel;

import javax.persistence.*;
import java.util.List;

/**
 * Стоп лист. Стоп лист состоит из массива сверток.
 * Стоп лист может быть доступен либо всем контрагентам, либо списку контрагентов, либо определенному контрагенту.
 */
@Entity
@Table(name = "STOP_LISTS")
public class StopList extends AbstractObject {
    /**
     * наименование
     */
    @Column(name = "NAME")
    private String stopListName;
    /**
     * список банков, которым доступен этот список
     */
    @ManyToMany(fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    @JoinTable(name = "STOP_LISTS_CONTENTS", joinColumns = {@JoinColumn(name = "DOC_ID")}, inverseJoinColumns = {@JoinColumn(name = "LISTS_ID")})
    private List<Document> owner;

    @Column(name = "SIMILARITY")
    private Double similarity;

    public String getStopListName() {
        return stopListName;
    }

    public void setStopListName(String stopListName) {
        this.stopListName = stopListName;
    }

    public List<Document> getOwner() {
        return owner;
    }

    public void setOwner(List<Document> owner) {
        this.owner = owner;
    }

    public Double getSimilarity() {
        return similarity;
    }

    public void setSimilarity(Double similarity) {
        this.similarity = similarity;
    }
}