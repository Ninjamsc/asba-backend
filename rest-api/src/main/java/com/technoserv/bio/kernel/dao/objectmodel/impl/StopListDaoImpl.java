package com.technoserv.bio.kernel.dao.objectmodel.impl;

import com.technoserv.bio.kernel.dao.AbstractHibernateDao;
import com.technoserv.bio.kernel.dao.objectmodel.api.StopListDao;
import com.technoserv.bio.kernel.model.objectmodel.StopList;
import org.springframework.stereotype.Repository;

/**
 * Created by sergey on 15.11.2016.
 */
@Repository("stopListDao")
public class StopListDaoImpl extends AbstractHibernateDao<Long,StopList> implements StopListDao {
}
