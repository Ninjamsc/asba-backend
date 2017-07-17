package com.technoserv.db.dao.objectmodel.impl;

import com.technoserv.db.dao.AbstractHibernateDao;
import com.technoserv.db.dao.objectmodel.api.SkudResultDao;
import com.technoserv.db.model.objectmodel.SkudResult;
import org.hibernate.criterion.Order;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by 90630 on 14.12.2016.
 */
@Repository("skudResultDao")
public class SkudResultDaoImpl extends AbstractHibernateDao<Long, SkudResult> implements SkudResultDao {

    @SuppressWarnings("unchecked")
    public List<SkudResult> findAll() {
        return getSession().createCriteria(getPersistentClass())
                .addOrder(Order.desc("orderDate")).list();
    }
}
