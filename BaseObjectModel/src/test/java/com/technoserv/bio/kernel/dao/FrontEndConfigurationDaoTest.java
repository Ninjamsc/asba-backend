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

/**
 * Created by VBasakov on 15.11.2016.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/testContext.xml")
public class FrontEndConfigurationDaoTest {

    @Autowired
    FrontEndConfigurationDao dao;

    @Before
    private void setUp(){}

    @After
    private void tearDown(){}

    @Test
    public void create() throws Exception {
        assertEquals(0, dao.countAll());
        FrontEndConfiguration entity = createFrontEndConfiguration();
        Long id = dao.save(entity);
        assertEquals(1, dao.countAll());
        assertEquals(entity, dao.findById(id));
    }

    FrontEndConfiguration createFrontEndConfiguration(){
        FrontEndConfiguration result = new FrontEndConfiguration();
        result.setVersion(1);
        return result;
    }
}
