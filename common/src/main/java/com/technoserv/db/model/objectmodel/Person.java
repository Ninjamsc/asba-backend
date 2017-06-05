package com.technoserv.db.model.objectmodel;

import com.technoserv.db.model.BaseEntity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Хранилище связок ИИН,
 * который идентифицирует заявителя
 * с полученными во время оформления заявки биометрическими материалами (фото)
 */
@Entity
@Table(name = "PERSONS")
public class Person extends BaseEntity<Long> {

    public Person() {
    }

    public Person(Long id) {
        this.id = id;
    }

    /**
     * ИИН персоны в WorkFlow
     */
    @Id
    @Column(name = "ID")
    private Long id;

    /**
     * массив всех заявок
     */
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "person")
    private List<Request> dossier = new ArrayList<Request>();

    public List<Request> getDossier() {
        return dossier;
    }

    public void setDossier(List<Request> dossier) {
        this.dossier = dossier;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}