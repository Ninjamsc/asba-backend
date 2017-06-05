package com.technoserv.jms.trusted;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by mlevitin on 15.02.17.
 */
public class Snapshot {

    @JsonProperty("shot")
    private String snapshot;

    @JsonProperty("height")
    private String height;

    @JsonProperty("width")
    private String width;

    @JsonProperty("blur")
    private String blur;

    public String getSnapshot() {
        return snapshot;
    }

    public void setSnapshot(String snapshot) {
        this.snapshot = snapshot;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getWidth() {
        return width;
    }

    public void setWidth(String width) {
        this.width = width;
    }

    public String getBlur() {
        return blur;
    }

    public void setBlur(String blur) {
        this.blur = blur;
    }

}
