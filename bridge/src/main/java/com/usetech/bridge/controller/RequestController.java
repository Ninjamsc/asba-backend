package com.usetech.bridge.controller;

import com.technoserv.db.model.configuration.FrontEnd;
import com.usetech.bridge.bean.*;
import com.usetech.bridge.config.CommonConfig;
import com.usetech.bridge.service.LogStoreService;
import com.usetech.bridge.service.SendImageService;
import com.usetech.bridge.service.SendLogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.usetech.bridge.config.ConfigValues.FRONT_END_REGISTRATION_URL;

@RestController
@RequestMapping("/rest")
public class RequestController {

    private static final Logger log = LoggerFactory.getLogger(RequestController.class);

    private LogStoreService logStoreService;

    private SendImageService sendImageService;

    private SendLogService sendLogService;

    private CommonConfig config;

    private String frontEndRegistrationUrl;

    private static Map<Long,String> enquedRequests = new ConcurrentHashMap<>();
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");

    @Autowired
    public RequestController(LogStoreService logStoreService, SendImageService sendImageService,
                             SendLogService sendLogService, CommonConfig config,
                             @Value(FRONT_END_REGISTRATION_URL) String frontEndRegistrationUrl) {
        this.logStoreService = logStoreService;
        this.sendImageService = sendImageService;
        this.sendLogService = sendLogService;
        this.config = config;
        this.frontEndRegistrationUrl = frontEndRegistrationUrl;
    }

    @PutMapping("/auth")
    public ResponseEntity auth(@Valid @RequestBody AuthBean authBean) {
        log.info("auth authBean: {}", authBean);

        CVBean cvs = new CVBean();

        cvs.setBlur_detection_thres(config.getBlur_detection_thres());
        cvs.setBlur_threshhold(config.getBlur_threshhold());
        cvs.setBrithness_thres_high(config.getBrithness_thres_high());
        cvs.setBrithness_thres_low(config.getBrithness_thres_low());
        cvs.setLips_threshhold(config.getLips_threshhold());
        cvs.setPitch_threshold(config.getPitch_threshold());
        cvs.setRoll_threshold(config.getRoll_threshold());
        cvs.setYaw_threshold(config.getYaw_threshold());
        cvs.setVideo_record_max_duration(config.getVideo_record_max_duration());
        cvs.setYaw_mean(config.getYaw_mean());
        cvs.setRoll_mean(config.getRoll_mean());
        cvs.setPitch_mean(config.getPitch_mean());
        cvs.setPicHeight(config.getPic_height());
        cvs.setPicWidth(config.getPic_width());

        return ResponseEntity.ok().body(cvs);
    }

    @PostMapping("/reg")
    public ResponseEntity reg(@Valid @RequestBody RegBean regBean) {
        log.info("reg regBean: {}", regBean);
        RestTemplate restTemplate = new RestTemplate();

        FrontEnd frontEnd = new FrontEnd(
                regBean.getUuid(),
                regBean.getWorkstationName(),
                regBean.getOsVersion(),
                regBean.getUsername(),
                regBean.getClientVersion());

        return restTemplate.postForEntity(frontEndRegistrationUrl, frontEnd, FrontEnd.class);
    }

    @GetMapping("/log")
    public ResponseEntity storeLog(@RequestBody LogStoreBean logStoreBean) {
        log.debug("storeLog logStoreBean: {}", logStoreBean);
        if (logStoreService.saveFile(logStoreBean)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().build();
    }

    @GetMapping("/ping")
    public ResponseEntity ping() {
        log.trace("ping");
        return ResponseEntity.ok().build();
    }

    @GetMapping("/isenqueued/{wfmId}")
    public ResponseEntity ping(@PathVariable Long wfmId) {
        log.info("/isenqueued/"+wfmId);
        log.info("enquedRequests = "+enquedRequests.size());
        String date = enquedRequests.get(wfmId);
        Map<String,Object> report = new HashMap<>();
        List<Map<String,Object>> rules = new ArrayList<>();
        Map<String,Object> rule = new HashMap<>();
        if (date != null) {
            report.put("wfNumber", wfmId);
            rule.put("ruleId","4.2.6");
            rule.put("ruleName","Заявка "+wfmId+" находится в обработке");
            rule.put("photo",null);
        } else {
            report.put("wfNumber", null);
            rule.put("ruleId","4.2.7");
            rule.put("ruleName","Заявка "+wfmId+" в обработку не поступала");
            rule.put("photo",null);
        }
        report.put("timestamp",date);
        report.put("created-at",date);
        report.put("picSimilarity",null);
        report.put("picSimilarityThreshold",null);
        report.put("username",null);
        report.put("iin",null);

        report.put("scannedPicturePreviewURL",null);
        report.put("webCamPicturePreviewURL",null);
        report.put("scannedPictureURL",null);
        report.put("webCamPictureURL",null);
        report.put("IIN",null);

        report.put("scannedPicture",new HashMap<>());
        report.put("cameraPicture",new HashMap<>());
        report.put("othernessPictures",new HashMap<>());
        report.put("similarPictures",new HashMap<>());

        rules.add(rule);
        report.put("rules",rules);
        return ResponseEntity.ok(report);
    }

    //TODO: Удаление PUT
    @PutMapping("/dequeue/{wfmId}")
    public ResponseEntity dequeueWfmId(@PathVariable Long wfmId) {
        log.debug("dequeue wfmId: {}", wfmId);
        //TODO: Сохраять wfmId в спец контейнере
        enquedRequests.remove(wfmId);
        return ResponseEntity.ok().build();
    }


    @PutMapping("/preview")
    public ResponseEntity preview(@Valid @RequestBody FrameBean frameBean, HttpServletRequest request) {
        log.debug("preview frameBean: {} remoteIp: {}", frameBean, request.getRemoteAddr());
        //TODO: Сохраять wfmId в спец контейнере
        enquedRequests.put(frameBean.getWfmId(),frameBean.getTimestamp().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss")));
        log.debug("/preview frameBean.getWfmId()={}", frameBean.getWfmId());
        return send(frameBean, ImageType.PREVIEW);
    }

    @PutMapping("/fullframe")
    public ResponseEntity fullframe(@Valid @RequestBody FrameBean frameBean, HttpServletRequest request) {
        log.debug("fullframe frameBean: {} remoteIp: {}", frameBean, request.getRemoteAddr());
        //TODO: Сохраять wfmId в спец контейнере
        enquedRequests.put(frameBean.getWfmId(),frameBean.getTimestamp().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss")));
        log.debug("/fullframe frameBean.getWfmId()={}", frameBean.getWfmId());
        return send(frameBean, ImageType.FULLFRAME);
    }

    @PutMapping("/log")
    public ResponseEntity log(@Valid @RequestBody LogBean logBean, HttpServletRequest request) {
        log.debug("log logBean: {} remoteIp: {}", logBean, request.getRemoteAddr());
        return send(logBean);
    }

    private ResponseEntity send(LogBean logBean) {
        ResponseEntity responseEntity = validate(logBean);

        if (responseEntity.getStatusCode() == HttpStatus.OK && !sendLogService.send(logBean)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorBean("Failed to deliver log message."));
        }
        return responseEntity;
    }

    private ResponseEntity send(FrameBean frameBean, ImageType imageType) {
        ResponseEntity responseEntity = validate(frameBean, imageType);

        if (responseEntity.getStatusCode() == HttpStatus.OK && !sendImageService.send(frameBean)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorBean("Failed to deliver image."));
        }
        return responseEntity;
    }

    private ResponseEntity validate(FrameBean frameBean, ImageType imageType) {
        ImageType image = ImageType.findByCode(frameBean.getType());
        if (image == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorBean(String.format("Provided image type %s is invalid", frameBean.getType())));
        }
        if (image != imageType) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorBean("Provided image type doesn't match expected: " + imageType));
        }
        return ResponseEntity.ok().build();
    }

    private ResponseEntity validate(LogBean logBean) {
        return ResponseEntity.ok().build();
    }

}