package com.technoserv.jms.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.technoserv.db.model.objectmodel.SkudResult;
import com.technoserv.db.service.objectmodel.api.*;
import com.technoserv.jms.trusted.RequestDTO;
import com.technoserv.jms.trusted.Snapshot;
import com.technoserv.rest.client.PhotoPersistServiceRestClient;
import com.technoserv.rest.client.SkudCompareServiceRestClient;
import com.technoserv.rest.client.TemplateBuilderServiceRestClient;
import com.technoserv.rest.model.SkudCompareRequest;
import com.technoserv.rest.model.SkudCompareResponse;
import com.technoserv.rest.request.PhotoTemplate;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.security.crypto.codec.Base64;

import javax.xml.bind.DatatypeConverter;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by sergey on 22.11.2016.
 */
@PropertySource("classpath:arm-consumer.properties")
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

    public void onReceive(String message) throws Exception {
        if (!saveRequest(message)) {
            //jmsTemplate.convertAndSend(new ArmRequestRetryMessage(message));
            log.error("Unable to process request");
        }
    }

    protected boolean saveRequest(String request) {
        List<ArrayRealVector> templates = new ArrayList<>();
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setDateFormat(DATE_FORMAT);
        try {
            log.debug("++++++++" + request);

            //todo переделать маппинг из очереди 1 в сервиc фоток и Request
            RequestDTO requestDTO = objectMapper.readValue(request, RequestDTO.class);

            String videoSource = requestDTO.getVideoSource();
            String faceId = requestDTO.getFaceId();
            Timestamp timestamp = requestDTO.getTimestamp();

            log.debug("Request received videosource=" + videoSource + " face=" + faceId);
            List<Snapshot> al = requestDTO.getSnapshots();
            if (al == null) return true; //empty photo list
            if (al.size() == 0) return true; // empty photo list
            Timestamp tstmp = new Timestamp(System.currentTimeMillis());
            for (Snapshot temp : al) {
                //System.out.println(temp.getSnapshot());
                String shot = handlePicture(temp.getSnapshot());
                String shotGuid = DatatypeConverter.printBase64Binary(UUID.randomUUID().toString().getBytes());
                String shotURL = photoServiceClient.putPhoto(shot, shotGuid);
                byte[] a = Base64.decode(shot.getBytes());
                //byte[] b = Base64.encode(a);
                PhotoTemplate tmplt = templateBuilderServiceClient.getPhotoTemplate(a);
                if (tmplt != null && tmplt.template != null) {
                    ArrayRealVector arv = new ArrayRealVector(tmplt.template);
                    templates.add(arv);
                    SkudResult t = new SkudResult();
                    t.setFaceId(new Long(requestDTO.getFaceId()));
                    t.setFaceSquare(shotURL);
//                  t.setTimestamp(tstmp);
                    t.setHeight(new Long(temp.getHeight()));
                    t.setWidth(new Long(temp.getWidth()));
                    t.setBlur(new Double(temp.getBlur()));
                    t.setVideoSrc(requestDTO.getSourceName());
                    t.setTimestamp(requestDTO.getTimestamp());
                    SkudCompareRequest r = new SkudCompareRequest();
                    r.setPictureURL(shotURL);
                    r.setTemplate(tmplt.template);
                    SkudCompareResponse response;
                    try {
                        response = compareServiceClient.compare(r);
                    } catch (Exception e) {
                        e.printStackTrace();
                        log.error("Photo:" + shotURL);
                        response = null;
                    }
                    if (response != null && response.getMatch() != null) {
                        t.setSimilarity(response.getMatch().getSimilarity());
                        t.setPerson(response.getMatch().getIdentity());
                        t.setUrl(response.getMatch().getUrl());
                        log.debug("=========" + response);
                    }
                    skudResultService.save(t);
                }
            }

            log.info("Templates built:" + templates.size());
            for (ArrayRealVector t : templates) {
                System.out.println(t.toString());
            }
            log.info("+++++++++++++++");
        } catch (IOException e) {
            e.printStackTrace();
            log.error("Can't save request", e);
            return false;
        }
        return true;
    }

    private String handlePicture(String picture) {
        //TODO ...
        if (picture != null && "".equals(picture.trim())) {
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

}
