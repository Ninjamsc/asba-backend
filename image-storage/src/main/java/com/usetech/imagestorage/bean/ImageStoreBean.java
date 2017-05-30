package com.usetech.imagestorage.bean;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

/**
 * Created by User on 14.11.2016.
 */
public class ImageStoreBean implements Serializable {

    private static final long serialVersionUID = -5054749880960511861L;

    @JsonProperty(value = "file-content")
    private String fileContent;

    @JsonProperty(value = "file-name")
    private String fileName;

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
}
