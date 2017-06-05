package com.technoserv.db.model.security;

import java.io.Serializable;

public enum UserProfileType implements Serializable {

    READ("READ"),
    WRITE("WRITE"),
    ADMIN("ADMIN");

    String userProfileType;

    UserProfileType(String userProfileType) {
        this.userProfileType = userProfileType;
    }

    public String getUserProfileType() {
        return userProfileType;
    }

}