package com.technoserv.db.dao.objectmodel.impl;

import com.technoserv.db.dao.AbstractHibernateDao;
import com.technoserv.db.dao.objectmodel.api.StopListDao;
import com.technoserv.db.model.objectmodel.StopList;
import org.hibernate.Criteria;
import org.hibernate.criterion.CriteriaSpecification;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by sergey on 15.11.2016.
 */
@Repository("stopListDao")
public class StopListDaoImpl extends AbstractHibernateDao<Long,StopList> implements StopListDao {
    @Override
    public List<StopList> getAll() {
        Criteria criteria = getSession().createCriteria(getPersistentClass());
        criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
        return criteria.list();
    }
}
