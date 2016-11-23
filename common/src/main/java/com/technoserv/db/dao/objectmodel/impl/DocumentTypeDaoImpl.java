package com.technoserv.db.dao.objectmodel.impl;

import com.technoserv.db.dao.AbstractHibernateDao;
import com.technoserv.db.dao.objectmodel.api.DocumentTypeDao;
import com.technoserv.db.model.objectmodel.DocumentType;
import org.springframework.stereotype.Repository;

/**
 * Created by sergey on 23.11.2016.
 */
@Repository("documentTypeDao")
public class DocumentTypeDaoImpl extends AbstractHibernateDao<Long,DocumentType> implements DocumentTypeDao {
}
