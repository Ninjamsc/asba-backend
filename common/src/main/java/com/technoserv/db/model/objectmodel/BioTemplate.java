package com.technoserv.db.model.objectmodel;

import com.google.common.base.MoreObjects;
import org.apache.commons.lang.StringUtils;

import javax.persistence.*;

/**
 * Хранилище биометрических шаблонов (векторов).
 * Каждый биометрический шаблон имеет версию и связан с конкретным документом,
 * по которому этот шаблон построен
 */
@Entity
@Table(name = "BIO_TEMPLATES")
public class BioTemplate extends AbstractObject {

    @ManyToOne
    @JoinColumn(name = "DOC_ID", referencedColumnName = "ID")
    private Document document;

    @ManyToOne
    @JoinColumn(name = "BTMPLT_ID", referencedColumnName = "BTMPLT_ID")
    private BioTemplateType bioTemplateType;

    @ManyToOne
    @JoinColumn(name = "BTMPLV_ID", referencedColumnName = "BTMPLV_ID")
    private BioTemplateVersion bioTemplateVersion;

    @Column(name = "INS_USER")
    private String insUser;

    @Lob
    @Column(name = "TEMPLATE_VECTOR")
    private String templateVector;

    public Document getDocument() {
        return document;
    }

    public void setDocument(Document document) {
        this.document = document;
    }

    public BioTemplateType getBioTemplateType() {
        return bioTemplateType;
    }

    public void setBioTemplateType(BioTemplateType bioTemplateType) {
        this.bioTemplateType = bioTemplateType;
    }

    public BioTemplateVersion getBioTemplateVersion() {
        return bioTemplateVersion;
    }

    public void setBioTemplateVersion(BioTemplateVersion bioTemplateVersion) {
        this.bioTemplateVersion = bioTemplateVersion;
    }

    public String getInsUser() {
        return insUser;
    }

    public void setInsUser(String insUser) {
        this.insUser = insUser;
    }

    public String getTemplateVector() {
        return templateVector;
    }

    public void setTemplateVector(String templateVector) {
        this.templateVector = templateVector;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("document", document)
                .add("bioTemplateType", bioTemplateType)
                .add("bioTemplateVersion", bioTemplateVersion)
                .add("insUser", insUser)
                .add("templateVector", StringUtils.length(templateVector))
                .toString();
    }
}
