package com.technoserv.rest.request;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Created by VBasakov on 23.11.2016.
 */
public class PhotoSaveRequest {

    public String timestamp;

    @JsonProperty("file-content")
    public String file_content;

    @JsonProperty("file-name")
    public String file_name;

    public PhotoSaveRequest(String timestamp, String file_content, String file_name) {
        this.timestamp = timestamp;
        this.file_content = file_content;
        this.file_name = file_name;
    }
}
