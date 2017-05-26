package com.technoserv.bio.kernel;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.technoserv.db.model.objectmodel.Document;
import com.technoserv.db.model.objectmodel.DocumentType;
import com.technoserv.db.model.objectmodel.Person;
import com.technoserv.db.model.objectmodel.Request;
import com.technoserv.db.service.objectmodel.api.DocumentService;
import com.technoserv.db.service.objectmodel.api.DocumentTypeService;
import com.technoserv.db.service.objectmodel.api.PersonService;
import com.technoserv.db.service.objectmodel.api.RequestService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Adrey on 22.11.2016.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/applicationContext.xml")
@Transactional
@ActiveProfiles({"test"})
public class RequestProcessorIT {//todo mock jmsTemplate and rename to RequestProcessorTest

    @Autowired
    private RequestProcessor requestProcessor;
    @Autowired
    private RequestService requestService;
    @Autowired
    private DocumentTypeService documentTypeService;
    @Autowired
    private DocumentService documentService;
    @Autowired
    private PersonService personService;

    @Before
    public void before() throws Exception {
        String request = TestUtils.readFile("request-fullframe-1.json");
        createData(request);
        String request2 = TestUtils.readFile("request-preview-1.json");
        createData(request2);
    }

    @Test
    public void process() {
        requestProcessor.setPhotoAnalyzerServiceRestClient(new MockPhotoAnalyzerServiceClient());
        requestProcessor.setTemplateBuilderServiceRestClient(new MockTemplateBuilderServiceRestClient());
        requestProcessor.setСompareServiceRestClient(new MockCompareServiceClient());
        requestProcessor.setPhotoPersistServiceRestClient(new MockPhotoServiceClient());

        requestProcessor.process();

        Long wfmId = 13169L;
        Request request = requestService.findByOrderNumber(wfmId);

        Assert.assertNotNull(request.getScannedDocument().getBioTemplates());
        Assert.assertNotNull(request.getCameraDocument().getBioTemplates());
        Assert.assertEquals(Request.Status.SUCCESS, request.getStatus());
    }

    private void createData(String request) throws Exception {
        RequestDTO requestDTO = new ObjectMapper().readValue(request, RequestDTO.class);

        String scannedPictureURL = "scanUrl";
        String webCamPictureURL = "webCamUrl";

        Document webCam;
        Document scan;
        Request requestEntity = requestService.findByOrderNumber(requestDTO.getWfNumber());
        if (requestEntity != null) { //TODO discuss что куда и когда и в каком формате доки
            // TODO соответствие между дто и ентити
            webCam = requestEntity.getCameraDocument();
            scan = requestEntity.getScannedDocument();
        } else {
            requestEntity = new Request();
            requestEntity.setId(requestDTO.getWfNumber());
            webCam = new Document();
            scan = new Document();
        }
        if (requestDTO.getType() == RequestDTO.Type.PREVIEW) {
            webCam.setOrigImageURL(webCamPictureURL);
            webCam.setDocumentType(documentTypeService.findByType(DocumentType.Type.WEB_CAM));

            scan.setOrigImageURL(scannedPictureURL);
            scan.setDocumentType(documentTypeService.findByType(DocumentType.Type.SCANNER));
        }
        if (requestDTO.getType() == RequestDTO.Type.FULLFRAME) {
            webCam.setFaceSquare(webCamPictureURL);
            webCam.setDocumentType(documentTypeService.findByType(DocumentType.Type.WEB_CAM));

            scan.setFaceSquare(scannedPictureURL);
            scan.setDocumentType(documentTypeService.findByType(DocumentType.Type.SCANNER));
        }
        documentService.saveOrUpdate(scan);
        documentService.saveOrUpdate(webCam);
        Person person = personService.findById(requestDTO.getIin());
        if (person == null) {
            person = new Person();
            person.setDossier(new ArrayList<Request>());
        }
        requestEntity.setPerson(person);
        person.setId(requestDTO.getIin());
        person.getDossier().add(requestEntity);
        personService.saveOrUpdate(person);

        requestEntity.setTimestamp(requestDTO.getTimestamp());
        requestEntity.setCameraDocument(webCam);
        requestEntity.setScannedDocument(scan);
        requestEntity.setLogin(requestDTO.getUsername());
        requestEntity.setObjectDate(new Date());
        //Todo save request
        requestService.saveOrUpdate(requestEntity);
    }
}