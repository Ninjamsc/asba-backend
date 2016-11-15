package com.technoserv.bio.kernel.model.objectmodel;

import javax.persistence.*;

/**
 * объект необходим для передачи в Oracle BPM комплекта документов (скан и фото) из АСБА
 */
@Entity
@Table(name="BMP_REPORTS")
public class BPMReport extends AbstractObject {

    @JoinColumn
    @ManyToOne
    private Document scan; //TODO main
    @JoinColumn
    @ManyToOne
    private Document photo;

    public Document getScan() {
        return scan;
    }
    public void setScan(Document scan) {
        this.scan = scan;
    }

    public Document getPhoto() {
        return photo;
    }
    public void setPhoto(Document photo) {
        this.photo = photo;
    }
}