package com.technoserv.bio.kernel.dao.objectmodel;

import com.technoserv.db.dao.objectmodel.api.RequestDao;
import com.technoserv.db.model.objectmodel.Request;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import static com.technoserv.bio.kernel.util.TestUtil.generateRequest;
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
        entity.setBpmRequestNumber("13");
        dao.saveOrUpdate(entity);
        assertEquals("13", dao.get(entity.getId()).getBpmRequestNumber());
    }

    @Test
    public void deleteRequest() throws Exception {
        Request entity = generateRequest();
        dao.saveOrUpdate(entity);
        assertNotNull(dao.get(entity.getId()));
        dao.delete(entity);
        assertNull(dao.get(entity.getId()));
    }
}
