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
    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.ALL, CascadeType.REMOVE})
    @JoinTable(name = "STOP_LISTS_CONTENTS", joinColumns = {@JoinColumn(name = "LISTS_ID")}, inverseJoinColumns = {@JoinColumn(name = "DOC_ID")})
    private List<Document> owner;

    @Column(name = "TYPE", nullable = false, columnDefinition = "character varying default 'bank'")
    private String type = "bank";

    @Column(name = "DESCRIPTION")
    private String description;
    //TODO ...
//    @Transient
//    private List<Person> persons;

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

//    public List<Person> getPersons() {
//        return persons;
//    }
//
//    public void setPersons(List<Person> persons) {
//        this.persons = persons;
//    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}