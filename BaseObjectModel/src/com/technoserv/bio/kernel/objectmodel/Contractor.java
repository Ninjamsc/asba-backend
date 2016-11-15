package com.technoserv.bio.kernel.objectmodel;

public class Contractor extends AbstractObject {

	String contractorName;
	StopList[] contractorLists;
	float similarityThresold; // мера схожести
	Document[] loadedImages; //загруженные банком фотографии (со свертками итп)
}
