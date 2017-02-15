package com.technoserv.jms.trusted;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;

import java.sql.Timestamp;
import java.util.ArrayList;

/**
 * Created by mlevitin on 14.02.2016.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class RequestDTO {


    @JsonProperty("videoSource")
    private String videoSource;

    @JsonProperty("faceId")
    private String faceId;

    @JsonProperty("timestamp")
    private Timestamp timestamp;

    public ArrayList<Snapshot> getSnapshots() {
        return snapshots;
    }

    public void setSnapshots(ArrayList<Snapshot> snapshots) {
        this.snapshots = snapshots;
    }

    @JsonProperty("Photos")
    private ArrayList<Snapshot> snapshots;


    public String getvideSouce() {
        return videoSource;
    }
    public void setVideoSource(String comment) {
        this.videoSource = comment;
    }

    public String getFaceId() {
        return faceId;
    }
    public void setFaceId(String faceId ) {
        this.faceId = faceId;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }
    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

}