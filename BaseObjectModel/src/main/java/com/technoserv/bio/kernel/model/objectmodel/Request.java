package com.technoserv.bio.kernel.model.objectmodel;

import javax.persistence.*;

@Entity
@Table(name = "REQUESTS")
public class Request extends AbstractObject {
    /**
     * номер заявки из BPM
     */
    @Column(name = "BMP_REQUEST_NUMBER")
    private String bpmRequestNumber;
    /**
     * cканированное изображение
     */
    @JoinColumn
    @ManyToOne
    private Document scannedDocument;
    /**
     * изображение с веб камеры
     */
    @JoinColumn
    @ManyToOne
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
}