package com.technoserv.bio.kernel.service.objectmodel.impl;

import com.technoserv.bio.kernel.dao.objectmodel.api.ConvolutionDao;
import com.technoserv.bio.kernel.model.objectmodel.Convolution;
import com.technoserv.bio.kernel.service.AbstractService;
import com.technoserv.bio.kernel.service.objectmodel.api.ConvolutionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

/**
 * Created by sergey on 15.11.2016.
 */
@Service
public class ConvolutionServiceImpl extends AbstractService<Long, Convolution,ConvolutionDao> implements ConvolutionService{
    @Override
    @Autowired
    @Qualifier("convolutionDao")
    public void setDao(ConvolutionDao dao) {
        this.dao = dao;
    }
}
