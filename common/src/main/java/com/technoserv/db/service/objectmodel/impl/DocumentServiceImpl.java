package com.technoserv.db.service.objectmodel.impl;

import com.technoserv.db.dao.objectmodel.api.DocumentDao;
import com.technoserv.db.model.objectmodel.Document;
import com.technoserv.db.service.AbstractService;
import com.technoserv.db.service.objectmodel.api.DocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

/**
 * Created by sergey on 15.11.2016.
 */
@Service
public class DocumentServiceImpl extends AbstractService<Long, Document, DocumentDao> implements DocumentService {
    @Override
    @Autowired
    @Qualifier("documentDao")
    public void setDao(DocumentDao dao) {
        this.dao = dao;
    }
}
