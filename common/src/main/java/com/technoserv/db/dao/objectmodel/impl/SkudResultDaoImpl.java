package com.technoserv.db.dao.objectmodel.impl;

import com.technoserv.db.dao.AbstractHibernateDao;
import com.technoserv.db.dao.objectmodel.api.CompareResultDao;
import com.technoserv.db.dao.objectmodel.api.SkudResultDao;
import com.technoserv.db.model.objectmodel.CompareResult;
import com.technoserv.db.model.objectmodel.SkudResult;
import org.springframework.stereotype.Repository;

/**
 * Created by 90630 on 14.12.2016.
 */
@Repository("skudResultDao")
public class SkudResultDaoImpl extends AbstractHibernateDao<Long, SkudResult> implements SkudResultDao {
}
