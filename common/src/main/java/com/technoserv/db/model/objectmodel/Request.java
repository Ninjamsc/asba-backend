package com.technoserv.db.model.objectmodel;

import javax.persistence.*;

/**
 * REQUESTS	Хранилище заявок на биометрическую идентификацию, полученных от АРМ.
 * Заявка связывает сканированное изображение (Документ),
 * Изображение с Веб Камеры с номером заявки (wfmId)
 **/
@Entity
@Table(name = "REQUESTS")
public class Request extends AbstractObject {

    public enum Status {SAVED, IN_PROCESS, SUCCESS, FAILED};
    /**
     * номер заявки из BPM
     */
    @Column(name = "WFM_ID", unique = true)
    private String wfmID;

    @Column(name = "STATUS", nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status;
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

    @Column(name = "INS_USER", nullable = false)
    private String insUser;

    public String getWfmID() {
        return wfmID;
    }

    public void setWfmID(String wfmID) {
        this.wfmID = wfmID;
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
}