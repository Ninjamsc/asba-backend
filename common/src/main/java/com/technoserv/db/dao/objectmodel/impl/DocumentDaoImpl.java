package com.technoserv.db.dao.objectmodel.impl;

import com.technoserv.db.dao.AbstractHibernateDao;
import com.technoserv.db.dao.objectmodel.api.DocumentDao;
import com.technoserv.db.model.objectmodel.Document;
import org.springframework.stereotype.Repository;

/**
 * Created by sergey on 15.11.2016.
 */
@Repository("documentDao")
public class DocumentDaoImpl extends AbstractHibernateDao<Long, Document> implements DocumentDao {
}
