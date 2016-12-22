package com.technoserv.db.dao;

import com.technoserv.db.model.BaseEntity;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Projections;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

public abstract class AbstractHibernateDao<ID extends Serializable,T extends BaseEntity<ID>> implements Dao<ID,T> {

    private Class<T> persistentClass;
    @Autowired
    protected SessionFactory sessionFactory;

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public Class<T> getPersistentClass() {
        return persistentClass;
    }

    @SuppressWarnings("unchecked")
    public AbstractHibernateDao() {
		Type type = getClass().getGenericSuperclass();
		while (type != null) {
			if(type instanceof ParameterizedType) {
				persistentClass = (Class<T>) ((ParameterizedType) type).getActualTypeArguments()[1];
				break;
			} else if (type instanceof Class) {
				type = ((Class) type).getGenericSuperclass();
			}
		}
    }

    @SuppressWarnings("unchecked")
    public T get(ID id) {
        T t = (T) getSession().get(getPersistentClass(), id);
        return t;
    }

    public void saveOrUpdate(T entity) {
        getSession().saveOrUpdate(entity);
    }

    public void update(T entity) {
        getSession().update(entity);
    }

    @SuppressWarnings("unchecked")
    public ID save(T entity) {
        return (ID) getSession().save(entity);
    }

    @SuppressWarnings("unchecked")
	public List<T> getAll() {
    	return getSession().createCriteria(getPersistentClass()).list();
    }

	public int countAll() {
    	Object o = getSession().createCriteria(getPersistentClass())
				.setProjection(Projections.rowCount())
				.uniqueResult();

		return o == null ? 0 : ((Number)o).intValue();
    }

    public void delete(T entity) {
        getSession().delete(entity);
    }

    public Session getSession() {
        return sessionFactory.getCurrentSession();
    }

    @SuppressWarnings("unchecked")
    public List<T> getAll(int page, int max) {
        return getSession().createCriteria(getPersistentClass()).setMaxResults(max).setFirstResult(page * max).list();
    }
}