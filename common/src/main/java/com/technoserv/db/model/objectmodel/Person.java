package com.technoserv.db.model.objectmodel;

import com.technoserv.db.model.BaseEntity;

import javax.persistence.*;
import java.util.List;

/**
 * Хранилище связок ИИН,
 * который идентифицирует заявителя
 * с полученными во время оформления заявки биометрическими материалами (фото)
 */
@Entity
@Table(name = "PERSONS")
public class Person extends BaseEntity<Long> {
    /**
     * ИИН персоны в WorkFlow
     */
    @Id
    @Column(name = "ID")
    private Long id;
    /**
     * массив всех заявок
     */
    @OneToMany
    private List<Request> dossier;

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