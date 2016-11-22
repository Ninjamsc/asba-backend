package com.technoserv.db.service.configuration.impl;

import com.technoserv.db.dao.configuration.api.FrontEndsDao;
import com.technoserv.db.model.configuration.FrontEnds;
import com.technoserv.db.service.AbstractService;
import com.technoserv.db.service.configuration.api.FrontEndsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

/**
 * Created by sergey on 15.11.2016.
 */
@Service
public class FrontEndsServiceImpl extends AbstractService<Long, FrontEnds,FrontEndsDao> implements FrontEndsService {
    @Override
    @Autowired
    @Qualifier("frontEndsDao")
    public void setDao(FrontEndsDao dao) {
        this.dao = dao;
    }
}
