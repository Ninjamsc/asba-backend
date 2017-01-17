/*** Eclipse Class Decompiler plugin, copyright (c) 2016 Chen Chao (cnfree2000@hotmail.com) ***/
package com.usetech.bridge.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

@Configuration
@PropertySource(value = { "classpath:application.properties" })
public class CommonConfig {
	@Value(value = "${com.technoserv.client.queueName}")
	private String queueName;
	@Value(value = "${com.technoserv.client.brokerUrl}")
	private String brokerUrl;

	@Value(value = "${com.technoserv.video.yaw_threshold}")
	private String yaw_threshold;
	@Value(value = "${com.technoserv.video.roll_threshold}")
	private String roll_threshold;
	@Value(value = "${com.technoserv.video.pitch_threshold}")
	private String pitch_threshold;
	@Value(value = "${com.technoserv.video.blur_detection_thres}")
	private String blur_detection_thres;
	@Value(value = "${com.technoserv.video.brithness_thres_low}")
	private String brithness_thres_low;
	@Value(value = "${com.technoserv.video.brithness_thres_high}")
	private String brithness_thres_high;
	@Value(value = "${com.technoserv.video.lips_threshhold}")
	private String lips_threshhold;
	@Value(value = "${com.technoserv.video.blur_threshhold}")
	private String blur_threshhold;
	@Value(value = "${com.technoserv.video.video_record_max_duration}")
	private String video_record_max_duration;
	@Value(value = "${com.technoserv.video.yaw_mean}")
	private String yaw_mean;
	@Value(value = "${com.technoserv.video.roll_mean}")
	private String roll_mean;
	@Value(value = "${com.technoserv.video.pitch_mean}")
	private String pitch_mean;
	
	@Value(value = "${com.technoserv.pic.heigth}")
	private String pic_height;
	@Value(value = "${com.technoserv.pic.width}")
	private String pic_width;

	
	@Bean
	public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
		return new PropertySourcesPlaceholderConfigurer();
	}

	
	public String getQueueName() {
		return this.queueName;
	}

	public String getBrokerUrl() {
		return this.brokerUrl;
	}


	public String getYaw_threshold() {
		return yaw_threshold;
	}


	public void setYaw_threshold(String yaw_threshold) {
		this.yaw_threshold = yaw_threshold;
	}


	public String getRoll_threshold() {
		return roll_threshold;
	}


	public void setRoll_threshold(String roll_threshold) {
		this.roll_threshold = roll_threshold;
	}


	public String getPitch_threshold() {
		return pitch_threshold;
	}


	public void setPitch_threshold(String pitch_threshold) {
		this.pitch_threshold = pitch_threshold;
	}


	public String getBlur_detection_thres() {
		return blur_detection_thres;
	}


	public void setBlur_detection_thres(String blur_detection_thres) {
		this.blur_detection_thres = blur_detection_thres;
	}


	public String getBrithness_thres_low() {
		return brithness_thres_low;
	}


	public void setBrithness_thres_low(String brithness_thres_low) {
		this.brithness_thres_low = brithness_thres_low;
	}


	public String getBrithness_thres_high() {
		return brithness_thres_high;
	}


	public void setBrithness_thres_high(String brithness_thres_high) {
		this.brithness_thres_high = brithness_thres_high;
	}


	public String getLips_threshhold() {
		return lips_threshhold;
	}


	public void setLips_threshhold(String lips_threshhold) {
		this.lips_threshhold = lips_threshhold;
	}


	public String getBlur_threshhold() {
		return blur_threshhold;
	}


	public void setBlur_threshhold(String blur_threshhold) {
		this.blur_threshhold = blur_threshhold;
	}


	public String getVideo_record_max_duration() {
		return video_record_max_duration;
	}


	public void setVideo_record_max_duration(String video_record_max_duration) {
		this.video_record_max_duration = video_record_max_duration;
	}


	public String getYaw_mean() {
		return yaw_mean;
	}


	public void setYaw_mean(String yaw_mean) {
		this.yaw_mean = yaw_mean;
	}


	public String getRoll_mean() {
		return roll_mean;
	}


	public void setRoll_mean(String roll_mean) {
		this.roll_mean = roll_mean;
	}


	public String getPitch_mean() {
		return pitch_mean;
	}


	public void setPitch_mean(String pitch_mean) {
		this.pitch_mean = pitch_mean;
	}


	public String getPic_height() {
		return pic_height;
	}


	public void setPic_height(String pic_height) {
		this.pic_height = pic_height;
	}


	public String getPic_width() {
		return pic_width;
	}


	public void setPic_width(String pic_width) {
		this.pic_width = pic_width;
	}
}