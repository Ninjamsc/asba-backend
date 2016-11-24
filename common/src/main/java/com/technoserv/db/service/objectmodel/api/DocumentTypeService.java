package com.technoserv.db.service.objectmodel.api;

import com.technoserv.db.model.objectmodel.DocumentType;
import com.technoserv.db.service.Service;

/**
 * Created by sergey on 23.11.2016.
 */
public interface DocumentTypeService extends Service<Long, DocumentType> {
    DocumentType findByType(DocumentType.Type type);
}
