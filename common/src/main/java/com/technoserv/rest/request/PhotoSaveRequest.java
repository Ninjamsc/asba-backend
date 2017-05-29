package com.technoserv.rest.request;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;

/**
 * Created by VBasakov on 23.11.2016.
 */
public class PhotoSaveRequest {

    @JsonProperty("file-content")
    public String file_content;

    @JsonProperty("file-name")
    public String file_name;

    public PhotoSaveRequest(String file_content, String file_name) {
        this.file_content = file_content;
        this.file_name = file_name;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("file_content", file_content)
                .add("file_name", file_name)
                .toString();
    }
}
