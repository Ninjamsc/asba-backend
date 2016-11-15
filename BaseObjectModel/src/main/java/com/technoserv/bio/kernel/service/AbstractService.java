package com.technoserv.bio.kernel.service;

import com.technoserv.bio.kernel.dao.Dao;
import com.technoserv.bio.kernel.model.BaseEntity;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.List;

public abstract class AbstractService<ID extends Serializable,T extends BaseEntity<ID>, D extends Dao<ID,T>> implements Service<T> {

    protected D dao;

    @Transactional(readOnly = true)
    public T findById(Long id) {
        return getDao().findById(id);
    }

    @Transactional
    public Long save(T entity) {
        return dao.save(entity);
    }

    @Transactional
    public void delete(Long id) {
        T entity = dao.findById(id);
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

    public D getDao() {
        return dao;
    }

	@Transactional(readOnly = true)
	public int countAll() {
		return dao.countAll();
	}

    public abstract void setDao(D dao);
}
