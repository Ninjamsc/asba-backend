package com.technoserv.db.service.objectmodel.api;


import com.technoserv.db.model.objectmodel.Document;
import com.technoserv.db.model.objectmodel.DocumentType;
import com.technoserv.db.model.objectmodel.Request;
import com.technoserv.db.service.Service;

import java.util.Collection;

/**
 * Created by sergey on 15.11.2016.
 */
public interface RequestService extends Service<Long,Request> {
    Request findByOrderNumber(Long id);
    Collection<Request> findNotProcessed();
    public Collection<Request> findByIin(Long id,String... properties);
    Long createOrder(Long iin, Long wfmId, String username);
    void updateDocument(Long wfUid, String previewUrl, String fullFrameUrl, DocumentType.Type type);
}