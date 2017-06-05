package com.technoserv.db.service.objectmodel.impl;

import com.technoserv.db.dao.objectmodel.api.CompareResultDao;
import com.technoserv.db.model.objectmodel.CompareResult;
import com.technoserv.db.service.AbstractService;
import com.technoserv.db.service.objectmodel.api.CompareResultService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

/**
 * Created by 90630 on 14.12.2016.
 */
@Service
public class CompareResultServiceImpl extends AbstractService<Long, CompareResult, CompareResultDao> implements CompareResultService {

    @Autowired
    @Qualifier("compareResultDao")
    public void setDao(CompareResultDao dao) {
        this.dao = dao;
    }
}
