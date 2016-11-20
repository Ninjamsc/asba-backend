package com.technoserv.bio.kernel.dao.objectmodel.impl;

import com.technoserv.bio.kernel.dao.AbstractHibernateDao;
import com.technoserv.bio.kernel.dao.objectmodel.api.DocumentDao;
import com.technoserv.bio.kernel.model.objectmodel.Document;
import org.springframework.stereotype.Repository;

/**
 * Created by sergey on 15.11.2016.
 */
@Repository("documentDao")
public class DocumentDaoImpl  extends AbstractHibernateDao<Long,Document> implements DocumentDao {
}
