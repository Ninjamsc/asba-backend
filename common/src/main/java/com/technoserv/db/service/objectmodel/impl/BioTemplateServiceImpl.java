package com.technoserv.db.service.objectmodel.impl;

import com.technoserv.db.dao.objectmodel.api.BioTemplateDao;
import com.technoserv.db.model.objectmodel.BioTemplate;
import com.technoserv.db.service.AbstractService;
import com.technoserv.db.service.objectmodel.api.BioTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

/**
 * Created by sergey on 23.11.2016.
 */
@Service
public class BioTemplateServiceImpl extends AbstractService<Long,BioTemplate,BioTemplateDao> implements BioTemplateService {
    @Override
    @Autowired
    @Qualifier("bioTemplateDao")
    public void setDao(BioTemplateDao dao) {
        this.dao = dao;
    }
}
