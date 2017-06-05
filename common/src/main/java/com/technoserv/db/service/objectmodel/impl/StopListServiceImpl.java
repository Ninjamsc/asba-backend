package com.technoserv.db.service.objectmodel.impl;

import com.technoserv.db.dao.objectmodel.api.StopListDao;
import com.technoserv.db.model.objectmodel.StopList;
import com.technoserv.db.service.AbstractService;
import com.technoserv.db.service.objectmodel.api.StopListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

/**
 * Created by sergey on 15.11.2016.
 */
@Service
public class StopListServiceImpl extends AbstractService<Long, StopList, StopListDao> implements StopListService {

    @Override
    @Autowired
    @Qualifier("stopListDao")
    public void setDao(StopListDao dao) {
        this.dao = dao;
    }

}
