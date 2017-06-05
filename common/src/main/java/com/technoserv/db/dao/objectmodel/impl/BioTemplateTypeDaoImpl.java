package com.technoserv.db.dao.objectmodel.impl;

import com.technoserv.db.dao.AbstractHibernateDao;
import com.technoserv.db.dao.objectmodel.api.BioTemplateTypeDao;
import com.technoserv.db.model.objectmodel.BioTemplateType;
import org.springframework.stereotype.Repository;

/**
 * Created by sergey on 23.11.2016.
 */
@Repository("bioTemplateTypeDao")
public class BioTemplateTypeDaoImpl extends AbstractHibernateDao<Long, BioTemplateType> implements BioTemplateTypeDao {
}
