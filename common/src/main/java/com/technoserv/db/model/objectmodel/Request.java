package com.technoserv.db.model.objectmodel;

import javax.persistence.*;

/**
 * Запрос от ARM в json-delivery-service-web
 */
@Entity
@Table(name = "REQUESTS")
public class Request extends AbstractObject {

    public enum Status {SAVED, IN_PROCESS, SUCCESS, FAILED};
    /**
     * номер заявки из BPM
     */
    @Column(name = "BMP_REQUEST_NUMBER", unique = true)
    private String bpmRequestNumber;
    @Column(name = "STATUS")
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
    @JoinColumn(name = "CAM_DOC_ID", referencedColumnName = "ID")
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Document cameraDocument;

    public String getBpmRequestNumber() {
        return bpmRequestNumber;
    }

    public void setBpmRequestNumber(String bpmRequestNumber) {
        this.bpmRequestNumber = bpmRequestNumber;
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
}