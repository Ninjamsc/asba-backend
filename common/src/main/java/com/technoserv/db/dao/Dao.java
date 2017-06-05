package com.technoserv.db.dao;


import com.technoserv.db.model.BaseEntity;

import java.io.Serializable;
import java.util.List;

public interface Dao<ID extends Serializable, T extends BaseEntity<ID>> {

    /**
     * Загружает сущность по заданному id и инициализирует указанные поля.
     *
     * @param id - идентификатор сущности.
     * @return сущность
     */
    T get(ID id);

    void saveOrUpdate(T entity);

    void update(T entity);

    ID save(T entity);

    void delete(T entity);

    List<T> getAll();

    int countAll();

    List<T> getAll(int page, int max);
}
