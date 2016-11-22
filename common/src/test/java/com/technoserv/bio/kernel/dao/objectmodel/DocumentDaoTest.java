package com.technoserv.bio.kernel.dao.objectmodel;

import com.technoserv.db.dao.objectmodel.api.DocumentDao;
import com.technoserv.db.model.objectmodel.Document;
import com.technoserv.db.model.objectmodel.DocumentType;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import static com.technoserv.bio.kernel.util.TestUtil.generateDocument;
import static org.junit.Assert.*;

/**
 * Created by VBasakov on 16.11.2016.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/testContext.xml")
@Transactional
public class DocumentDaoTest {

    @Autowired
    DocumentDao dao;

    @Before
    public void setUp(){}

    @After
    public void tearDown(){}

    @Test
    public void createDocument() throws Exception {
        Document document = dao.get(1L);
        assertNull(document);
        assertEquals(0, dao.countAll());
        Document entity = generateDocument();
        dao.saveOrUpdate(entity);
        assertEquals(1, dao.countAll());
        assertEquals(entity, dao.get(entity.getId()));
    }

    @Test
    public void updateDocument() throws Exception {
        Document entity = generateDocument();
        dao.saveOrUpdate(entity);
        Document document = dao.get(entity.getId());
        assertEquals(document, entity);
        entity.setDocumentType(DocumentType.EXTERNAL);
        dao.saveOrUpdate(entity);
        assertEquals(DocumentType.EXTERNAL, dao.get(entity.getId()).getDocumentType());
    }

    @Test
    public void deleteDocument() throws Exception {
        Document entity = generateDocument();
        dao.saveOrUpdate(entity);
        assertNotNull(dao.get(entity.getId()));
        dao.delete(entity);
        assertNull(dao.get(entity.getId()));
    }
}
