package com.technoserv.bio.kernel.model.objectmodel;

import java.util.List;

public class Contractor extends AbstractObject {

    private String contractorName;
    private StopList[] contractorLists;
    private float similarityThresold; // мера схожести
    /**
     * загруженные банком фотографии (со свертками итп)
     */
    private List<Document> loadedImages; //
}
