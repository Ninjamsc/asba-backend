package com.technoserv.rest.resources;


import com.technoserv.db.model.BaseEntity;
import com.technoserv.db.service.Service;

import javax.ws.rs.core.Response;
import java.io.Serializable;
import java.util.Collection;

public abstract class BaseResource<K extends Serializable, T extends BaseEntity<K>> {

    protected abstract Service<K, T> getBaseService();

    public Collection<T> list() {
         return getBaseService().getAll();
    }
    public Collection<T> list(int page,int max) {
         return getBaseService().getAll(page, max);
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