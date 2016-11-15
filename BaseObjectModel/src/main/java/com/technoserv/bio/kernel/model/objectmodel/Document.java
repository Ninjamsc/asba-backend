package com.technoserv.bio.kernel.model.objectmodel;
public class Document extends AbstractObject {
	
	DocumentType	documentType;
	String			origImageURL; //TODO String http URL
	String			faceSquare; //TODO String HTTP URL
	Convolution[]	allConvolutions; // если есть несколько шаблонов нескольких версий сети
}
