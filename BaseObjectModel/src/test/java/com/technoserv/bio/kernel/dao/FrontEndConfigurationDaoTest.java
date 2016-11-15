package com.technoserv.bio.kernel.dao;

import com.technoserv.bio.kernel.dao.configuration.api.FrontEndConfigurationDao;
import com.technoserv.bio.kernel.model.configuration.FrontEndConfiguration;
import org.junit.After;
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
    public void create() throws Exception {
        FrontEndConfiguration frontEndConfiguration = dao.get(1L);
        assertNull(frontEndConfiguration);
        assertEquals(0, dao.countAll());
        FrontEndConfiguration entity = createFrontEndConfiguration();
        dao.saveOrUpdate(entity);
        assertEquals(1, dao.countAll());
        assertEquals(entity, dao.get(entity.getId()));
    }

    @Test
    public void update() throws Exception {
        FrontEndConfiguration entity = createFrontEndConfiguration();
        dao.saveOrUpdate(entity);
        FrontEndConfiguration frontEndConfiguration = dao.get(entity.getId());
        assertEquals(frontEndConfiguration, entity);
        entity.setVersion(2);
        dao.saveOrUpdate(entity);
        assertEquals(2, dao.get(entity.getId()).getVersion()+0);
    }

    @Test
    public void delete() throws Exception {
        FrontEndConfiguration entity = createFrontEndConfiguration();
        dao.saveOrUpdate(entity);
        assertNotNull(dao.get(entity.getId()));
        dao.delete(entity);
        assertNull(dao.get(entity.getId()));
    }

    FrontEndConfiguration createFrontEndConfiguration(){
        FrontEndConfiguration result = new FrontEndConfiguration();
        result.setVersion(1);
        result.setObjectDate(new Date());
        return result;
    }
}
