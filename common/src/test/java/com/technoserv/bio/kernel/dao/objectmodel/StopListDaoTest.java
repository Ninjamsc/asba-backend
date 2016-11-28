package com.technoserv.bio.kernel.dao.objectmodel;

import com.technoserv.db.dao.objectmodel.api.DocumentDao;
import com.technoserv.db.dao.objectmodel.api.DocumentTypeDao;
import com.technoserv.db.dao.objectmodel.api.StopListDao;
import com.technoserv.db.model.objectmodel.Document;
import com.technoserv.db.model.objectmodel.DocumentType;
import com.technoserv.db.model.objectmodel.StopList;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

import static com.technoserv.bio.kernel.util.TestUtil.generateDocument;
import static com.technoserv.bio.kernel.util.TestUtil.generateStopList;
import static org.junit.Assert.*;

/**
 * Created by VBasakov on 16.11.2016.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/testContext.xml")
@Transactional
public class StopListDaoTest {

    @Autowired
    StopListDao dao;

    @Autowired
    DocumentDao documentDao;

    @Autowired
    DocumentTypeDao documentTypeDao;

    @Before
    public void setUp(){}

    @After
    public void tearDown(){}

    @Test
    public void createStopList() throws Exception {
        StopList stopList = dao.get(1L);
        assertNull(stopList);
        assertEquals(0, dao.countAll());
        StopList entity = generateStopList("List1");
        dao.saveOrUpdate(entity);
        assertEquals(1, dao.countAll());
        assertEquals(entity, dao.get(entity.getId()));
    }

    @Test
    public void updateStopList() throws Exception {
        StopList entity = generateStopList("List1");
        dao.saveOrUpdate(entity);
        StopList stopList = dao.get(entity.getId());
        assertEquals(stopList, entity);
        entity.setStopListName("AnotherStopListName");
        dao.saveOrUpdate(entity);
        assertEquals("AnotherStopListName", dao.get(entity.getId()).getStopListName());
    }

    @Test
    public void deleteStopList() throws Exception {
        StopList entity = generateStopList("List1");
        dao.saveOrUpdate(entity);
        assertNotNull(dao.get(entity.getId()));
        dao.delete(entity);
        assertNull(dao.get(entity.getId()));
    }


    @Test
    public void testGetAll() throws Exception {
        StopList entity = generateStopList("List1");
        dao.saveOrUpdate(entity);
        entity.setOwner(new ArrayList<Document>());
        DocumentType documentType = documentTypeDao.findByType(DocumentType.Type.ANY);
        for (int i = 0; i < 10; i++) {
            entity.getOwner().add(generateDocument(documentType));
        }
        StopList entity2 = generateStopList("List2");
        entity2.setOwner(new ArrayList<Document>());
        for (int i = 0; i < 5; i++) {
            entity2.getOwner().add(generateDocument(documentType));
        }
        dao.saveOrUpdate(entity2);


        Assert.assertEquals(2,dao.countAll());
    }
}
