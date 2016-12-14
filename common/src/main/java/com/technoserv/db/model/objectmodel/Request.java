package com.technoserv.db.model.objectmodel;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.technoserv.db.model.BaseEntity;

import javax.persistence.*;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Date;

/**
 * REQUESTS	Хранилище заявок на биометрическую идентификацию, полученных от АРМ.
 * Заявка связывает сканированное изображение (Документ),
 * Изображение с Веб Камеры с номером заявки (wfmId)
 **/
@Entity
@Table(name = "REQUESTS")
public class Request extends BaseEntity<Long> {
    public enum Status {SAVED, IN_PROCESS, SUCCESS, FAILED};

    /**
     * номер заявки из BPM
     */
    @Id
    @Column(name = "WFM_ID", unique = true)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(columnDefinition = "PERSON_ID",referencedColumnName = "ID")
    private Person person;
    /**
     * дата создания объекта
     */
    @Temporal(value = TemporalType.DATE)
    @Column(name = "INS_DATE", nullable = false, updatable = false)
    private Date objectDate = new Date();

    @Column(name = "STATUS", nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status = Status.SAVED;
    /**
     * cканированное изображение
     */
    @JoinColumn(name = "SCAN_DOC_ID", referencedColumnName = "ID")
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Document scannedDocument;
    /**
     * изображение с веб камеры
     */
    @JoinColumn(name = "WEB_DOC_ID", referencedColumnName = "ID")
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Document cameraDocument;

    @Column(name = "LOGIN_USER", nullable = false)
    private String login;

    @Column(name = "INS_USER")
    private String insUser;

    //from request
    @Column(name = "TIMESTAMP") //TODO absent in db model
    @Temporal(TemporalType.TIMESTAMP)
    private Date orderDate;

    public Long getId() {
        return id;
    } //TODO ...
    public void setId(Long id) {
        this.id = id;
    }

    public Document getScannedDocument() {
        return scannedDocument;
    }
    public void setScannedDocument(Document scannedDocument) {
        this.scannedDocument = scannedDocument;
    }

    public Document getCameraDocument() {
        return cameraDocument;
    }
    public void setCameraDocument(Document cameraDocument) {
        this.cameraDocument = cameraDocument;
    }

    public Status getStatus() {
        return status;
    }
    public void setStatus(Status status) {
        this.status = status;
    }

    public String getLogin() {
        return login;
    }
    public void setLogin(String login) {
        this.login = login;
    }

    public String getInsUser() {
        return insUser;
    }
    public void setInsUser(String insUser) {
        this.insUser = insUser;
    }

    public Date getObjectDate() {
        return objectDate;
    }
    public void setObjectDate(Date objectDate) {
        this.objectDate = objectDate;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public Timestamp getTimestamp() {
        return orderDate != null ? new Timestamp(orderDate.getTime()) : new Timestamp(0);
    }

    public void setTimestamp(Timestamp timestamp) {
        this.orderDate = new Date(timestamp.getTime());
    }

}