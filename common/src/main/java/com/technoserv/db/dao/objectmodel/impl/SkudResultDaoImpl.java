package com.technoserv.db.dao.objectmodel.impl;

import com.technoserv.db.dao.AbstractHibernateDao;
import com.technoserv.db.dao.objectmodel.api.CompareResultDao;
import com.technoserv.db.dao.objectmodel.api.SkudResultDao;
import com.technoserv.db.model.objectmodel.CompareResult;
import com.technoserv.db.model.objectmodel.SkudResult;
import com.technoserv.db.model.security.UserProfile;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by 90630 on 14.12.2016.
 */
@Repository("skudResultDao")
public class SkudResultDaoImpl extends AbstractHibernateDao<Long, SkudResult> implements SkudResultDao {

    @SuppressWarnings("unchecked")
    public List<SkudResult> findAll() {
        //return (List<SkudResult>) getSession().createCriteria(getPersistentClass()).addOrder(Order.desc("faceId")).uniqueResult();
        return getSession().createCriteria(getPersistentClass()).addOrder(Order.desc("orderDate")).list();
        /*Criteria crit = createEntityCriteria();
        crit.addOrder(Order.asc("orderDate"));
        return (List<SkudResult>)crit.list();*/
    }
}
