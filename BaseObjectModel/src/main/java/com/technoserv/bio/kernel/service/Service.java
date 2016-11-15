package com.technoserv.bio.kernel.service;


import com.technoserv.bio.kernel.model.BaseEntity;

import java.io.Serializable;
import java.util.List;

public interface Service<ID extends Serializable,T extends BaseEntity<ID>> {
    /**
     * Загружает сущность по заданному id.
     * @param id         - идентификатор сущности.
     * @return сущность
     */
    public T findById(ID id);

    public ID save(T entity);

    public void saveOrUpdate(T entity);

    public void delete(ID id);

    public void delete(T entity);

    public List<T> getAll();

	public int countAll();
}
