package com.technoserv.bio.kernel.dao.objectmodel;

import com.technoserv.bio.kernel.dao.objectmodel.api.BPMReportDao;
import com.technoserv.bio.kernel.dao.objectmodel.api.DocumentDao;
import com.technoserv.bio.kernel.model.objectmodel.BPMReport;
import org.junit.After;

import static com.technoserv.bio.kernel.util.TestUtil.generateBPMReport;
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
 * Created by VBasakov on 16.11.2016.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/testContext.xml")
@Transactional
public class BPMReportDaoTest {

    @Autowired
    BPMReportDao reportDao;

    @Autowired
    DocumentDao documentDao;

    @Before
    public void setUp(){}

    @After
    public void tearDown(){}

    @Test
    public void createBPMReport() throws Exception {
        BPMReport report = reportDao.get(1L);
        assertNull(report);
        assertEquals(0, reportDao.countAll());
        assertEquals(0, documentDao.countAll());
        BPMReport entity = generateBPMReport();
        reportDao.saveOrUpdate(entity);
        /*reportDao.flush(); //todo тестирование сложных сущностей
        assertEquals(1, reportDao.countAll());
        assertEquals(2, documentDao.countAll());
        assertNotNull(documentDao.get(entity.getPhoto().getId()));
        assertNotNull(documentDao.get(entity.getScan().getId()));*/
        assertEquals(entity, reportDao.get(entity.getId()));
    }

    @Test
    public void updateBPMReport() throws Exception {
        BPMReport entity = generateBPMReport();
        reportDao.saveOrUpdate(entity);
        BPMReport report = reportDao.get(entity.getId());
        assertEquals(report, entity);
        Date date = new Date();
        entity.setObjectDate(date);
        reportDao.saveOrUpdate(entity);
        assertEquals(date, reportDao.get(entity.getId()).getObjectDate());
    }

    @Test
    public void deleteBPMReport() throws Exception {
        BPMReport entity = generateBPMReport();
        reportDao.saveOrUpdate(entity);
        assertNotNull(reportDao.get(entity.getId()));
        reportDao.delete(entity);
        assertNull(reportDao.get(entity.getId()));
    }
}
