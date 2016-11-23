package com.technoserv.db.dao.objectmodel.impl;

import com.technoserv.db.dao.AbstractHibernateDao;
import com.technoserv.db.dao.objectmodel.api.RequestDao;
import com.technoserv.db.model.objectmodel.Request;
import org.hibernate.criterion.Property;
import org.springframework.stereotype.Repository;

import java.util.Collection;

/**
 * Created by sergey on 15.11.2016.
 */
@Repository("requestDao")
public class RequestDaoImpl extends AbstractHibernateDao<Long, Request> implements RequestDao {
    public Request findByOrderNumber(Long id) {
        return (Request) getSession().createCriteria(getPersistentClass())
                .add(Property.forName("wfmID").eq(id)).uniqueResult();
    }

    public Collection<Request> findNotProcessed() {
        return getSession().createCriteria(getPersistentClass())
                .add(Property.forName("status").eq(Request.Status.SAVED)).list();
    }
}
