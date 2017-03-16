package com.technoserv.db.dao.configuration.impl;

import com.technoserv.db.dao.AbstractHibernateDao;
import com.technoserv.db.dao.configuration.api.FrontEndsDao;
import com.technoserv.db.model.configuration.FrontEnd;
import org.hibernate.criterion.Property;
import org.springframework.stereotype.Repository;

/**
 * Created by sergey on 15.11.2016.
 */
@Repository("frontEndsDao")
public class FrontEndsDaoImpl extends AbstractHibernateDao<Long,FrontEnd> implements FrontEndsDao {

    public FrontEnd findByUuid(String uuid) {
        return (FrontEnd) getSession().createCriteria(getPersistentClass())
                .add(Property.forName("uuid").eq(uuid)).uniqueResult();
    }
}
