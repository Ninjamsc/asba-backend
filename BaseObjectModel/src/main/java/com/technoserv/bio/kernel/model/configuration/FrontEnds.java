package com.technoserv.bio.kernel.model.configuration;

import com.technoserv.bio.kernel.model.objectmodel.AbstractObject;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.Table;

@Entity
@Table(name = "FRONT_ENDS")
public class FrontEnds extends AbstractObject {

    @Column(name = "FE_TYPE")
    private int feType;
    @Column(name = "VERSION")
    private Integer version; // TODO:нужно major minor
    //TODO: нужно сделать массив объетов класса. у каждого объекта ссылка на установенную ему конфигурацию и версию АРМ

    public FrontEndType getFeType() {
        return FrontEndType.parse(feType);
    }
    public void setFeType(FrontEndType feType) {
        this.feType = feType.getCode();
    }

    public Integer getVersion() {
        return version;
    }
    public void setVersion(Integer version) {
        this.version = version;
    }
}