package com.technoserv.bio.kernel.dao.objectmodel.impl;

import com.technoserv.bio.kernel.dao.AbstractHibernateDao;
import com.technoserv.bio.kernel.dao.objectmodel.api.ConvolutionDao;
import com.technoserv.bio.kernel.model.objectmodel.Convolution;
import org.springframework.stereotype.Repository;

/**
 * Created by sergey on 15.11.2016.
 */
@Repository
public class ConvolutionDaoImpl  extends AbstractHibernateDao<Long,Convolution> implements ConvolutionDao {
}
