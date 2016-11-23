package com.technoserv.db.dao.objectmodel.api;

import com.technoserv.db.dao.Dao;
import com.technoserv.db.model.objectmodel.DocumentType;

/**
 * Created by sergey on 23.11.2016.
 */
public interface DocumentTypeDao extends Dao<Long, DocumentType> {
    DocumentType findByType(DocumentType.Type type);
}
