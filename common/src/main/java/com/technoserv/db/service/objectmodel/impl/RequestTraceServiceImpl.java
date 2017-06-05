package com.technoserv.db.service.objectmodel.impl;

import com.technoserv.db.dao.objectmodel.api.RequestTraceDao;
import com.technoserv.db.model.objectmodel.Request;
import com.technoserv.db.model.objectmodel.RequestTrace;
import com.technoserv.db.service.AbstractService;
import com.technoserv.db.service.objectmodel.api.RequestTraceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

/**
 *
 */
@Service
@Transactional
public class RequestTraceServiceImpl extends AbstractService<Long, RequestTrace, RequestTraceDao>
        implements RequestTraceService {

    @Override
    @Autowired
    @Qualifier("requestTraceDao")
    public void setDao(RequestTraceDao dao) {
        this.dao = dao;
    }

    public Long saveTrace(Long requestId, Request.Status status, String comment) {
        return dao.saveTrace(requestId, status, comment);
    }

}
