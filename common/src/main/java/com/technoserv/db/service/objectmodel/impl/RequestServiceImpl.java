package com.technoserv.db.service.objectmodel.impl;

import com.technoserv.db.dao.objectmodel.api.RequestDao;
import com.technoserv.db.model.objectmodel.Document;
import com.technoserv.db.model.objectmodel.DocumentType;
import com.technoserv.db.model.objectmodel.Person;
import com.technoserv.db.model.objectmodel.Request;
import com.technoserv.db.service.AbstractService;
import com.technoserv.db.service.objectmodel.api.DocumentService;
import com.technoserv.db.service.objectmodel.api.DocumentTypeService;
import com.technoserv.db.service.objectmodel.api.PersonService;
import com.technoserv.db.service.objectmodel.api.RequestService;
import com.technoserv.utils.HibernateInitializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

/**
 * Created by sergey on 15.11.2016.
 */
@Service
public class RequestServiceImpl extends AbstractService<Long, Request,RequestDao> implements RequestService {

    @Autowired
    private PersonService personService;
    @Autowired
    private DocumentService documentService;
    @Autowired
    private DocumentTypeService documentTypeService;

    @Override
    @Autowired
    @Qualifier("requestDao")
    public void setDao(RequestDao dao) {
        this.dao = dao;
    }

    @Transactional(readOnly = true)
    public Request findByOrderNumber(Long id) {
        return dao.findByOrderNumber(id);
    }

    @Transactional(readOnly = true)
    public Collection<Request> findByIin(Long id,String... properties) {

        Collection<Request> result = dao.findByIin(id);
        HibernateInitializer.initializeProperties(result, properties);

        return result;
    }
    @Transactional(readOnly = true)
    public Collection<Request> findNotProcessed() {
        return dao.findNotProcessed();
    }

    @Transactional
    public Long createOrder(Long iin, Long wfmId, String username) {
        Request request = new Request();
        Person person = personService.findById(iin);
        person = person != null ? person : new Person();
        request.setId(wfmId);
        request.setLogin(username);
        request.setPerson(person);
        save(request);
        person.setId(iin);
        person.getDossier().add(request);
        return personService.save(person);
    }

    @Transactional
    public void updateDocument(Long wfmId, String previewUrl,
                               String fullFrameUrl, DocumentType.Type type) {
        Request request = findById(wfmId);
        Document document = new Document();
        document.setOrigImageURL(previewUrl);
        document.setFaceSquare(fullFrameUrl);
        document.setDocumentType(documentTypeService.findByType(type));
        documentService.save(document);
        if(DocumentType.Type.SCANNER == type){
            request.setScannedDocument(document);
        } else if (DocumentType.Type.WEB_CAM == type) {
            request.setCameraDocument(document);
        }
        saveOrUpdate(request);
    }
}