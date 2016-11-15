package com.technoserv.bio.kernel.service.objectmodel.impl;

import com.technoserv.bio.kernel.dao.objectmodel.api.RequestDao;
import com.technoserv.bio.kernel.model.objectmodel.Request;
import com.technoserv.bio.kernel.service.AbstractService;
import com.technoserv.bio.kernel.service.objectmodel.api.RequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

/**
 * Created by sergey on 15.11.2016.
 */
@Service
public class RequestServiceImpl extends AbstractService<Long, Request,RequestDao> implements RequestService{
    @Override
    @Autowired
    @Qualifier("requestDao")
    public void setDao(RequestDao dao) {
        this.dao = dao;
    }
}
