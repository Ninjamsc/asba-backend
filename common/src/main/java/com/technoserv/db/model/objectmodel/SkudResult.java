package com.technoserv.db.model.objectmodel;

import com.technoserv.db.model.BaseEntity;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;

/**
 * Created by mlevitin on 14.12.2016.
 */
@Entity
@Table(name = "SKUD_RESULTS")
public class SkudResult extends AbstractObject {

    @Column(name = "FACE_ID") // from request
    private Long faceId;

    @Column(name = "VIDEO_SRC") // from request
    private String videoSrc;

    @Column(name = "FACE_SQUARE", length = 2048) // picture URL
    private String faceSquare; //TODO String HTTP URL

    @Column(name = "PERSON") // person ID
    private Long person;

    @Column(name = "URL") // URL to picture of similar person
    private String url;

    @Column(name = "HEIGHT")
    private Long height;

    @Column(name = "WIDTH")
    private Long width;

    @Column(name = "BLUR")
    private Double blur;

    @Column(name = "SIMILARITY") // degree of similarity
    private Double similarity;

    //from request
    @Column(name = "TIMESTAMP") //TODO absent in db model
    @Temporal(TemporalType.TIMESTAMP)
    private Date orderDate;


    public SkudResult() {}

    public String getFaceSquare() { return faceSquare; }
    public void setFaceSquare(String faceSquare) { this.faceSquare = faceSquare;}
    public Timestamp getTimestamp() {
        return orderDate != null ? new Timestamp(orderDate.getTime()) : new Timestamp(0);
    }
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
    public void setTimestamp(Timestamp timestamp) {
        this.orderDate = new Date(timestamp.getTime());
    }
    public Long getFaceId() { return faceId; }
    public void setFaceId(Long faceId) { this.faceId = faceId; }
    public Long getPerson() { return person; }
    public void setPerson(Long person) { this.person = person; }
    public Double getSimilarity() { return similarity; }
    public void setSimilarity(Double similarity) { this.similarity = similarity;}
    public String getVideoSrc() { return videoSrc; }
    public void setVideoSrc(String videoSrc) { this.videoSrc = videoSrc; }
    public Long getHeight() {return height;}
    public void setHeight(Long height) {this.height = height;}
    public Long getWidth() {return width;}
    public void setWidth(Long width) {this.width = width;}
    public Double getBlur() {return blur;}
    public void setBlur(Double blur) {this.blur = blur;}


}