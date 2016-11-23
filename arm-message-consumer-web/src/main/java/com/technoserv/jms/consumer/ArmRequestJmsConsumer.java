package com.technoserv.jms.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.technoserv.db.model.objectmodel.Document;
import com.technoserv.db.model.objectmodel.DocumentType;
import com.technoserv.db.model.objectmodel.Request;
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
    private PhotoPersistServiceRestClient photoServiceClient;

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd.MM.yyyy_hh_mm_ss_SSSSSS");

    @Value("${arm-retry.queue.maxRetryCount}")
    public int maxTryCount;

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

    private boolean saveRequest(String request) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            //todo переделать маппинг из очереди 1 в сервиc фоток и Request
            /**
             * "_comment": "Это запрос от АРМ в back-end с парой фоток на сравнение",
             "token":"0LfQsNGH0LXQvCDQstGLINC/0L7RgtGA0LDRgtC40LvQuCDQstGA0LXQvNGPINC90LAg0LTQtdC60L7QtNC40YDQvtCy0LDQvdC40LU/",
             "wfmid": 13169,
             "iin": 11154,
             "username": "OperatorName",
             "timestamp": "timestamp",
             "type": "fullframe",
             "camPic": "data:image/jpeg;base64,/9j/4AAQSkZJRgABAQEASABIAA==",
             "scanPic": "data:image/jpeg;base64,/9j/4AAQSkZJRgABAQEASABIAAD/=="
             }
             */
            RequestDTO requestDTO = objectMapper.readValue(request, RequestDTO.class);

            String scannedPicture = requestDTO.getScannedPicturePreviewURL(); //TODO ...
            String webCamPicture = requestDTO.getScannedPicturePreviewURL(); //
            String scannedGuid = DatatypeConverter.printBase64Binary(UUID.randomUUID().toString().getBytes());
            String webCamGuid = DatatypeConverter.printBase64Binary(UUID.randomUUID().toString().getBytes());

            String scannedPictureURL = photoServiceClient.putPhoto(requestDTO.getTimestamp().toString(), scannedPicture, scannedGuid);
            String webCamPictureURL = photoServiceClient.putPhoto(requestDTO.getTimestamp().toString(), webCamPicture, webCamGuid);
            Document photo;
            Document scan;
            //TODO Find request to add
            Request requestEntity = requestService.findByOrderNumber(requestDTO.getWfNumber());
            if(requestEntity != null) { //TODO discuss что куда и когда и в каком формате доки
                // TODO соответствие между дто и ентити
                photo = requestEntity.getCameraDocument();
                scan = requestEntity.getScannedDocument();
            } else {
                requestEntity = new Request();
                photo = new Document();
                scan = new Document();
            }
            if(requestDTO.getType() == RequestDTO.Type.PREVIEW) {
                photo.setFaceSquare(requestDTO.getWebCamPicturePreviewURL());
//                photo.setDocumentType(DocumentType.PHOTO);

                scan.setFaceSquare(requestDTO.getScannedPicturePreviewURL());
//                scan.setDocumentType(DocumentType.SCAN);
            } else {
                photo.setOrigImageURL(requestDTO.getWebCamPicturePreviewURL());
//                photo.setDocumentType(DocumentType.PHOTO);

                scan.setOrigImageURL(requestDTO.getScannedPicturePreviewURL());
//                scan.setDocumentType(DocumentType.SCAN);
            }

            photo.setOrigImageURL(webCamPictureURL);
            scan.setOrigImageURL(scannedPictureURL);

            requestEntity.setCameraDocument(photo);
            requestEntity.setScannedDocument(scan);
            requestEntity.setWfmID("" + requestDTO.getWfNumber());
            requestEntity.setObjectDate(new Date());
            //Todo save request
            requestService.saveOrUpdate(requestEntity);
            return true;
        } catch (IOException e) {
            log.error(e);
            return false;
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
