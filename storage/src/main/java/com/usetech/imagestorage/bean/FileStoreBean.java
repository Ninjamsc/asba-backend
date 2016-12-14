//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.usetech.imagestorage.bean;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;

public class FileStoreBean implements Serializable {
    private static final long serialVersionUID = -5054749880960511861L;
    @JsonProperty("file-content")
    private String fileContent;
    @JsonProperty("file-name")
    private String fileName;

    public FileStoreBean() {
    }

    public String getFileContent() {
        return this.fileContent;
    }

    public void setFileContent(String fileContent) {
        this.fileContent = fileContent;
    }

    public String getFileName() {
        return this.fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
