package com.usetech.bridge.bean;

import com.google.common.base.MoreObjects;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

public class RegBean implements Serializable {

    private static final long serialVersionUID = -5054749880970511861L;

    @NotNull
    private String uuid;

    @NotNull
    private String username;

    @NotNull
    private String workstationName;

    private String osVersion;

    private String clientVersion;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getWorkstationName() {
        return workstationName;
    }

    public void setWorkstationName(String workstationName) {
        this.workstationName = workstationName;
    }

    public String getOsVersion() {
        return osVersion;
    }

    public void setOsVersion(String osVersion) {
        this.osVersion = osVersion;
    }

    public String getClientVersion() {
        return clientVersion;
    }

    public void setClientVersion(String clientVersion) {
        this.clientVersion = clientVersion;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("uuid", uuid)
                .add("username", username)
                .add("workstationName", workstationName)
                .add("osVersion", osVersion)
                .add("clientVersion", clientVersion)
                .toString();
    }
}