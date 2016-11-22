package com.technoserv.db.service.objectmodel.impl;

import com.technoserv.db.dao.objectmodel.api.ContractorDao;
import com.technoserv.db.model.objectmodel.Contractor;
import com.technoserv.db.service.AbstractService;
import com.technoserv.db.service.objectmodel.api.ContractorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

/**
 * Created by sergey on 15.11.2016.
 */
@Service
public class ContractorServiceImpl extends AbstractService<Long, Contractor,ContractorDao> implements ContractorService {
    @Override
    @Autowired
    @Qualifier("contractorDao")
    public void setDao(ContractorDao dao) {
        this.dao = dao;
    }
}
