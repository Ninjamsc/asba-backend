package com.technoserv.bio.kernel.service.configuration.impl;

import com.technoserv.bio.kernel.dao.configuration.api.FrontEndsDao;
import com.technoserv.bio.kernel.model.configuration.FrontEnds;
import com.technoserv.bio.kernel.service.AbstractService;
import com.technoserv.bio.kernel.service.configuration.api.FrontEndsService;
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
