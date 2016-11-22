package com.technoserv.db.dao.configuration.impl;

import com.technoserv.db.dao.AbstractHibernateDao;
import com.technoserv.db.dao.configuration.api.FrontEndsDao;
import com.technoserv.db.model.configuration.FrontEnds;
import org.springframework.stereotype.Repository;

/**
 * Created by sergey on 15.11.2016.
 */
@Repository("frontEndsDao")
public class FrontEndsDaoImpl extends AbstractHibernateDao<Long,FrontEnds> implements FrontEndsDao {
}
