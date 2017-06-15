package com.technoserv.db.dao.objectmodel.impl;

import com.technoserv.db.dao.AbstractHibernateDao;
import com.technoserv.db.dao.objectmodel.api.DocumentTypeDao;
import com.technoserv.db.model.objectmodel.DocumentType;
import org.hibernate.criterion.Property;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * Created by sergey on 23.11.2016.
 */
@Repository("documentTypeDao")
public class DocumentTypeDaoImpl extends AbstractHibernateDao<Long, DocumentType> implements DocumentTypeDao {

    public DocumentType findByType(DocumentType.Type type) {
        return (DocumentType) getSession().createCriteria(getPersistentClass())
                .add(Property.forName("type").eq(type))
                .uniqueResult();
    }
}