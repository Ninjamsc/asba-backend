/*** Eclipse Class Decompiler plugin, copyright (c) 2016 Chen Chao (cnfree2000@hotmail.com) ***/
package com.usetech.bridge.bean;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.usetech.bridge.service.LocalDateTimeDeserializer;
import com.usetech.bridge.service.LocalDateTimeSerializer;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;

public class LogBean implements Serializable {
    private static final long serialVersionUID = -5054749880970511861L;
    @JsonProperty(value = "token")
    @NotNull
    private String token;

    @NotNull
    @JsonProperty(value = "wfmId")
    //private String wfmId;
    private Long wfmId;

    @JsonProperty(value = "iin")
    @NotNull
    @Digits(integer = 19, fraction = 0)
    private Long iin;

    @JsonProperty(value = "username")
    @NotNull
    private String username;

    @JsonProperty(value = "workstation")
    @NotNull
    private String workstation;

    @JsonProperty(value = "timestamp")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @NotNull
    private LocalDateTime timestamp;

    @JsonProperty(value = "type")
    @NotNull
    private String type;

    @JsonProperty(value = "fileName")
    @NotNull
    private String fileName;

    @JsonProperty(value = "fileContent")
    @NotNull
    private String fileContent;

    public String getWorkstation() {
        return workstation;
    }

    public void setWorkstation(String workstation) {
        this.workstation = workstation;
    }

    public String getToken() {
        return this.token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Long getWfmId() {
        return this.wfmId;
    }

    public void setWfmId(Long wfmId) {
        this.wfmId = wfmId;
    }

    public Long getIin() {
        return this.iin;
    }

    public void setIin(Long iin) {
        this.iin = iin;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public LocalDateTime getTimestamp() {
        return this.timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getFileName() {
        return this.fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileContent() {
        return this.fileContent;
    }

    public void setFileContent(String fileContent) {
        this.fileContent = fileContent;
    }

    public String toString() {
        return "FrameBean{token='" + this.token + '\'' + ", wfmId='" + this.wfmId + '\'' + ", iin=" + this.iin
                + ", username='" + this.username + '\'' + ", timestamp='" + this.timestamp + '\'' + ", type='"
                + this.type + '\'' + '}';
    }
}