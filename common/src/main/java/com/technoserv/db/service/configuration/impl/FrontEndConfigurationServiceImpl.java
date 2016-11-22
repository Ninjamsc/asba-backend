package com.technoserv.db.service.configuration.impl;

import com.technoserv.db.dao.configuration.api.FrontEndConfigurationDao;
import com.technoserv.db.model.configuration.FrontEndConfiguration;
import com.technoserv.db.service.AbstractService;
import com.technoserv.db.service.configuration.api.FrontEndConfigurationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;


/**
 * Created by sergey on 15.11.2016.
 */
@Service
public class FrontEndConfigurationServiceImpl extends AbstractService<Long, FrontEndConfiguration,FrontEndConfigurationDao> implements FrontEndConfigurationService {

    @Override
    @Autowired
    @Qualifier("frontEndConfigurationDao")
    public void setDao(FrontEndConfigurationDao dao) {
        this.dao = dao;
    }
}
