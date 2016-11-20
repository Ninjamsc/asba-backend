package com.technoserv.bio.kernel.dao.configuration;

import com.technoserv.bio.kernel.dao.configuration.api.FrontEndsDao;
import com.technoserv.bio.kernel.model.configuration.FrontEnds;
import org.junit.After;

import static com.technoserv.bio.kernel.util.TestUtil.generateFrontEnds;
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
public class FrontEndsDaoTest {

    //TODO: нужно протестировать массивы объетов класса, когда они появятся

    @Autowired
    FrontEndsDao dao;

    @Before
    public void setUp(){}

    @After
    public void tearDown(){}

    @Test
    public void createFrontEnds() throws Exception {
        FrontEnds frontEnds = dao.get(1L);
        assertNull(frontEnds);
        assertEquals(0, dao.countAll());
        FrontEnds entity = generateFrontEnds();
        dao.saveOrUpdate(entity);
        assertEquals(1, dao.countAll());
        assertEquals(entity, dao.get(entity.getId()));
    }

    @Test
    public void updateFrontEnds() throws Exception {
        FrontEnds entity = generateFrontEnds();
        dao.saveOrUpdate(entity);
        FrontEnds frontEnds = dao.get(entity.getId());
        assertEquals(frontEnds, entity);
        entity.setVersion(2);
        dao.saveOrUpdate(entity);
        assertEquals(2, dao.get(entity.getId()).getVersion()+0);
    }

    @Test
    public void deleteFrontEnds() throws Exception {
        FrontEnds entity = generateFrontEnds();
        dao.saveOrUpdate(entity);
        assertNotNull(dao.get(entity.getId()));
        dao.delete(entity);
        assertNull(dao.get(entity.getId()));
    }
}
