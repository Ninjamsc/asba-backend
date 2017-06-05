package com.technoserv.db.service;

import com.technoserv.db.dao.Dao;
import com.technoserv.db.model.BaseEntity;
import com.technoserv.utils.HibernateInitializer;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.List;

public abstract class AbstractService<ID extends Serializable, T extends BaseEntity<ID>, D extends Dao<ID, T>>
        implements Service<ID, T> {

    protected D dao;

    @Transactional(readOnly = true)
    public T findById(ID id, String... properties) {
        T t = getDao().get(id);
        HibernateInitializer.initializeProperties(t, properties);
        return t;
    }

    @Transactional
    public ID save(T entity) {
        return dao.save(entity);
    }

    @Transactional
    public void delete(ID id) {
        T entity = dao.get(id);
        if (entity != null) {
            dao.delete(entity);
        }
    }

    @Transactional
    public void delete(T entity) {
        dao.delete(entity);
    }

    @Transactional
    public List<T> getAll(String... properties) {
        List<T> result = dao.getAll();
        HibernateInitializer.initializeProperties(result, properties);
        return result;
    }

    @Transactional()
    public void saveOrUpdate(T entity) {
        dao.saveOrUpdate(entity);
    }

    @Transactional()
    public void update(T entity) {
        dao.update(entity);
    }

    protected D getDao() {
        return dao;
    }

    @Transactional(readOnly = true)
    public int countAll() {
        return dao.countAll();
    }

    @Transactional(readOnly = true)
    public List<T> getAll(int page, int max, String... properties) {
        List<T> result = dao.getAll(page, max);
        HibernateInitializer.initializeProperties(result, properties);
        return result;
    }

    public abstract void setDao(D dao);
}
