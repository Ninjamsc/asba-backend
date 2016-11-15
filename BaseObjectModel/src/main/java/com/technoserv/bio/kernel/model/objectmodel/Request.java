package com.technoserv.bio.kernel.model.objectmodel;

public class Request extends AbstractObject {
	String			bpmRequestNumber; // номер заявки из BPM
	Document		scannedDocument; // cсканированное изображение
	Document		cameraDocument;  // изображение с веб камеры
}
