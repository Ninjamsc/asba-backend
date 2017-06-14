package com.usetech.bridge.bean;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;
import org.assertj.core.util.Strings;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

public class AuthBean implements Serializable {

    private static final long serialVersionUID = -5054749880970511861L;

    @JsonProperty(value = "token")
    @NotNull
    private String token;

    @JsonProperty(value = "username")
    @NotNull
    private String username;

    public String getToken() {
        return this.token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("token", !Strings.isNullOrEmpty(token))
                .add("username", username)
                .toString();
    }
}