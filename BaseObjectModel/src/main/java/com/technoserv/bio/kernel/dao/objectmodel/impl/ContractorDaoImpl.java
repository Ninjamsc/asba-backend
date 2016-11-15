package com.technoserv.bio.kernel.dao.objectmodel.impl;

import com.technoserv.bio.kernel.dao.AbstractHibernateDao;
import com.technoserv.bio.kernel.dao.objectmodel.api.ContractorDao;
import com.technoserv.bio.kernel.model.objectmodel.Contractor;
import org.springframework.stereotype.Repository;

/**
 * Created by sergey on 15.11.2016.
 */
@Repository
public class ContractorDaoImpl extends AbstractHibernateDao<Long,Contractor> implements ContractorDao {
}
