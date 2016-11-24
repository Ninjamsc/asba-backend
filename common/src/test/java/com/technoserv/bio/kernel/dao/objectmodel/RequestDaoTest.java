package com.technoserv.bio.kernel.dao.objectmodel;

import com.technoserv.db.dao.objectmodel.api.PersonDao;
import com.technoserv.db.dao.objectmodel.api.RequestDao;
import com.technoserv.db.model.objectmodel.Person;
import com.technoserv.db.model.objectmodel.Request;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.*;

/**
 * Created by VBasakov on 16.11.2016.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/testContext.xml")
@Transactional
public class RequestDaoTest {

    @Autowired
    RequestDao dao;
    @Autowired
    PersonDao personDao;

    @Before
    public void setUp(){}

    @After
    public void tearDown(){}

    @Test
    public void createRequest() throws Exception {
        Request request = dao.get(1L);
        assertNull(request);
        assertEquals(0, dao.countAll());
        Request entity = generateRequest();
        dao.saveOrUpdate(entity);
        assertEquals(1, dao.countAll());
        assertEquals(entity, dao.get(entity.getId()));
    }

    @Test
    public void updateRequest() throws Exception {
        Request entity = generateRequest();
        dao.saveOrUpdate(entity);
        Request request = dao.get(entity.getId());
        assertEquals(request, entity);
        entity.setId(1L);
        dao.saveOrUpdate(entity);
        assertEquals(1L, dao.get(entity.getId()).getId().longValue());
    }

    @Test
    public void deleteRequest() throws Exception {
        Request entity = generateRequest();
        dao.saveOrUpdate(entity);
        assertNotNull(dao.get(entity.getId()));
        dao.delete(entity);
        assertNull(dao.get(entity.getId()));
    }

    @Test
    public void saveWithPerson() {
        Request request = new Request();
        request.setId(111L);
        request.setLogin("username");
        dao.save(request);
        Person person = new Person();
        person.setId(555L);
        person.getDossier().add(request);
        personDao.save(person);
    }

    @Test
    public void findTest() {
        Request request = new Request();
        request.setId(111L);
        request.setLogin("username");
        dao.save(request);
        Request result = dao.findByOrderNumber(111L);
        Assert.assertEquals(111L, result.getId().longValue());
    }

    public static Request generateRequest(){
        Request result = new Request();
        result.setId(1L);
        result.setInsUser("1");
        result.setLogin("1");
        result.setStatus(Request.Status.SAVED);
//        result.setCameraDocument(generateDocument());
//        result.setScannedDocument(generateDocument());
        return result;
    }
}
