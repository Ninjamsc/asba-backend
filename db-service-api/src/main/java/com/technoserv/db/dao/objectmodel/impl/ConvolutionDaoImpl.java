package com.technoserv.db.dao.objectmodel.impl;

import com.technoserv.db.dao.AbstractHibernateDao;
import com.technoserv.db.dao.objectmodel.api.ConvolutionDao;
import com.technoserv.db.model.objectmodel.Convolution;
import org.springframework.stereotype.Repository;

/**
 * Created by sergey on 15.11.2016.
 */
@Repository("convolutionDao")
public class ConvolutionDaoImpl  extends AbstractHibernateDao<Long,Convolution> implements ConvolutionDao {
}
