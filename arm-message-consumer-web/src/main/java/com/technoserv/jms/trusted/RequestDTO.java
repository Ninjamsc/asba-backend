package com.technoserv.jms.trusted;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.net.ntp.TimeStamp;

/**
 * Created by sergey on 22.11.2016.
 */
public class RequestDTO {

    public enum Type{FULL_FRAME, PREVIEW};

    @JsonProperty("_comment")
    private String comment;
    private long wfNumber;
    @JsonProperty("IIN")
    private long iin;
    private String username;
    private Type type;
    private TimeStamp timestamp;
    private String webCamPicturePreviewURL; //TODO ...
    private String scannedPicturePreviewURL; //TODO ...

    public String getComment() {
        return comment;
    }
    public void setComment(String comment) {
        this.comment = comment;
    }

    public long getWfNumber() {
        return wfNumber;
    }
    public void setWfNumber(long wfNumber) {
        this.wfNumber = wfNumber;
    }

    public long getIin() {
        return iin;
    }
    public void setIin(long iin) {
        this.iin = iin;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }

    public TimeStamp getTimestamp() {
        return timestamp;
    }
    public void setTimestamp(TimeStamp timestamp) {
        this.timestamp = timestamp;
    }

    public String getWebCamPicturePreviewURL() {
        return webCamPicturePreviewURL;
    }
    public void setWebCamPicturePreviewURL(String webCamPicturePreviewURL) {
        this.webCamPicturePreviewURL = webCamPicturePreviewURL;
    }

    public String getScannedPicturePreviewURL() {
        return scannedPicturePreviewURL;
    }
    public void setScannedPicturePreviewURL(String scannedPicturePreviewURL) {
        this.scannedPicturePreviewURL = scannedPicturePreviewURL;
    }

    public Type getType() {
        return type;
    }
    public void setType(Type type) {
        this.type = type;
    }
}