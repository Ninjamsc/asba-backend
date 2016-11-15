package com.technoserv.bio.kernel.dao;

import com.technoserv.bio.kernel.model.BaseEntity;

import java.io.Serializable;
import java.util.List;

public interface Dao<ID extends Serializable,T extends BaseEntity<ID>> {

    /**
     * Загружает сущность по заданному id и инициализирует указанные поля.
     * @param id - идентификатор сущности.
     * @return сущность
     */
    public T findById(Long id);

    public void saveOrUpdate(T entity);

    public Long save(T entity);

    public void delete(T entity);
    
    public List<T> getAll();

	public int countAll();
}
