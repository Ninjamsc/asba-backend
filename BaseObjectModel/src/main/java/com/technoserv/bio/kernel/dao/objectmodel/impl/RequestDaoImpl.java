package com.technoserv.bio.kernel.dao.objectmodel.impl;

import com.technoserv.bio.kernel.dao.AbstractHibernateDao;
import com.technoserv.bio.kernel.dao.objectmodel.api.RequestDao;
import com.technoserv.bio.kernel.model.objectmodel.Request;
import org.springframework.stereotype.Repository;

/**
 * Created by sergey on 15.11.2016.
 */
@Repository("requestDao")
public class RequestDaoImpl  extends AbstractHibernateDao<Long,Request> implements RequestDao {
}
