package com.technoserv.bio.kernel.service.objectmodel.impl;

import com.technoserv.bio.kernel.dao.objectmodel.api.StopListDao;
import com.technoserv.bio.kernel.model.objectmodel.StopList;
import com.technoserv.bio.kernel.service.AbstractService;
import com.technoserv.bio.kernel.service.objectmodel.api.StopListService;
import org.springframework.stereotype.Service;

/**
 * Created by sergey on 15.11.2016.
 */
@Service
public class StopListServiceImpl extends AbstractService<Long, StopList,StopListDao> implements StopListService{
}
