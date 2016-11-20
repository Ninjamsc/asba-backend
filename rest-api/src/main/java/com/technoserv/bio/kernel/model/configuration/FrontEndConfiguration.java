package com.technoserv.bio.kernel.model.configuration;

import com.technoserv.bio.kernel.model.objectmodel.AbstractObject;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "FRONT_END_CONFIGURATIONS")
public class FrontEndConfiguration extends AbstractObject {

    @Column(name = "VERSION")
    private Integer version; // TODO: string val
    // key value

    public Integer getVersion() {
        return version;
    }
    public void setVersion(Integer version) {
        this.version = version;
    }
}