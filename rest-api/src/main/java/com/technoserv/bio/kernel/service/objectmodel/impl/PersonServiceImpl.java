package com.technoserv.bio.kernel.service.objectmodel.impl;

import com.technoserv.bio.kernel.dao.objectmodel.api.PersonDao;
import com.technoserv.bio.kernel.model.objectmodel.Person;
import com.technoserv.bio.kernel.service.AbstractService;
import com.technoserv.bio.kernel.service.objectmodel.api.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

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
}