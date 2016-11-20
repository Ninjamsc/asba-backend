package com.technoserv.bio.kernel.dao.configuration;

import com.technoserv.bio.kernel.dao.configuration.api.FrontEndConfigurationDao;
import com.technoserv.bio.kernel.model.configuration.FrontEndConfiguration;
import org.junit.After;

import static com.technoserv.bio.kernel.util.TestUtil.generateFrontEndConfiguration;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * Created by VBasakov on 15.11.2016.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/testContext.xml")
@Transactional
public class FrontEndConfigurationDaoTest {

    @Autowired
    FrontEndConfigurationDao dao;

    @Before
    public void setUp(){}

    @After
    public void tearDown(){}

    @Test
    public void createFrontEndConfiguration() throws Exception {
        FrontEndConfiguration frontEndConfiguration = dao.get(1L);
        assertNull(frontEndConfiguration);
        assertEquals(0, dao.countAll());
        FrontEndConfiguration entity = generateFrontEndConfiguration();
        dao.saveOrUpdate(entity);
        assertEquals(1, dao.countAll());
        assertEquals(entity, dao.get(entity.getId()));
    }

    @Test
    public void updateFrontEndConfiguration() throws Exception {
        FrontEndConfiguration entity = generateFrontEndConfiguration();
        dao.saveOrUpdate(entity);
        FrontEndConfiguration frontEndConfiguration = dao.get(entity.getId());
        assertEquals(frontEndConfiguration, entity);
        entity.setVersion(2);
        dao.saveOrUpdate(entity);
        assertEquals(2, dao.get(entity.getId()).getVersion()+0);
    }

    @Test
    public void deleteFrontEndConfiguration() throws Exception {
        FrontEndConfiguration entity = generateFrontEndConfiguration();
        dao.saveOrUpdate(entity);
        assertNotNull(dao.get(entity.getId()));
        dao.delete(entity);
        assertNull(dao.get(entity.getId()));
    }

}
