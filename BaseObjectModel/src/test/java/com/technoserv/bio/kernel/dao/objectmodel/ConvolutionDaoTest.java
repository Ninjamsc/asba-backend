package com.technoserv.bio.kernel.dao.objectmodel;

import com.technoserv.bio.kernel.dao.objectmodel.api.ConvolutionDao;
import com.technoserv.bio.kernel.model.objectmodel.Convolution;
import org.junit.After;

import static com.technoserv.bio.kernel.util.TestUtil.generateConvolution;
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
public class ConvolutionDaoTest {

    @Autowired
    ConvolutionDao dao;

    @Before
    public void setUp(){}

    @After
    public void tearDown(){}

    @Test
    public void createConvolution() throws Exception {
        Convolution convolution = dao.get(1L);
        assertNull(convolution);
        assertEquals(0, dao.countAll());
        Convolution entity = generateConvolution();
        dao.saveOrUpdate(entity);
        assertEquals(1, dao.countAll());
        assertEquals(entity, dao.get(entity.getId()));
    }

    @Test
    public void updateConvolution() throws Exception {
        Convolution entity = generateConvolution();
        dao.saveOrUpdate(entity);
        Convolution convolution = dao.get(entity.getId());
        assertEquals(convolution, entity);
        entity.setCnnVersion(13);
        dao.saveOrUpdate(entity);
        assertEquals(13, dao.get(entity.getId()).getCnnVersion());
    }

    @Test
    public void deleteConvolution() throws Exception {
        Convolution entity = generateConvolution();
        dao.saveOrUpdate(entity);
        assertNotNull(dao.get(entity.getId()));
        dao.delete(entity);
        assertNull(dao.get(entity.getId()));
    }
}
