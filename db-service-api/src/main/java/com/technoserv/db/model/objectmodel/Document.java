package com.technoserv.db.model.objectmodel;


import javax.persistence.*;
import java.util.List;

/**
 * Документ
 */
@Entity
@Table(name = "DOCUMENTS")
public class Document extends AbstractObject {

    @Column(name = "DOCUMENT_TYPE")
    @Enumerated(EnumType.STRING)
    private DocumentType documentType;

    @Column(name = "ORIG_IMAGE_URL")
    private String origImageURL; //TODO String http URL

    @Column(name = "FACE_SQUARE")
    private String faceSquare; //TODO String HTTP URL
    /**
     * если есть несколько шаблонов нескольких версий сети
     */
    @OneToMany
    private List<Convolution> allConvolutions;

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

    public List<Convolution> getAllConvolutions() {
        return allConvolutions;
    }

    public void setAllConvolutions(List<Convolution> allConvolutions) {
        this.allConvolutions = allConvolutions;
    }
}