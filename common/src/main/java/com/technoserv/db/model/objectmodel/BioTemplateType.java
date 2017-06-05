package com.technoserv.db.model.objectmodel;

import com.technoserv.db.model.BaseEntity;

import javax.persistence.*;
import java.util.Date;

/**
 * Справочник типов биометрических шаблонов (Фото, вены, отпечатки пальцев и т.п.)
 */
@Entity
@Table(name = "BIO_TEMPLATES_TYPES")
public class BioTemplateType extends BaseEntity<Long> {

    public enum Type {
        UNDEF(0, "reserved"), PORTRAIT(1, "портретная свёртка"), FINGERPRINT(2, "пальцы");
        private long value;
        private String description;

        Type(long value, String description) {
            this.value = value;
            this.description = description;
        }

        public long getValue() {
            return value;
        }

        public String getDescription() {
            return description;
        }
    }

    /**
     * уникальный идентификатор объекта
     */
    @Id
    @Column(name = "BTMPLT_ID")
    private Long id;
    /**
     * дата создания объекта
     */
    @Temporal(value = TemporalType.DATE)
    @Column(name = "INS_DATE", nullable = false, updatable = false)
    private Date objectDate = new Date();

    @Column(name = "DESCRIPTION", nullable = false)
    private String description;


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    @Override
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
