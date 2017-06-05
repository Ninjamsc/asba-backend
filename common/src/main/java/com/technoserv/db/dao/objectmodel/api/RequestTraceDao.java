package com.technoserv.db.dao.objectmodel.api;

import com.technoserv.db.dao.Dao;
import com.technoserv.db.model.objectmodel.Request;
import com.technoserv.db.model.objectmodel.RequestTrace;

/**
 *
 */
public interface RequestTraceDao extends Dao<Long, RequestTrace> {

    Long saveTrace(long requestId, Request.Status status, String comment);

}
