package com.technoserv.bio.kernel.model.objectmodel;

import com.technoserv.bio.kernel.model.BaseEntity;
import org.hibernate.annotations.Generated;
import org.hibernate.annotations.GenerationTime;

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
    @Generated(GenerationTime.INSERT)
    @Temporal(value = TemporalType.DATE)
    @Column(name = "OBJECT_DATE", updatable = false)
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