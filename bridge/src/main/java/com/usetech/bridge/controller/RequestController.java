/*** Eclipse Class Decompiler plugin, copyright (c) 2016 Chen Chao (cnfree2000@hotmail.com) ***/
package com.usetech.bridge.controller;

import com.usetech.bridge.bean.*;
import com.usetech.bridge.config.CommonConfig;
import com.usetech.bridge.service.LogStoreService;
import com.usetech.bridge.service.SendImageService;
import com.usetech.bridge.service.SendLogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping(value = { "/rest" })
public class RequestController {
	private static final Logger log = LoggerFactory.getLogger(RequestController.class);

	@Autowired
	private LogStoreService logStoreService;
	@Autowired
	private SendImageService sendImageService;
	@Autowired
	private SendLogService sendLogService;
	@Autowired
	private CommonConfig config;

	@RequestMapping(value = { "/auth" }, method = { RequestMethod.PUT })
	public ResponseEntity auth(@Valid @RequestBody AuthBean frameBean) {
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
		return ResponseEntity.ok().body((Object) cvs);
	}
	
	@RequestMapping(value = { "/reg" }, method = { RequestMethod.PUT })
	public ResponseEntity auth(@Valid @RequestBody RegBean frameBean) {
		
		return ResponseEntity.ok().body((Object) null);
	}


	@RequestMapping(value = { "/log" }, method = { RequestMethod.GET })
	public ResponseEntity storeLog(@RequestBody LogStoreBean logStoreBean) {
		if (logStoreService.saveFile(logStoreBean)) {
			return ResponseEntity.status(HttpStatus.OK).body(null);
		}
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
	}


	@RequestMapping(value = { "/ping" }, method = { RequestMethod.GET })
	public ResponseEntity ping() {
		return ResponseEntity.ok().body((Object) null);
	}

	@RequestMapping(value = { "/preview" }, method = { RequestMethod.PUT })
	public ResponseEntity preview(@Valid @RequestBody FrameBean frameBean) {
		return this.send(frameBean, ImageType.PREVIEW);
	}

	@RequestMapping(value = { "/fullframe" }, method = { RequestMethod.PUT })
	public ResponseEntity fullframe(@Valid @RequestBody FrameBean frameBean) {
		return this.send(frameBean, ImageType.FULLFRAME);
	}

	@RequestMapping(value = { "/log" }, method = { RequestMethod.PUT })
	public ResponseEntity log(@Valid @RequestBody LogBean logBean) {
		return this.send(logBean);
	}
	private ResponseEntity send(LogBean logBean) {
		ResponseEntity responseEntity = this.validate(logBean);
		if (responseEntity.getStatusCode() == HttpStatus.OK && !this.sendLogService.send(logBean)) {
			return ResponseEntity.status((HttpStatus) HttpStatus.BAD_REQUEST)
					.body((Object) new ErrorBean("failed to deliver message"));
		}
		return responseEntity;
	}
	private ResponseEntity send(FrameBean frameBean, ImageType imageType) {
		ResponseEntity responseEntity = this.validate(frameBean, imageType);
		if (responseEntity.getStatusCode() == HttpStatus.OK && !this.sendImageService.send(frameBean)) {
			return ResponseEntity.status((HttpStatus) HttpStatus.BAD_REQUEST)
					.body((Object) new ErrorBean("failed to deliver message"));
		}
		return responseEntity;
	}

	private ResponseEntity validate(FrameBean frameBean, ImageType imageType) {
		ImageType image = ImageType.findByCode(frameBean.getType());
		if (image == null) {
			return ResponseEntity.status((HttpStatus) HttpStatus.BAD_REQUEST)
					.body((Object) new ErrorBean("Provided image type " + frameBean.getType() + " is invalid"));
		}
		if (image != imageType) {
			return ResponseEntity.status((HttpStatus) HttpStatus.BAD_REQUEST).body((Object) new ErrorBean(
					"Provided image type doesn't match expected " + (Object) ((Object) imageType)));
		}
		return ResponseEntity.status((HttpStatus) HttpStatus.OK).body((Object) null);
	}

	private ResponseEntity validate(LogBean logBean) {
		return ResponseEntity.status((HttpStatus) HttpStatus.OK).body((Object) null);
	}

}