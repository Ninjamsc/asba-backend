package com.technoserv.jms.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Strings;
import com.technoserv.db.model.objectmodel.SkudResult;
import com.technoserv.db.service.objectmodel.api.SkudResultService;
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
import org.springframework.security.crypto.codec.Base64;

import javax.xml.bind.DatatypeConverter;
import java.io.IOException;
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
    private PhotoPersistServiceRestClient photoServiceClient;

    @Autowired
    private SkudCompareServiceRestClient compareServiceClient;

    @Autowired
    private TemplateBuilderServiceRestClient templateBuilderServiceClient;

    @Autowired
    private SkudResultService skudResultService;

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss.S");

    @Value("${arm-retry.queue.maxRetryCount}")
    private static Integer maxTryCount = 10;

    public void onReceive(String message) throws Exception {
        if (!saveRequest(message)) {
            log.error("Can't process request.");
        }
    }

    protected boolean saveRequest(String request) {
        log.debug("saveRequest request: {}", request);
        List<ArrayRealVector> templates = new ArrayList<>();
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setDateFormat(DATE_FORMAT);
        try {
            //todo переделать маппинг из очереди 1 в сервиc фоток и Request
            RequestDTO requestDTO = objectMapper.readValue(request, RequestDTO.class);

            String videoSource = requestDTO.getVideoSource();
            String faceId = requestDTO.getFaceId();

            log.debug("Request received videoSource: {} face: {}", videoSource, faceId);
            List<Snapshot> snapshots = requestDTO.getSnapshots();


            if (snapshots == null || snapshots.isEmpty()) {
                log.warn("Photo list is empty.");
                return true;
            }

            for (Snapshot temp : snapshots) {

                String shot = handlePicture(temp.getSnapshot());
                String shotGuid = DatatypeConverter.printBase64Binary(UUID.randomUUID().toString().getBytes());
                String shotURL = photoServiceClient.putPhoto(shot, shotGuid);

                byte[] a = Base64.decode(shot.getBytes());
                PhotoTemplate photoTemplate = templateBuilderServiceClient.getPhotoTemplate(a);

                if (photoTemplate != null && photoTemplate.template != null) {
                    ArrayRealVector arv = new ArrayRealVector(photoTemplate.template);
                    templates.add(arv);

                    SkudResult t = new SkudResult();
                    t.setFaceId(new Long(requestDTO.getFaceId()));
                    t.setFaceSquare(shotURL);
                    t.setHeight(new Long(temp.getHeight()));
                    t.setWidth(new Long(temp.getWidth()));
                    t.setBlur(new Double(temp.getBlur()));
                    t.setVideoSrc(requestDTO.getSourceName());
                    t.setTimestamp(requestDTO.getTimestamp());
                    SkudCompareRequest r = new SkudCompareRequest();
                    r.setPictureURL(shotURL);
                    r.setTemplate(photoTemplate.template);

                    SkudCompareResponse response = null;
                    try {
                        response = compareServiceClient.compare(r);
                    } catch (Exception e) {
                        log.error("Can't compare photo: {}", shotURL);
                    }

                    if (response != null && response.getMatch() != null) {
                        t.setSimilarity(response.getMatch().getSimilarity());
                        t.setPerson(response.getMatch().getIdentity());
                        t.setUrl(response.getMatch().getUrl());
                        log.debug("Compare response: {}", response);
                    }
                    skudResultService.save(t);
                }
            }

            log.info("Number of templates built: {}", templates.size());
            for (ArrayRealVector t : templates) {
                log.debug(t.toString());
            }

        } catch (IOException e) {
            log.error("Can't process request.", e);
            return false;
        }
        return true;
    }

    private String handlePicture(String picture) {
        if (Strings.isNullOrEmpty(picture)) {
            log.warn("handlePicture: picture is empty");
            return null;
        }
        return picture;
    }

}
