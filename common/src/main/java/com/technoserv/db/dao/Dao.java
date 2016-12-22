package com.technoserv.db.dao;


import com.technoserv.db.model.BaseEntity;

import java.io.Serializable;
import java.util.List;

public interface Dao<ID extends Serializable,T extends BaseEntity<ID>> {

    /**
     * Загружает сущность по заданному id и инициализирует указанные поля.
     * @param id - идентификатор сущности.
     * @return сущность
     */
    public T get(ID id);

    public void saveOrUpdate(T entity);

    public void update(T entity);

    public ID save(T entity);

    public void delete(T entity);
    
    public List<T> getAll();

	public int countAll();

    List<T> getAll(int page, int max);
}
