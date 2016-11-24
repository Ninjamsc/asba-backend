package com.technoserv.db.service.objectmodel.impl;

import com.technoserv.db.dao.objectmodel.api.PersonDao;
import com.technoserv.db.model.objectmodel.Person;
import com.technoserv.db.service.AbstractService;
import com.technoserv.db.service.objectmodel.api.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by sergey on 15.11.2016.
 */
@Service
public class PersonServiceImpl extends AbstractService<Long, Person,PersonDao> implements PersonService {
    @Override
    @Autowired
    @Qualifier("personDao")
    public void setDao(PersonDao dao) {
        this.dao = dao;
    }

    @Transactional(readOnly = true)
    public Person history(Long iin) {
        return dao.history(iin);
    }
}
