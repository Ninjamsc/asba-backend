package com.technoserv.db.dao.objectmodel.impl;

import com.technoserv.db.dao.AbstractHibernateDao;
import com.technoserv.db.dao.objectmodel.api.CompareResultDao;
import com.technoserv.db.model.objectmodel.CompareResult;
import org.springframework.stereotype.Repository;

/**
 * Created by 90630 on 14.12.2016.
 */
@Repository("compareResultDao")
public class CompareResultDaoImpl extends AbstractHibernateDao<Long, CompareResult> implements CompareResultDao {
}
