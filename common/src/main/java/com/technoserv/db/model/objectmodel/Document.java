package com.technoserv.db.model.objectmodel;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.base.MoreObjects;
import org.apache.commons.lang.StringUtils;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DOCUMENTS	Хранилище документов. Каждый документ - это связка между фотографией полного кадра, превью лица с нее
 */
@Entity
@Table(name = "DOCUMENTS")
public class Document extends AbstractObject {

    @Column(name = "DESCRIPTION", nullable = false)
    private String description = "UNKNOWN";

    @ManyToOne
    @JoinColumn(name = "DOC_TYPE_ID", referencedColumnName = "ID")
    private DocumentType documentType;

    @Column(name = "ORIG_IMAGE_URL", length = 2048)
    private String origImageURL; //TODO String http URL

    @Column(name = "FACE_SQUARE", length = 2048)
    private String faceSquare; //TODO String HTTP URL

    @OneToMany(mappedBy = "document", cascade = {CascadeType.ALL, CascadeType.REMOVE})
    @JsonIgnore
    private List<BioTemplate> bioTemplates = new ArrayList<BioTemplate>();

    public DocumentType getDocumentType() {
        return documentType;
    }

    public void setDocumentType(DocumentType documentType) {
        this.documentType = documentType;
    }

    public String getOrigImageURL() {
        return origImageURL;
    }

    public void setOrigImageURL(String origImageURL) {
        this.origImageURL = origImageURL;
    }

    public String getFaceSquare() {
        return faceSquare;
    }

    public void setFaceSquare(String faceSquare) {
        this.faceSquare = faceSquare;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<BioTemplate> getBioTemplates() {
        return bioTemplates;
    }

    public void setBioTemplates(List<BioTemplate> bioTemplates) {
        this.bioTemplates = bioTemplates;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("description", description)
                .add("documentType", documentType)
                .add("origImageURL", origImageURL)
                .add("faceSquare", StringUtils.length(faceSquare))
                .add("bioTemplates count", bioTemplates == null ? "null" : bioTemplates.size())
                .toString();
    }
}
