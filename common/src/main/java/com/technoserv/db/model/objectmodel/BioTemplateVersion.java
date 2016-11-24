package com.technoserv.db.model.objectmodel;

import com.technoserv.db.model.BaseEntity;

import javax.persistence.*;
import java.util.Date;

/**
 * Справочник версий биометрических шаблонов. Шаблон имеет версию.
 * На базе данного документа может быть построено несколько версий биометрического шаблона данного типа
 */
@Entity
@Table(name="BIO_TEMPLATES_VERSION")
public class BioTemplateVersion extends BaseEntity<Integer> {
    /**
     * уникальный идентификатор объекта
     */
    @Id
    @Column(name = "ID")
    private Integer id;

    /**
     * дата создания объекта
     */
    @Temporal(value = TemporalType.DATE)
    @Column(name = "INS_DATE", nullable = false, updatable = false)
    private Date objectDate = new Date();


    @Column(name = "DESCRIPTION", nullable = false)
    private String description;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getObjectDate() {
        return objectDate;
    }

    public void setObjectDate(Date objectDate) {
        this.objectDate = objectDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
