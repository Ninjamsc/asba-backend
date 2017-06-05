package com.technoserv.db.service.objectmodel.api;

import com.technoserv.db.model.objectmodel.Request;
import com.technoserv.db.model.objectmodel.RequestTrace;
import com.technoserv.db.service.Service;

/**
 *
 */
public interface RequestTraceService extends Service<Long, RequestTrace> {

    Long saveTrace(Long requestId, Request.Status status, String comment);

}
