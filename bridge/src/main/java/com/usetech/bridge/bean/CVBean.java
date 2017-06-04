package com.usetech.bridge.bean;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;

//@XmlRootElement
public class CVBean {

    public class PreviewProperties {
        @JsonProperty
        private String heigth;
        @JsonProperty
        private String width;
    }

    public void setPicHeight(String heigth) {
        this.previewProperties.heigth = heigth;
    }

    public void setPicWidth(String width) {
        this.previewProperties.width = width;
    }

    @JsonProperty
    private PreviewProperties previewProperties;
    @JsonProperty
    private String yaw_threshold;
    @JsonProperty
    private String roll_threshold;
    @JsonProperty
    private String pitch_threshold;
    @JsonProperty
    private String blur_detection_thres;
    @JsonProperty
    private String brithness_thres_low;
    @JsonProperty
    private String brithness_thres_high;
    @JsonProperty
    private String lips_threshhold;
    @JsonProperty
    private String blur_threshhold;
    @JsonProperty
    private String video_record_max_duration;

    @JsonProperty
    private String yaw_mean;
    @JsonProperty
    private String roll_mean;
    @JsonProperty
    private String pitch_mean;


    public CVBean() {
        this.previewProperties = new PreviewProperties();
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

    public String getBlur_detection_thres() {
        return blur_detection_thres;
    }

    public void setBlur_detection_thres(String blur_detection_thres) {
        this.blur_detection_thres = blur_detection_thres;
    }

    public String getPitch_threshold() {
        return pitch_threshold;
    }

    public void setPitch_threshold(String pitch_threshold) {
        this.pitch_threshold = pitch_threshold;
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

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("previewProperties", previewProperties)
                .add("yaw_threshold", yaw_threshold)
                .add("roll_threshold", roll_threshold)
                .add("pitch_threshold", pitch_threshold)
                .add("blur_detection_thres", blur_detection_thres)
                .add("brithness_thres_low", brithness_thres_low)
                .add("brithness_thres_high", brithness_thres_high)
                .add("lips_threshhold", lips_threshhold)
                .add("blur_threshhold", blur_threshhold)
                .add("video_record_max_duration", video_record_max_duration)
                .add("yaw_mean", yaw_mean)
                .add("roll_mean", roll_mean)
                .add("pitch_mean", pitch_mean)
                .toString();
    }
}