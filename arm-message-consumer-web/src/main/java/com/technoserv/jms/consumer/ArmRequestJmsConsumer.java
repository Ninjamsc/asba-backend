package com.technoserv.jms.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jms.core.JmsTemplate;

import javax.transaction.Transactional;
import javax.xml.bind.DatatypeConverter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

/**
 * Created by sergey on 22.11.2016.
 */
@PropertySource("classpath:arm-consumer.properties")
public class ArmRequestJmsConsumer {

    private static final Log log = LogFactory.getLog(ArmRequestJmsConsumer.class);

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

    @Value("${arm-retry.queue.maxRetryCount}")
    private static Integer maxTryCount = 10;

    public void onReceive(String message) {
        if(!saveRequest(message)) {
            jmsTemplate.convertAndSend(new ArmRequestRetryMessage(message));
        }
    }

    public void onReceive(ArmRequestRetryMessage message) {
        if(message.getTryCount()<=maxTryCount) {
            if (!saveRequest(message.getMessage())) {
                message.incTryCount();
                jmsTemplate.convertAndSend(message);
            }
        } else {
            try {
                writeToFile(message);
            } catch (IOException e) {
                log.error(e);
            }
        }
    }

    protected boolean saveRequest(String request) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setDateFormat(DATE_FORMAT);
        try {
            //todo переделать маппинг из очереди 1 в сервиc фоток и Request
            RequestDTO requestDTO = objectMapper.readValue(request, RequestDTO.class);

            String scannedPicture = handlePicture(requestDTO.getScannedPicture());
            String webCamPicture = handlePicture(requestDTO.getWebCameraPicture());
            String scannedGuid = DatatypeConverter.printBase64Binary(UUID.randomUUID().toString().getBytes());
            String webCamGuid = DatatypeConverter.printBase64Binary(UUID.randomUUID().toString().getBytes());

            String scannedPictureURL = photoServiceClient.putPhoto(scannedPicture, scannedGuid);
            String webCamPictureURL = photoServiceClient.putPhoto(webCamPicture, webCamGuid);

            Document webCam = null;
            Document scan = null;

            //TODO Find request to add
            Request requestEntity = requestService.findByOrderNumber(requestDTO.getWfNumber());

            if(requestEntity != null) { //TODO discuss что куда и когда и в каком формате доки
                // TODO соответствие между дто и ентити
                webCam = requestEntity.getCameraDocument();
                scan = requestEntity.getScannedDocument();

            } else {
                requestEntity = new Request();
                requestEntity.setId(requestDTO.getWfNumber());
                if(webCamPictureURL!=null) {
                    webCam = new Document();
                }
                if(scannedPictureURL!=null) {
                    scan = new Document();
                }
            }
            if(requestDTO.getType() == RequestDTO.Type.PREVIEW) {
                if(webCamPictureURL!=null) {
                    if(webCam==null) {
                        webCam = new Document();
                    }
                    webCam.setOrigImageURL(webCamPictureURL);
                    webCam.setDocumentType(documentTypeService.findByType(DocumentType.Type.WEB_CAM));
                }
                if(scannedPictureURL!=null) {
                    if(scan==null) {
                        scan = new Document();
                    }
                    scan.setOrigImageURL(scannedPictureURL);
                    scan.setDocumentType(documentTypeService.findByType(DocumentType.Type.SCANNER));
                }

            } if (requestDTO.getType() == RequestDTO.Type.FULLFRAME) {
                if(webCamPictureURL!=null) {
                    if(webCam==null) {
                        webCam = new Document();
                    }
                    webCam.setFaceSquare(webCamPictureURL);
                    webCam.setDocumentType(documentTypeService.findByType(DocumentType.Type.WEB_CAM));
                }
                if(scannedPictureURL!=null) {
                    if(scan==null) {
                        scan = new Document();
                    }
                    scan.setFaceSquare(scannedPictureURL);
                    scan.setDocumentType(documentTypeService.findByType(DocumentType.Type.SCANNER));
                }
            }

            if(scan!=null) {
                documentService.saveOrUpdate(scan);
            }

            if(webCam!=null) {
                documentService.saveOrUpdate(webCam);
            }
            Person person = personService.findById(requestDTO.getIin());
            if(person==null) {
                person = new Person();
                person.setDossier(new ArrayList<Request>());
            }
            requestEntity.setPerson(person);
            person.setId(requestDTO.getIin());
            person.getDossier().add(requestEntity);
            personService.saveOrUpdate(person);

            requestEntity.setTimestamp(requestDTO.getTimestamp());
            if(webCam!=null) {
                requestEntity.setCameraDocument(webCam);
            }
            if(scan!=null) {
                requestEntity.setScannedDocument(scan);
            }
            requestEntity.setLogin(requestDTO.getUsername());
            requestEntity.setObjectDate(new Date());
            //Todo save request
            requestService.saveOrUpdate(requestEntity);
            return true;
        } catch (IOException e) {
            log.error(e);
            return false;
        }
    }

    private String handlePicture(String picture) { //TODO ...
        if(picture!=null && "".equals(picture.trim())) {
            return null;
        }
//        if(picture.contains("data:image")) {
//            String base64Image = picture.split(",")[1];
//            byte[] imageBytes = javax.xml.bind.DatatypeConverter.parseBase64Binary(base64Image);
//            return new String(imageBytes);
//        } else {
            return picture;
//        }
    }

    private void writeToFile(ArmRequestRetryMessage message) throws IOException {
        File file = new File("arm_req_" + DATE_FORMAT.format(new Date()) + ".txt");
        log.info("create file " + file.getAbsolutePath());
        if (!file.exists()) {
            file.createNewFile();
        }
        FileWriter fileWriter = new FileWriter(file);
        fileWriter.write(message.getMessage());
        fileWriter.close();
    }

}
