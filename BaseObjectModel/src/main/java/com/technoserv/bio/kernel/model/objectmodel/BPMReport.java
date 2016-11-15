package com.technoserv.bio.kernel.model.objectmodel;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * объект необходим для передачи в Oracle BPM комплекта документов (скан и фото) из АСБА
 */
@Entity
@Table(name="BMP_REPORTS")
public class BPMReport extends AbstractObject {

    @Column
    @ManyToOne
    private Document scan; //TODO main
    @Column
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