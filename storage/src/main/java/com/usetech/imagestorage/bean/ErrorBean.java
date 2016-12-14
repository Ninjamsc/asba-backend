//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.usetech.imagestorage.bean;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;

public class ErrorBean implements Serializable {
    private static final long serialVersionUID = -5054349990970511861L;
    @JsonProperty("message")
    private String message;

    public ErrorBean(String message) {
        this.message = message;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
