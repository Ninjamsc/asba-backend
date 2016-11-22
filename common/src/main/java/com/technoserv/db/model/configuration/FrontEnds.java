package com.technoserv.db.model.configuration;


import com.technoserv.db.model.objectmodel.AbstractObject;

import javax.persistence.*;

@Entity
@Table(name = "FRONT_ENDS")
public class FrontEnds extends AbstractObject {

    @Column(name = "FE_TYPE")
    @Enumerated(EnumType.STRING)
    private FrontEndType feType;

    @Column(name = "VERSION")
    private Integer version; // TODO:нужно major minor
    //TODO: нужно сделать массив объетов класса. у каждого объекта ссылка на установенную ему конфигурацию и версию АРМ

    public FrontEndType getFeType() {
        return feType;
    }

    public void setFeType(FrontEndType feType) {
        this.feType = feType;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }
}