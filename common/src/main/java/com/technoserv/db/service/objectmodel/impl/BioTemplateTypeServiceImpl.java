package com.technoserv.db.service.objectmodel.impl;

import com.technoserv.db.dao.objectmodel.api.BioTemplateTypeDao;
import com.technoserv.db.model.objectmodel.BioTemplateType;
import com.technoserv.db.service.AbstractService;
import com.technoserv.db.service.objectmodel.api.BioTemplateTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

/**
 * Created by sergey on 23.11.2016.
 */
@Service
public class BioTemplateTypeServiceImpl extends AbstractService<Long, BioTemplateType, BioTemplateTypeDao> implements BioTemplateTypeService {

    @Override
    @Autowired
    @Qualifier("bioTemplateTypeDao")
    public void setDao(BioTemplateTypeDao dao) {
        this.dao = dao;
    }
}
