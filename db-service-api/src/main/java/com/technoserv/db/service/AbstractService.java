package com.technoserv.db.service;

import com.technoserv.db.dao.Dao;
import com.technoserv.db.model.BaseEntity;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.List;

public abstract class AbstractService<ID extends Serializable,T extends BaseEntity<ID>, D extends Dao<ID,T>> implements Service<ID,T> {

    protected D dao;

    @Transactional(readOnly = true)
    public T findById(ID id) {
        return getDao().get(id);
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
    public List<T> getAll() {
        return dao.getAll();
    }

    @Transactional(readOnly = false)
    public void saveOrUpdate(T entity) {
        dao.saveOrUpdate(entity);
    }

    protected D getDao() {
        return dao;
    }

	@Transactional(readOnly = true)
	public int countAll() {
		return dao.countAll();
	}

    public abstract void setDao(D dao);
}
