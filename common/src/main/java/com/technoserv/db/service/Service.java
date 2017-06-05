package com.technoserv.db.service;


import com.technoserv.db.model.BaseEntity;

import java.io.Serializable;
import java.util.List;

public interface Service<ID extends Serializable, T extends BaseEntity<ID>> {

    /**
     * Загружает сущность по заданному id.
     *
     * @param id - идентификатор сущности.
     * @return сущность
     */
    T findById(ID id, String... properties);

    ID save(T entity);

    void saveOrUpdate(T entity);

    void update(T entity);

    void delete(ID id);

    void delete(T entity);

    List<T> getAll(String... properties);

    int countAll();

    List<T> getAll(int page, int max, String... properties);
}
