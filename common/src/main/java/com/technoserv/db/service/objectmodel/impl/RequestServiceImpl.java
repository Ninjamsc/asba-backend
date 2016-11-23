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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
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

    public Request findByOrderNumber(Long id) {
        return dao.findByOrderNumber(id);
    }

    public Collection<Request> findNotProcessed() {
        return dao.findNotProcessed();
    }

    @Transactional
    public Long createOrder(Long iin, String username) {
        Request request = new Request();
        request.setId(iin);
        request.setLogin(username);
        save(request);
        Person person = new Person();
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
        request.setScannedDocument(document);
        saveOrUpdate(request);
    }
}