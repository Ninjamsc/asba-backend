package com.usetech.bridge.bean;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

/**
 * Created by User on 14.11.2016.
 */
public class LogStoreBean implements Serializable {

    private static final long serialVersionUID = -5054749880960511861L;


    @JsonProperty(value = "username")
    private String username;
    @JsonProperty(value = "workstation")
    private String workstation;
    @JsonProperty(value = "wfmId")
    private Long wfmId;
    @JsonProperty(value = "fileContent")
    private String fileContent;
    @JsonProperty(value = "file-name")
    private String fileName;
    @JsonProperty(value = "timestamp")
    private String timestamp;

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getFileContent() {
        return fileContent;
    }

    public void setFileContent(String fileContent) {
        this.fileContent = fileContent;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Long getWfmId() {
        return wfmId;
    }

    public void setWfmId(Long wfmId) {
        this.wfmId = wfmId;
    }

    public String getWorkstation() {
        return workstation;
    }

    public void setWorkstation(String workstation) {
        this.workstation = workstation;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
