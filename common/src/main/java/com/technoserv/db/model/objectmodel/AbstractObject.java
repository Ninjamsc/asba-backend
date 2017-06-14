package com.technoserv.db.model.objectmodel;


import com.google.common.base.MoreObjects;
import com.technoserv.db.model.BaseEntity;

import javax.persistence.*;
import java.util.Date;

@MappedSuperclass
public abstract class AbstractObject extends BaseEntity<Long> {
    /**
     * уникальный идентификатор объекта
     */
    @Id
    @Column(name = "ID")
    @GeneratedValue
    private Long id;

    /**
     * дата создания объекта
     */
    @Temporal(value = TemporalType.DATE)
    @Column(name = "INS_DATE", nullable = false, updatable = false)
    private Date objectDate = new Date();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getObjectDate() {
        return objectDate;
    }

    public void setObjectDate(Date objectDate) {
        this.objectDate = objectDate;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("id", id)
                .add("objectDate", objectDate)
                .toString();
    }
}