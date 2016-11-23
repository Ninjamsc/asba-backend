package com.technoserv.db.dao.objectmodel.impl;

import com.technoserv.db.dao.AbstractHibernateDao;
import com.technoserv.db.dao.objectmodel.api.BioTemplateDao;
import com.technoserv.db.model.objectmodel.BioTemplate;
import org.springframework.stereotype.Repository;

/**
 * Created by sergey on 23.11.2016.
 */
@Repository("bioTemplateDao")
public class BioTemplateDaoImpl extends AbstractHibernateDao<Long, BioTemplate> implements BioTemplateDao {
}
