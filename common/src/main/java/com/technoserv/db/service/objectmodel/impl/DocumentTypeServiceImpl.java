package com.technoserv.db.service.objectmodel.impl;

import com.technoserv.db.dao.objectmodel.api.DocumentTypeDao;
import com.technoserv.db.model.objectmodel.DocumentType;
import com.technoserv.db.service.AbstractService;
import com.technoserv.db.service.objectmodel.api.DocumentTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

/**
 * Created by sergey on 23.11.2016.
 */
@Service
public class DocumentTypeServiceImpl extends AbstractService<Long, DocumentType,DocumentTypeDao> implements DocumentTypeService {
    @Override
    @Autowired
    @Qualifier("documentTypeDao")
    public void setDao(DocumentTypeDao dao) {
        this.dao = dao;
    }

    public DocumentType findByType(DocumentType.Type type) {
        return getDao().findByType(type);
    }
}