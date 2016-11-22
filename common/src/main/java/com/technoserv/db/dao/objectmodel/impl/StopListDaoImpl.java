package com.technoserv.db.dao.objectmodel.impl;

import com.technoserv.db.dao.AbstractHibernateDao;
import com.technoserv.db.dao.objectmodel.api.StopListDao;
import com.technoserv.db.model.objectmodel.StopList;
import org.springframework.stereotype.Repository;

/**
 * Created by sergey on 15.11.2016.
 */
@Repository("stopListDao")
public class StopListDaoImpl extends AbstractHibernateDao<Long,StopList> implements StopListDao {
}