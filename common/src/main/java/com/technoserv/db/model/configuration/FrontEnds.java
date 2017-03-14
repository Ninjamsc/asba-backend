package com.technoserv.db.model.configuration;


import com.technoserv.db.model.objectmodel.AbstractObject;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "FRONT_ENDS")
public class FrontEnds extends AbstractObject {

    @Column(name = "UUID", nullable = false)
    private String uuid;

    @Column(name = "WORKSTATION_NAME")
    private String workstationName;

    @Column(name = "OS_VERSION")
    private String osVersion;

    @Column(name = "USERNAME")
    private String username;

    @Column(name = "CLIENT_VERSION")
    private String clientVersion;


    // getters and setters

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getClientVersion() {
        return clientVersion;
    }

    public void setClientVersion(String clientVersion) {
        this.clientVersion = clientVersion;
    }
}