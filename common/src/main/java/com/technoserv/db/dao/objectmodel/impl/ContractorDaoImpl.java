package com.technoserv.db.dao.objectmodel.impl;

import com.technoserv.db.dao.AbstractHibernateDao;
import com.technoserv.db.dao.objectmodel.api.ContractorDao;
import com.technoserv.db.model.objectmodel.Contractor;
import org.springframework.stereotype.Repository;

/**
 * Created by sergey on 15.11.2016.
 */
@Repository("contractorDao")
public class ContractorDaoImpl extends AbstractHibernateDao<Long,Contractor> implements ContractorDao {
}
