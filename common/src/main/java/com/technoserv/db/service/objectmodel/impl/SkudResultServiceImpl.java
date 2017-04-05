package com.technoserv.db.service.objectmodel.impl;

import com.technoserv.db.dao.objectmodel.api.SkudResultDao;
import com.technoserv.db.model.objectmodel.SkudResult;
import com.technoserv.db.service.AbstractService;
import com.technoserv.db.service.objectmodel.api.SkudResultService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

/**
 * Created by 90630 on 14.12.2016.
 */
@Service
public class SkudResultServiceImpl extends AbstractService<Long,SkudResult,SkudResultDao> implements SkudResultService {

    @Autowired
    @Qualifier("skudResultDao")
    public void setDao(SkudResultDao dao) {
        this.dao = dao;
    }
}
