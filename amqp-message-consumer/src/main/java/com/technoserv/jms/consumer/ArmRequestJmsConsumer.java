package com.technoserv.jms.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.technoserv.rest.client.SkudCompareServiceRestClient;
import com.technoserv.db.model.objectmodel.*;
import com.technoserv.db.service.objectmodel.api.*;
import com.technoserv.jms.trusted.ArmRequestRetryMessage;
import com.technoserv.jms.trusted.RequestDTO;
import com.technoserv.jms.trusted.Snapshot;
import com.technoserv.rest.client.PhotoPersistServiceRestClient;
import com.technoserv.rest.client.TemplateBuilderServiceRestClient;
import com.technoserv.rest.model.SkudCompareRequest;
import com.technoserv.rest.model.SkudCompareResponse;
import com.technoserv.rest.request.PhotoTemplate;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.security.crypto.codec.Base64;

import javax.xml.bind.DatatypeConverter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;
import org.apache.commons.math3.linear.ArrayRealVector;
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
    private SkudCompareServiceRestClient compareServiceClient;
    @Autowired
    private TemplateBuilderServiceRestClient templateBuilderServiceClient;
    @Autowired
    private DocumentService documentService;
    @Autowired
    private DocumentTypeService documentTypeService;
    @Autowired
    private SkudResultService skudResultService;

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss.S");

    @Value("${arm-retry.queue.maxRetryCount}")
    private static Integer maxTryCount = 10;

    public void onReceive(String message) throws Exception{
        if(!saveRequest(message)) {
            //jmsTemplate.convertAndSend(new ArmRequestRetryMessage(message));
            System.out.println("Unable to process request");
        }
    }

    protected boolean saveRequest(String request)  {
        ArrayList<ArrayRealVector> templates = new ArrayList<ArrayRealVector>();
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setDateFormat(DATE_FORMAT);
        try {
            System.out.println("++++++++"+request);

            //todo переделать маппинг из очереди 1 в сервиc фоток и Request
            RequestDTO requestDTO = objectMapper.readValue(request, RequestDTO.class);

            String videoSource = requestDTO.getVideoSource();
            String faceId = requestDTO.getFaceId();
            Timestamp timestamp = requestDTO.getTimestamp();

            System.out.println("Request received videosource="+videoSource+" face="+faceId);
            ArrayList<Snapshot> al = requestDTO.getSnapshots();
            if(al != null ) {
                if (al.size() != 0) {
                    Timestamp tstmp = new Timestamp(System.currentTimeMillis());
                    for (Snapshot temp : al) {
                        //System.out.println(temp.getSnapshot());
                        String shot = handlePicture(temp.getSnapshot());
                        String shotGuid = DatatypeConverter.printBase64Binary(UUID.randomUUID().toString().getBytes());
                        String shotURL = photoServiceClient.putPhoto(shot, shotGuid);
                        byte[] a = Base64.decode(shot.getBytes());
                        //byte[] b = Base64.encode(a);
                        PhotoTemplate tmplt = templateBuilderServiceClient.getPhotoTemplate(a);
                        if(tmplt!=null && tmplt.template != null) {
                            ArrayRealVector arv = new ArrayRealVector(tmplt.template);
                            templates.add(arv);
                            SkudResult t = new SkudResult();
                            t.setFaceId(new Long(requestDTO.getFaceId()));
                            t.setFaceSquare(shotURL);
//                            t.setTimestamp(tstmp);
                            t.setHeight(new Long(temp.getHeight()));
                            t.setWidth(new Long(temp.getWidth()));
                            t.setBlur(new Double(temp.getBlur()));
                            t.setVideoSrc(requestDTO.getSourceName());
                            t.setTimestamp(requestDTO.getTimestamp());
                            SkudCompareRequest r = new SkudCompareRequest();
                            r.setPictureURL(shotURL);
                            r.setTemplate(tmplt.template);
                            SkudCompareResponse response = null;
                            try {
                                response = compareServiceClient.compare(r);
                            }
                            catch(Exception e)
                            {
                                e.printStackTrace();
                                response = null;
                            }
                            if (response != null) {
                                t.setSimilarity(response.getMatch().getSimilarity());
                                t.setPerson(response.getMatch().getIdentity());
                                t.setUrl(response.getMatch().getUrl());
                                System.out.println("=========" + response);
                            }

                            skudResultService.save(t);
                        }
                    }
                }
            }
         System.out.println("Templates built:"+templates.size());
            for ( ArrayRealVector t : templates) {
                System.out.println(t.toString());
            }
            }
        catch (IOException e) {
            e.printStackTrace();
            log.error(e);
            return false;
            }
        return true;
    }

  /*  protected boolean saveRequest(String request) {

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
            log.info("scannedPictureURL " + scannedPictureURL);
            log.info("webCamPictureURL " + webCamPictureURL);
            //TODO Find request to add
            Request requestEntity = requestService.findByOrderNumber(requestDTO.getWfNumber());

            if(requestEntity != null) { //TODO discuss что куда и когда и в каком формате доки
                // TODO соответствие между дто и ентити
                webCam = requestEntity.getCameraDocument();
                scan = requestEntity.getScannedDocument();

            } else {
                requestEntity = new Request();
                requestEntity.setId(requestDTO.getWfNumber());
                webCam = new Document();
                scan = new Document();
            }
            if(requestDTO.getType() == RequestDTO.Type.FULLFRAME) {
                if(webCamPictureURL!=null) {
                    webCam.setOrigImageURL(webCamPictureURL);
                }
                webCam.setDocumentType(documentTypeService.findByType(DocumentType.Type.WEB_CAM));
                if(scannedPictureURL!=null) {
                    scan.setOrigImageURL(scannedPictureURL);
                }
                scan.setDocumentType(documentTypeService.findByType(DocumentType.Type.SCANNER));
            } if (requestDTO.getType() == RequestDTO.Type.PREVIEW) {
                if(webCamPictureURL!=null) {
                    webCam.setFaceSquare(webCamPictureURL);
                }
                webCam.setDocumentType(documentTypeService.findByType(DocumentType.Type.WEB_CAM));
                if(scannedPictureURL!=null) {
                    scan.setFaceSquare(scannedPictureURL);
                }
                scan.setDocumentType(documentTypeService.findByType(DocumentType.Type.SCANNER));
            }

            documentService.saveOrUpdate(scan);
            documentService.saveOrUpdate(webCam);

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
            requestEntity.setCameraDocument(webCam);
            requestEntity.setScannedDocument(scan);
            requestEntity.setLogin(requestDTO.getUsername());
            requestEntity.setObjectDate(new Date());
            requestEntity.setStatus(Request.Status.SAVED);
            //Todo save request
            requestService.saveOrUpdate(requestEntity);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            log.error(e);
            return false;
        }
    }
*/
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
