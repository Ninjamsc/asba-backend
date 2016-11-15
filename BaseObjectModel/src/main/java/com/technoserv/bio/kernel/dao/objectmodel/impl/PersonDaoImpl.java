package com.technoserv.bio.kernel.dao.objectmodel.impl;

import com.technoserv.bio.kernel.dao.AbstractHibernateDao;
import com.technoserv.bio.kernel.dao.objectmodel.api.PersonDao;
import com.technoserv.bio.kernel.model.objectmodel.Person;
import org.springframework.stereotype.Repository;

/**
 * Created by sergey on 15.11.2016.
 */
@Repository
public class PersonDaoImpl  extends AbstractHibernateDao<Long,Person> implements PersonDao {
}
