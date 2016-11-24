package com.technoserv.db.model.objectmodel;


import javax.persistence.*;
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

    @Column(name = "ORIG_IMAGE_URL")
    private String origImageURL; //TODO String http URL

    @Column(name = "FACE_SQUARE")
    private String faceSquare; //TODO String HTTP URL

    @OneToMany(mappedBy = "document")
    private List<BioTemplate> bioTemplates;

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
}
