package com.technoserv.db.dao.objectmodel.impl;

import com.technoserv.db.dao.AbstractHibernateDao;
import com.technoserv.db.dao.objectmodel.api.BioTemplateDao;
import com.technoserv.db.model.objectmodel.BioTemplate;
import com.technoserv.db.model.objectmodel.Document;
import org.hibernate.Query;
import org.hibernate.criterion.Order;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by sergey on 23.11.2016.
 */
@Repository("bioTemplateDao")
public class BioTemplateDaoImpl extends AbstractHibernateDao<Long, BioTemplate> implements BioTemplateDao {
    public List<BioTemplate> getAllByDocument(Document d) {
        Query query = getSession().createQuery("FROM BioTemplate WHERE document=:doc");
        query.setParameter("doc",d);
                //.createCriteria(getPersistentClass()).
                //.addOrder(Order.desc("orderDate")).list();
        return (List<BioTemplate>) query.list();
    }
}
