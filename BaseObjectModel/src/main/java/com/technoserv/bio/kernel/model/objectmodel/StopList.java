package com.technoserv.bio.kernel.model.objectmodel;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
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
    @ManyToMany
    private List<Contractor> owner;
    /**
     * true - список доступен всем контракторам
     */
    @Column(name = "IS_COMMON")
    private boolean isCommon;

    public String getStopListName() {
        return stopListName;
    }

    public void setStopListName(String stopListName) {
        this.stopListName = stopListName;
    }

    public List<Contractor> getOwner() {
        return owner;
    }

    public void setOwner(List<Contractor> owner) {
        this.owner = owner;
    }

    public boolean isCommon() {
        return isCommon;
    }

    public void setCommon(boolean common) {
        isCommon = common;
    }
}
