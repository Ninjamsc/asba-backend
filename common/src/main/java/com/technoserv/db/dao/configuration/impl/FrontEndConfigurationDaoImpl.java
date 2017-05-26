package com.technoserv.db.dao.configuration.impl;


import com.technoserv.db.dao.AbstractHibernateDao;
import com.technoserv.db.dao.configuration.api.FrontEndConfigurationDao;
import com.technoserv.db.model.configuration.FrontEndConfiguration;
import org.springframework.stereotype.Repository;

/**
 * Created by sergey on 15.11.2016.
 */
@Repository("frontEndConfigurationDao")
public class FrontEndConfigurationDaoImpl extends AbstractHibernateDao<Long, FrontEndConfiguration> implements FrontEndConfigurationDao {
}
