package com.technoserv.db.dao.objectmodel.impl;

import com.technoserv.db.dao.AbstractHibernateDao;
import com.technoserv.db.dao.objectmodel.api.RequestDao;
import com.technoserv.db.model.objectmodel.Request;
import org.springframework.stereotype.Repository;

/**
 * Created by sergey on 15.11.2016.
 */
@Repository("requestDao")
public class RequestDaoImpl  extends AbstractHibernateDao<Long,Request> implements RequestDao {
}
