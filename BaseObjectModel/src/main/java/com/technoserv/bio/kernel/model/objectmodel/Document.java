package com.technoserv.bio.kernel.model.objectmodel;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;

@Entity
@Table(name = "DOCUMENTS")
public class Document extends AbstractObject {

    @Column(name = "DOCUMENT_TYPE")
    private int documentType;
    @Column(name = "ORIG_IMAGE_URL")
    private String origImageURL; //TODO String http URL
    @Column(name = "FACE_SQUARE")
    private String faceSquare; //TODO String HTTP URL
    /**
     * если есть несколько шаблонов нескольких версий сети
     */
    @Column
    @OneToMany
    private List<Convolution> allConvolutions;

    public DocumentType getDocumentType() {
        return DocumentType.parse(documentType);
    }
    public void setDocumentType(DocumentType documentType) {
        this.documentType = documentType.getCode();
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