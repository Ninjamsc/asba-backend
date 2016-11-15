package com.technoserv.bio.kernel.model.objectmodel;


public class Document extends AbstractObject {
	
	DocumentType	documentType;
	String			origImageURL; //TODO String http URL
	String			faceSquare; //TODO String HTTP URL
	Convolution[]	allConvolutions; // если есть несколько шаблонов нескольких версий сети

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

	public Convolution[] getAllConvolutions() {
		return allConvolutions;
	}

	public void setAllConvolutions(Convolution[] allConvolutions) {
		this.allConvolutions = allConvolutions;
	}
}
