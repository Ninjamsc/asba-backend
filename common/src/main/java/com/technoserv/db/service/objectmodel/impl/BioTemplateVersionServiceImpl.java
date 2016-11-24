package com.technoserv.db.service.objectmodel.impl;

import com.technoserv.db.dao.objectmodel.api.BioTemplateVersionDao;
import com.technoserv.db.model.objectmodel.BioTemplateVersion;
import com.technoserv.db.service.AbstractService;
import com.technoserv.db.service.objectmodel.api.BioTemplateVersionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

/**
 * Created by sergey on 23.11.2016.
 */
@Service
public class BioTemplateVersionServiceImpl extends AbstractService<Integer,BioTemplateVersion,BioTemplateVersionDao> implements BioTemplateVersionService {
    @Override
    @Autowired
    @Qualifier("bioTemplateVersionDao")
    public void setDao(BioTemplateVersionDao dao) {
        this.dao = dao;
    }


}
