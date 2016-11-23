package com.technoserv.db.service.objectmodel.impl;

import com.technoserv.db.model.objectmodel.DocumentType;
import com.technoserv.db.service.objectmodel.api.DocumentTypeService;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by sergey on 23.11.2016.
 */
@Service //TODO ...
public class DocumentTypeDictionaryInitializer implements InitializingBean {

    @Autowired
    private DocumentTypeService documentTypeService;

    public void afterPropertiesSet() throws Exception {
        for(DocumentType.Type type : DocumentType.Type.values()) {
            documentTypeService.save(new DocumentType(type, type.toString()));
        }
    }
}