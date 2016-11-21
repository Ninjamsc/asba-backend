package com.technoserv.bio.kernel.model.configuration;

import com.technoserv.bio.kernel.enumeration.EnumUserType;
import com.technoserv.bio.kernel.model.objectmodel.AbstractObject;
import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.Table;

@Entity
@Table(name = "FRONT_ENDS")
@TypeDefs({@TypeDef(name = "AgencyType", typeClass = EnumUserType.class, parameters = {
        @Parameter(name = "enumClass", value = "com.technoserv.bio.kernel.model.configuration.FrontEndType"),
        @Parameter(name = "identifierMethod", value = "getCode"),
        @Parameter(name = "valueOfMethod", value = "getByCode")})})
public class FrontEnds extends AbstractObject {

    @Column(name = "FE_TYPE")
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