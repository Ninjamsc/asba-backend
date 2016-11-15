package com.technoserv.bio.kernel.model.objectmodel;

import com.technoserv.bio.kernel.model.BaseEntity;

import javax.persistence.*;
import java.util.Date;

@MappedSuperclass
public abstract class AbstractObject extends BaseEntity<Long> {
    /**
     * уникальный идентификатор объекта
     */
    @Id
    @GeneratedValue
    private Long id;
    /**
     * дата создания объекта
     */
    @Column
    @Temporal(value = TemporalType.DATE)
    private Date objectDate;

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
}