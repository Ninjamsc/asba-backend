package com.technoserv.jms.trusted;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;

import java.sql.Timestamp;
import java.util.List;

/**
 * Created by mlevitin on 14.02.2016.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class RequestDTO {

    @JsonProperty("videoSource")
    private String videoSource;

    @JsonProperty("sourceName")
    private String sourceName;

    @JsonProperty("faceId")
    private String faceId;

    @JsonProperty("timestamp")
    private Timestamp timestamp;

    @JsonProperty("Photos")
    private List<Snapshot> snapshots;

    public List<Snapshot> getSnapshots() {
        return snapshots;
    }

    public void setSnapshots(List<Snapshot> snapshots) {
        this.snapshots = snapshots;
    }

    public String getVideoSource() {
        return videoSource;
    }

    public void setVideoSource(String comment) {
        this.videoSource = comment;
    }

    public String getFaceId() {
        return faceId;
    }

    public void setFaceId(String faceId) {
        this.faceId = faceId;
    }

    public String getSourceName() {
        return sourceName;
    }

    public void setSourceName(String sourceName) {
        this.sourceName = sourceName;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("videoSource", videoSource)
                .add("sourceName", sourceName)
                .add("faceId", faceId)
                .add("timestamp", timestamp)
                .add("snapshots", snapshots)
                .toString();
    }
}