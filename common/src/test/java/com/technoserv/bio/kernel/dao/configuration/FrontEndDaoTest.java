package com.technoserv.bio.kernel.dao.configuration;

import com.technoserv.db.dao.configuration.api.FrontEndsDao;
import com.technoserv.db.model.configuration.FrontEnd;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import static com.technoserv.bio.kernel.util.TestUtil.generateFrontEnds;
import static org.junit.Assert.*;

/**
 * Created by VBasakov on 16.11.2016.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/testContext.xml")
@Transactional
public class FrontEndDaoTest {

    //TODO: нужно протестировать массивы объетов класса, когда они появятся

    @Autowired
    FrontEndsDao dao;

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void createFrontEnds() throws Exception {
        FrontEnd frontEnd = dao.get(1L);
        assertNull(frontEnd);
        assertEquals(0, dao.countAll());
        FrontEnd entity = generateFrontEnds();
        dao.saveOrUpdate(entity);
        assertEquals(1, dao.countAll());
        assertEquals(entity, dao.get(entity.getId()));
    }

    @Test
    public void updateFrontEnds() throws Exception {
        FrontEnd entity = generateFrontEnds();
        dao.saveOrUpdate(entity);
        FrontEnd frontEnd = dao.get(entity.getId());
        assertEquals(frontEnd, entity);
        entity.setClientVersion("2");
        dao.saveOrUpdate(entity);
        assertEquals(2, dao.get(entity.getId()).getClientVersion() + 0);
    }

    @Test
    public void deleteFrontEnds() throws Exception {
        FrontEnd entity = generateFrontEnds();
        dao.saveOrUpdate(entity);
        assertNotNull(dao.get(entity.getId()));
        dao.delete(entity);
        assertNull(dao.get(entity.getId()));
    }
}
