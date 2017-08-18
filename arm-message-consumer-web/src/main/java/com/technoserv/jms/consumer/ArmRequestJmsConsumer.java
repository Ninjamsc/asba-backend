package com.technoserv.jms.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Strings;
import com.technoserv.db.model.objectmodel.Document;
import com.technoserv.db.model.objectmodel.DocumentType;
import com.technoserv.db.model.objectmodel.Person;
import com.technoserv.db.model.objectmodel.Request;
import com.technoserv.db.service.objectmodel.api.DocumentService;
import com.technoserv.db.service.objectmodel.api.DocumentTypeService;
import com.technoserv.db.service.objectmodel.api.PersonService;
import com.technoserv.db.service.objectmodel.api.RequestService;
import com.technoserv.jms.trusted.ArmRequestRetryMessage;
import com.technoserv.jms.trusted.RequestDTO;
import com.technoserv.rest.client.PhotoPersistServiceRestClient;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.web.client.RestTemplate;

import javax.xml.bind.DatatypeConverter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.UUID;

/**
 * Created by sergey on 22.11.2016.
 */
public class ArmRequestJmsConsumer {

    private static final Logger log = LoggerFactory.getLogger(ArmRequestJmsConsumer.class);

    @Autowired
    private JmsTemplate jmsTemplate;

    @Autowired
    private RequestService requestService;

    @Autowired
    private PersonService personService;

    @Autowired
    private PhotoPersistServiceRestClient photoServiceClient;

    @Autowired
    private DocumentService documentService;

    @Autowired
    private DocumentTypeService documentTypeService;

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");

    public void onReceive(String message) {
        log.debug("onReceive message length: {}", StringUtils.length(message));
        if (!saveRequest(message)) {
            jmsTemplate.convertAndSend(new ArmRequestRetryMessage(message));
        }
    }

    private boolean saveRequest(String request) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setDateFormat(DATE_FORMAT);
        try {
            RequestDTO requestDTO = objectMapper.readValue(request, RequestDTO.class);
            //log.debug("Request: {} comes from workstation queue: {}", requestDTO.getWfNumber(), request);

            Request requestEntity = requestService.findByOrderNumber(requestDTO.getWfNumber());

            // do not save request which was already processed
            if (requestEntity != null) {
                log.warn("Request: {} already exists in the database: {}", requestEntity.getId(), requestEntity);
                if ((requestEntity.getStatus() == Request.Status.SUCCESS && requestDTO.getType() != RequestDTO.Type.FULLFRAME)
                        || requestEntity.getStatus() == Request.Status.FAILED
                        || requestEntity.getStatus() == Request.Status.REJECTED) {

                    log.warn("Request: {} is process already and now its status: {}. Request will NOT be processed again.",
                            requestEntity.getId(), requestEntity.getStatus());

                    return true;
                }
            }

            String scannedPicture = handlePicture(requestDTO.getScannedPicture());
            String webCamPicture = handlePicture(requestDTO.getWebCameraPicture());

            String scannedUuid = DatatypeConverter.printBase64Binary(UUID.randomUUID().toString().getBytes());
            String webCamUuid = DatatypeConverter.printBase64Binary(UUID.randomUUID().toString().getBytes());

            String scannedPictureURL = photoServiceClient.putPhoto(scannedPicture, scannedUuid);
            String webCamPictureURL = photoServiceClient.putPhoto(webCamPicture, webCamUuid);

            log.info("scannedPictureURL: {} ", scannedPictureURL);
            log.info("webCamPictureURL: {} ", webCamPictureURL);

            Document webCam;
            Document scan;

            if (requestEntity != null) {
                webCam = requestEntity.getCameraDocument();
                scan = requestEntity.getScannedDocument();

            } else {
                requestEntity = new Request();
                requestEntity.setId(requestDTO.getWfNumber());
                webCam = new Document();
                scan = new Document();
            }

            if (requestDTO.getType() == RequestDTO.Type.FULLFRAME) {
                if (webCamPictureURL != null) {
                    webCam.setOrigImageURL(webCamPictureURL);
                }
                webCam.setDocumentType(documentTypeService.findByType(DocumentType.Type.WEB_CAM));
                if (scannedPictureURL != null) {
                    scan.setOrigImageURL(scannedPictureURL);
                }
                scan.setDocumentType(documentTypeService.findByType(DocumentType.Type.SCANNER));
            }

            if (requestDTO.getType() == RequestDTO.Type.PREVIEW) {
                if (webCamPictureURL != null) {
                    webCam.setFaceSquare(webCamPictureURL);
                }
                webCam.setDocumentType(documentTypeService.findByType(DocumentType.Type.WEB_CAM));
                if (scannedPictureURL != null) {
                    scan.setFaceSquare(scannedPictureURL);
                }
                scan.setDocumentType(documentTypeService.findByType(DocumentType.Type.SCANNER));
            }

            documentService.saveOrUpdate(scan);
            documentService.saveOrUpdate(webCam);

            Person person = personService.findById(requestDTO.getIin());
            if (person == null) {
                person = new Person();
                person.setDossier(new ArrayList<>());
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
            //TODO: Проверка на Status.SUCCESS если обработано, то не меняем, но если FAILED то запускаем снова
            requestEntity.setStatus(Request.Status.SAVED);
            requestService.saveOrUpdate(requestEntity);
            RestTemplate restTemplate = new RestTemplate();
            //restTemplate.put("http://localhost:9080/client/rest/dequeue/"+requestEntity.getId(),new HashMap<>());
            //CompareReport report = restTemplate.getForEntity("http://localhost:9080/client/rest/dequeue/"+requestEntity.getId(),CompareReport.class).getBody();

            return true;
        } catch (IOException e) {
            log.error(String.format("Can't save request: %s", request), e);
            return false;
        }
    }

    private String handlePicture(String picture) {
        if (Strings.isNullOrEmpty(picture)) {
            return null;
        }
        return picture;
    }

}
