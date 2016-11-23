package com.technoserv.db.dao.objectmodel.impl;

import com.technoserv.db.dao.AbstractHibernateDao;
import com.technoserv.db.dao.objectmodel.api.PersonDao;
import com.technoserv.db.model.objectmodel.Person;
import org.hibernate.Criteria;
import org.hibernate.criterion.Property;
import org.hibernate.sql.JoinType;
import org.springframework.stereotype.Repository;

/**
 * Created by sergey on 15.11.2016.
 */
@Repository("personDao")
public class PersonDaoImpl extends AbstractHibernateDao<Long,Person> implements PersonDao {

    public Person history(Long iin) {
        Criteria criteria = getSession().createCriteria(getPersistentClass()).add(Property.forName("id").eq(iin));
        criteria.createCriteria("dossier","d", JoinType.INNER_JOIN);
        return (Person) criteria.uniqueResult();
    }
}
