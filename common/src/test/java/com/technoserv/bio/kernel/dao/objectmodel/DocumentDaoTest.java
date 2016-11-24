package com.technoserv.bio.kernel.dao.objectmodel;

import com.technoserv.db.dao.objectmodel.api.DocumentDao;
import com.technoserv.db.dao.objectmodel.api.DocumentTypeDao;
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
    private DocumentDao dao;
    @Autowired
    private DocumentTypeDao documentTypeDao;

    @Before
    public void setUp(){
//        documentTypeDao.save(new DocumentType(DocumentType.Type.ANY, "sfsdfdsf"));
    }

    @After
    public void tearDown(){}

    @Test
    public void createDocument() throws Exception {
        DocumentType documentType = documentTypeDao.findByType(DocumentType.Type.ANY);
        Document document = dao.get(1L);
        assertNull(document);
        assertEquals(0, dao.countAll());
        Document entity = generateDocument(documentType);
        entity.setDocumentType(documentType);
        dao.saveOrUpdate(entity);
        assertEquals(1, dao.countAll());
        assertEquals(entity, dao.get(entity.getId()));
        assertEquals(DocumentType.Type.ANY.getValue(), entity.getDocumentType().getType().getValue());
    }

    @Test
    public void updateDocument() throws Exception {
        DocumentType documentType = documentTypeDao.findByType(DocumentType.Type.ANY);
        Document entity = generateDocument(documentType);
        dao.saveOrUpdate(entity);
        Document document = dao.get(entity.getId());
        assertEquals(document, entity);
//        entity.setDocumentType(DocumentType.EXTERNAL);
//        dao.saveOrUpdate(entity);
//        assertEquals(DocumentType.EXTERNAL, dao.get(entity.getId()).getDocumentType());
    }

    @Test
    public void deleteDocument() throws Exception {
        DocumentType documentType = documentTypeDao.findByType(DocumentType.Type.ANY);
        Document entity = generateDocument(documentType);
        dao.saveOrUpdate(entity);
        assertNotNull(dao.get(entity.getId()));
        dao.delete(entity);
        assertNull(dao.get(entity.getId()));
    }
}
