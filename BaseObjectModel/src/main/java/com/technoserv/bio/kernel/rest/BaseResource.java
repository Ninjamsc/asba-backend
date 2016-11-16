package com.technoserv.bio.kernel.rest;

import com.technoserv.bio.kernel.model.BaseEntity;
import com.technoserv.bio.kernel.service.Service;

import javax.ws.rs.core.Response;
import java.io.Serializable;
import java.util.Collection;

public abstract class BaseResource<K extends Serializable, T extends BaseEntity<K>> {

    protected abstract Service<K, T> getBaseService();

    public Collection<T> list() {
         return getBaseService().getAll();
    }

    public T get(K id) {
        return getBaseService().findById(id);
    }

    public K add(T entity) {
        return getBaseService().save(entity);
    }

    public Response update(T entity) {
        getBaseService().saveOrUpdate(entity);
        return Response.ok().build();
    }

    public Response delete(K id) {
        getBaseService().delete(id);
        return Response.ok().build();
    }
}