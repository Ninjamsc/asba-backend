package com.technoserv.db.dao.objectmodel.impl;

import com.technoserv.db.dao.AbstractHibernateDao;
import com.technoserv.db.dao.objectmodel.api.PersonDao;
import com.technoserv.db.model.objectmodel.Person;
import org.springframework.stereotype.Repository;

/**
 * Created by sergey on 15.11.2016.
 */
@Repository("personDao")
public class PersonDaoImpl  extends AbstractHibernateDao<Long,Person> implements PersonDao {
}
