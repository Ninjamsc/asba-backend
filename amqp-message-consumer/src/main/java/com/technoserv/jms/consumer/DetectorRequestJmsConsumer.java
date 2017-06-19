package com.technoserv.jms.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Strings;
import com.technoserv.config.ConfigValues;
import com.technoserv.db.model.objectmodel.SkudResult;
import com.technoserv.db.service.objectmodel.api.SkudResultService;
import com.technoserv.dto.Notification;
import com.technoserv.jms.trusted.RequestDTO;
import com.technoserv.jms.trusted.Snapshot;
import com.technoserv.rest.client.PhotoPersistServiceRestClient;
import com.technoserv.rest.client.SkudCompareServiceRestClient;
import com.technoserv.rest.client.TemplateBuilderServiceRestClient;
import com.technoserv.rest.model.SkudCompareRequest;
import com.technoserv.rest.model.SkudCompareResponse;
import com.technoserv.rest.request.PhotoTemplate;
import com.technoserv.service.NotificationService;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
public class DetectorRequestJmsConsumer {

    private static final Logger log = LoggerFactory.getLogger(DetectorRequestJmsConsumer.class);

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss.S");

    private PhotoPersistServiceRestClient photoServiceClient;

    private SkudCompareServiceRestClient compareServiceClient;

    private TemplateBuilderServiceRestClient templateBuilderServiceClient;

    private SkudResultService skudResultService;

    private NotificationService notificationService;


    @Autowired
    public DetectorRequestJmsConsumer(PhotoPersistServiceRestClient photoServiceClient,
                                      SkudCompareServiceRestClient compareServiceClient,
                                      TemplateBuilderServiceRestClient templateBuilderServiceClient,
                                      SkudResultService skudResultService,
                                      NotificationService notificationService) {

        this.photoServiceClient = photoServiceClient;
        this.compareServiceClient = compareServiceClient;
        this.templateBuilderServiceClient = templateBuilderServiceClient;
        this.skudResultService = skudResultService;
        this.notificationService = notificationService;
    }

    @Value(ConfigValues.ARM_QUEUE_MAX_NUMBER_OF_RETRIES)
    private static Integer maxTryCount = 10;

    public void onReceive(String message) throws Exception {
        log.debug("onReceive message: {}", message);
        if (!saveRequest(message)) {
            log.error("Can't process request.");
        }
    }

    boolean saveRequest(String request) {
        log.debug("saveRequest request: {}", request);
        List<ArrayRealVector> templates = new ArrayList<>();
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setDateFormat(DATE_FORMAT);
        try {
            //todo переделать маппинг из очереди 1 в сервис фоток и Request
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
                String shotUuid = DatatypeConverter.printBase64Binary(UUID.randomUUID().toString().getBytes());
                String shotURL = photoServiceClient.putPhoto(shot, shotUuid);

                byte[] a = Base64.decode(shot.getBytes());
                PhotoTemplate photoTemplate = templateBuilderServiceClient.getPhotoTemplate(a);

                if (photoTemplate != null && photoTemplate.template != null) {
                    ArrayRealVector arv = new ArrayRealVector(photoTemplate.template);
                    templates.add(arv);

                    SkudResult skudResult = new SkudResult();
                    skudResult.setFaceId(new Long(requestDTO.getFaceId()));
                    skudResult.setFaceSquare(shotURL);
                    skudResult.setHeight(new Long(temp.getHeight()));
                    skudResult.setWidth(new Long(temp.getWidth()));
                    skudResult.setBlur(new Double(temp.getBlur()));
                    skudResult.setVideoSrc(requestDTO.getSourceName());
                    skudResult.setTimestamp(requestDTO.getTimestamp());

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
                        skudResult.setSimilarity(response.getMatch().getSimilarity());
                        skudResult.setPerson(response.getMatch().getIdentity());
                        skudResult.setUrl(response.getMatch().getUrl());
                        log.debug("Compare response: {}", response);
                    }
                    skudResultService.save(skudResult);
                    notificationService.send(new Notification(skudResult.getId(), ""));
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
