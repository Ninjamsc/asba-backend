package com.technoserv.db.model.objectmodel;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Хранилище биометрических шаблонов (векторов).
 * Каждый биометрический шаблон имеет версию и связан с конкретным документом,
 * по которому этот шаблон построен
 */
@Entity
@Table(name="BIO_TEMPLATES")
public class BioTemplate extends AbstractObject {

    @ManyToOne
    @JoinColumn(name = "DOC_ID", referencedColumnName = "ID")
    private Document document;

    public Document getDocument() {
        return document;
    }

    public void setDocument(Document document) {
        this.document = document;
    }
}
