package com.technoserv.db.model;

import com.google.common.base.MoreObjects;

import java.io.Serializable;

public abstract class BaseEntity<ID extends Serializable> implements Serializable {

    private static final long serialVersionUID = -779738708199712530L;

    public abstract ID getId();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BaseEntity that = (BaseEntity) o;

        if (getId() != null ? !getId().equals(that.getId()) : that.getId() != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return getId() != null ? getId().hashCode() : super.hashCode();
    }

}