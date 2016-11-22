package com.technoserv.bio.kernel.dao.objectmodel;

import com.technoserv.db.dao.objectmodel.api.ContractorDao;
import com.technoserv.db.model.objectmodel.Contractor;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import static com.technoserv.bio.kernel.util.TestUtil.generateContractor;
import static org.junit.Assert.*;

/**
 * Created by VBasakov on 16.11.2016.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/testContext.xml")
@Transactional
public class ContractorDaoTest {

    @Autowired
    ContractorDao dao;

    @Before
    public void setUp(){}

    @After
    public void tearDown(){}

    @Test
    public void createContractor() throws Exception {
        Contractor contractor = dao.get(1L);
        assertNull(contractor);
        assertEquals(0, dao.countAll());
        Contractor entity = generateContractor();
        dao.saveOrUpdate(entity);
        assertEquals(1, dao.countAll());
        assertEquals(entity, dao.get(entity.getId()));
    }

    @Test
    public void updateContractor() throws Exception {
        Contractor entity = generateContractor();
        dao.saveOrUpdate(entity);
        Contractor contractor = dao.get(entity.getId());
        assertEquals(contractor, entity);
        entity.setSimilarityThreshold(33f);
        dao.saveOrUpdate(entity);
        assertEquals(33f, dao.get(entity.getId()).getSimilarityThreshold(), 0);
    }

    @Test
    public void deleteContractor() throws Exception {
        Contractor entity = generateContractor();
        dao.saveOrUpdate(entity);
        assertNotNull(dao.get(entity.getId()));
        dao.delete(entity);
        assertNull(dao.get(entity.getId()));
    }
}
