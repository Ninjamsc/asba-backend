package com.technoserv.jms.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.technoserv.db.model.objectmodel.Document;
import com.technoserv.db.model.objectmodel.DocumentType;
import com.technoserv.db.model.objectmodel.Request;
import com.technoserv.db.service.objectmodel.api.DocumentService;
import com.technoserv.db.service.objectmodel.api.DocumentTypeService;
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
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * Created by sergey on 22.11.2016.
 */
//@PropertySource("classpath:arm-consumer.properties")
public class ArmRequestJmsConsumer {

    private static final Log log = LogFactory.getLog(ArmRequestJmsConsumer.class);

    @Autowired
    private JmsTemplate jmsTemplate;

    @Autowired
    private RequestService requestService;

    @Autowired
    private PhotoPersistServiceRestClient photoServiceClient;
    @Autowired
    private DocumentService documentService;
    @Autowired
    private DocumentTypeService documentTypeService;

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd.MM.yyyy_hh_mm_ss_SSSSSS");

//    @Value("${arm-retry.queue.maxRetryCount}")
    //TODO fix me
    private static Integer maxTryCount = 10;

    @Transactional
    public void onReceive(String message) {
        if(!saveRequest(message)) {
            jmsTemplate.convertAndSend(new ArmRequestRetryMessage(message));
        }
    }

    @Transactional
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
        try {
            //todo переделать маппинг из очереди 1 в сервиc фоток и Request
            RequestDTO requestDTO = objectMapper.readValue(request, RequestDTO.class);

            String scannedPicture = handlePicture(requestDTO.getScannedPicture());
            String webCamPicture = handlePicture(requestDTO.getWebCameraPicture());
            String scannedGuid = DatatypeConverter.printBase64Binary(UUID.randomUUID().toString().getBytes());
            String webCamGuid = DatatypeConverter.printBase64Binary(UUID.randomUUID().toString().getBytes());

            String scannedPictureURL = photoServiceClient.putPhoto(requestDTO.getTimestamp().toString(), scannedPicture, scannedGuid);
            String webCamPictureURL = photoServiceClient.putPhoto(requestDTO.getTimestamp().toString(), webCamPicture, webCamGuid);
            Document webCam;
            Document scan;
            //TODO Find request to add
            Request requestEntity = requestService.findByOrderNumber(requestDTO.getWfNumber());
            if(requestEntity != null) { //TODO discuss что куда и когда и в каком формате доки
                // TODO соответствие между дто и ентити
                webCam = requestEntity.getCameraDocument();
                scan = requestEntity.getScannedDocument();
            } else {
                requestEntity = new Request();
                webCam = new Document();
                scan = new Document();
            }
            if(requestDTO.getType() == RequestDTO.Type.PREVIEW) {
                webCam.setOrigImageURL(webCamPictureURL);
                webCam.setDocumentType(documentTypeService.findByType(DocumentType.Type.WEB_CAM));

                scan.setOrigImageURL(scannedPictureURL);
                scan.setDocumentType(documentTypeService.findByType(DocumentType.Type.SCANNER));
            } if (requestDTO.getType() == RequestDTO.Type.FULLFRAME) {
                webCam.setFaceSquare(requestDTO.getWebCameraPicture());
                webCam.setDocumentType(documentTypeService.findByType(DocumentType.Type.WEB_CAM));

                scan.setFaceSquare(scannedPictureURL);
                scan.setDocumentType(documentTypeService.findByType(DocumentType.Type.SCANNER));
            }

            requestEntity.setTimestamp(requestDTO.getTimestamp());
            requestEntity.setCameraDocument(webCam);
            requestEntity.setScannedDocument(scan);
            requestEntity.setId(requestDTO.getWfNumber());
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
        if(picture.contains("data:image")) {
            String base64Image = picture.split(",")[1];
            byte[] imageBytes = javax.xml.bind.DatatypeConverter.parseBase64Binary(base64Image);
            return new String(imageBytes);
        } else {
            return picture;
        }
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
