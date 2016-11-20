package com.technoserv.bio.kernel.dao.configuration.impl;

import com.technoserv.bio.kernel.dao.AbstractHibernateDao;
import com.technoserv.bio.kernel.dao.configuration.api.FrontEndsDao;
import com.technoserv.bio.kernel.model.configuration.FrontEnds;
import org.springframework.stereotype.Repository;

/**
 * Created by sergey on 15.11.2016.
 */
@Repository("frontEndsDao")
public class FrontEndsDaoImpl extends AbstractHibernateDao<Long,FrontEnds> implements FrontEndsDao {
}
