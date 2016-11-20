package com.technoserv.bio.kernel.dao.configuration.impl;

import com.technoserv.bio.kernel.dao.AbstractHibernateDao;
import com.technoserv.bio.kernel.dao.configuration.api.FrontEndConfigurationDao;
import com.technoserv.bio.kernel.model.configuration.FrontEndConfiguration;
import org.springframework.stereotype.Repository;

/**
 * Created by sergey on 15.11.2016.
 */
@Repository("frontEndConfigurationDao")
public class FrontEndConfigurationDaoImpl extends AbstractHibernateDao<Long,FrontEndConfiguration> implements FrontEndConfigurationDao {
}
