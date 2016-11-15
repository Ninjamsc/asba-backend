package com.technoserv.bio.kernel.dao.objectmodel;

import com.technoserv.bio.kernel.dao.objectmodel.api.PersonDao;
import com.technoserv.bio.kernel.model.objectmodel.Person;
import org.junit.After;

import static com.technoserv.bio.kernel.util.TestUtil.generatePerson;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by VBasakov on 16.11.2016.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/testContext.xml")
@Transactional
public class PersonDaoTest {

    @Autowired
    PersonDao dao;

    @Before
    public void setUp(){}

    @After
    public void tearDown(){}

    @Test
    public void createPerson() throws Exception {
        Person person = dao.get(1L);
        assertNull(person);
        assertEquals(0, dao.countAll());
        Person entity = generatePerson();
        dao.saveOrUpdate(entity);
        assertEquals(1, dao.countAll());
        assertEquals(entity, dao.get(entity.getId()));
    }

    @Test
    public void updatePerson() throws Exception {
        Person entity = generatePerson();
        dao.saveOrUpdate(entity);
        Person person = dao.get(entity.getId());
        assertEquals(person, entity);
        entity.setPersonNumber("13");
        dao.saveOrUpdate(entity);
        assertEquals("13", dao.get(entity.getId()).getPersonNumber());
    }

    @Test
    public void deletePerson() throws Exception {
        Person entity = generatePerson();
        dao.saveOrUpdate(entity);
        assertNotNull(dao.get(entity.getId()));
        dao.delete(entity);
        assertNull(dao.get(entity.getId()));
    }
}
