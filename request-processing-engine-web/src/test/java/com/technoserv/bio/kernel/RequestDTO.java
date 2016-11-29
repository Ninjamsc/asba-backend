package com.technoserv.bio.kernel;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;

import java.sql.Timestamp;

/**
 * Created by sergey on 22.11.2016.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class RequestDTO {

    public enum Type{PREVIEW, FULLFRAME;
        @JsonValue
        String getValue() {
            return this.toString().toLowerCase();
        }
    };

    @JsonProperty("_comment")
    private String comment;
    @JsonProperty("wfmId")
    private long wfNumber;
    @JsonProperty("iin")
    private long iin;
    private String username;
    private Type type;
    private Timestamp timestamp;
    @JsonProperty("camPic")
    private String webCameraPicture;
    @JsonProperty("scanPic")
    private String scannedPicture;

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

    public Timestamp getTimestamp() {
        return timestamp;
    }
    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public String getWebCameraPicture() {
        return webCameraPicture;
    }
    public void setWebCameraPicture(String webCameraPicture) {
        this.webCameraPicture = webCameraPicture;
    }

    public String getScannedPicture() {
        return scannedPicture;
    }
    public void setScannedPicture(String scannedPicture) {
        this.scannedPicture = scannedPicture;
    }

    public Type getType() {
        return type;
    }
    public void setType(Type type) {
        this.type = type;
    }
}