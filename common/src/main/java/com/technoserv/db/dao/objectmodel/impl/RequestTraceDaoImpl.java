package com.technoserv.db.dao.objectmodel.impl;

import com.technoserv.db.dao.AbstractHibernateDao;
import com.technoserv.db.dao.objectmodel.api.RequestTraceDao;
import com.technoserv.db.model.objectmodel.Request;
import com.technoserv.db.model.objectmodel.RequestTrace;
import org.springframework.stereotype.Repository;

/**
 *
 */
@Repository("requestTraceDao")
public class RequestTraceDaoImpl extends AbstractHibernateDao<Long, RequestTrace> implements RequestTraceDao {

    public Long saveTrace(long requestId, Request.Status status, String comment) {
        RequestTrace trace = new RequestTrace(requestId, status, comment);
        return save(trace);
    }
}
