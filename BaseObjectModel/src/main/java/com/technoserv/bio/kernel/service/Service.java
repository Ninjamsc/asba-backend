package com.technoserv.bio.kernel.service;


import com.technoserv.bio.kernel.model.BaseEntity;

import java.util.List;

public interface Service<T extends BaseEntity> {
    /**
     * Загружает сущность по заданному id.
     * @param id         - идентификатор сущности.
     * @return сущность
     */
    public T findById(Long id);

    public Long save(T entity);

    void saveOrUpdate(T entity);

    public void delete(Long id);

    public void delete(T entity);

    public List<T> getAll();

	int countAll();
}
