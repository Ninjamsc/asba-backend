package com.technoserv.db.service.objectmodel.impl;

import com.technoserv.db.dao.objectmodel.api.RequestDao;
import com.technoserv.db.model.objectmodel.Request;
import com.technoserv.db.service.AbstractService;
import com.technoserv.db.service.objectmodel.api.RequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

/**
 * Created by sergey on 15.11.2016.
 */
@Service
public class RequestServiceImpl extends AbstractService<Long, Request,RequestDao> implements RequestService {
    @Override
    @Autowired
    @Qualifier("requestDao")
    public void setDao(RequestDao dao) {
        this.dao = dao;
    }
}
