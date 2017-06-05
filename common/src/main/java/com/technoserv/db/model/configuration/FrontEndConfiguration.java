package com.technoserv.db.model.configuration;

import com.google.common.base.MoreObjects;
import com.technoserv.db.model.objectmodel.AbstractObject;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "FRONT_END_CONFIGURATIONS")
public class FrontEndConfiguration extends AbstractObject {

    @Column(name = "VERSION")
    private Integer version;

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("version", version)
                .toString();
    }
}