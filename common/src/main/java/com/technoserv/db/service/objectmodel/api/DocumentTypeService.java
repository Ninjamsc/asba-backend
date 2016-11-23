package com.technoserv.db.service.objectmodel.api;

import com.technoserv.db.model.objectmodel.DocumentType;

/**
 * Created by sergey on 23.11.2016.
 */
public interface DocumentTypeService {
    DocumentType findByType(DocumentType.Type type);
}
