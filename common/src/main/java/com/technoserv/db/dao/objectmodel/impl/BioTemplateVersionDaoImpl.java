package com.technoserv.db.dao.objectmodel.impl;

import com.technoserv.db.dao.AbstractHibernateDao;
import com.technoserv.db.dao.objectmodel.api.BioTemplateVersionDao;
import com.technoserv.db.model.objectmodel.BioTemplateVersion;
import org.springframework.stereotype.Repository;

/**
 * Created by sergey on 23.11.2016.
 */
@Repository("bioTemplateVersionDao")
public class BioTemplateVersionDaoImpl extends AbstractHibernateDao<Long, BioTemplateVersion> implements BioTemplateVersionDao {
}
