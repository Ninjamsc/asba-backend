package com.technoserv.jms.trusted;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by mlevitin on 15.02.17.
 */
public class Snapshot {

    public String getSnapshot() {
        return snapshot;
    }

    public void setSnapshot(String snapshot) {
        this.snapshot = snapshot;
    }

    @JsonProperty("shot")
    private String snapshot;
}
