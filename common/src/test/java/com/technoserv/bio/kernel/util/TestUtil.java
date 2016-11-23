package com.technoserv.bio.kernel.util;


import com.technoserv.db.model.configuration.FrontEndConfiguration;
import com.technoserv.db.model.configuration.FrontEnds;
import com.technoserv.db.model.objectmodel.*;

import java.util.Date;

/**
 * Created by VBasakov on 16.11.2016.
 */
public class TestUtil {

    public static FrontEnds generateFrontEnds(){
        FrontEnds result = new FrontEnds();
        setObjectDate(result);
        result.setVersion(1);
        return result;
    }

    public static FrontEndConfiguration generateFrontEndConfiguration(){
        FrontEndConfiguration result = new FrontEndConfiguration();
        setObjectDate(result);
        result.setVersion(1);
        return result;
    }

    public static Document generateDocument(){
        Document result = new Document();
        setObjectDate(result);
        result.setDescription("Description");
//        result.setDocumentType(DocumentType.PHOTO);
        result.setFaceSquare("faceSquare_" + result.getObjectDate());
        result.setOrigImageURL("origImageURL_" + result.getObjectDate());
        return result;
    }

    public static Person generatePerson(){
        Person result = new Person();
        setObjectDate(result);
        result.setIin("1");
        return result;
    }

    public static Request generateRequest(){
        Request result = new Request();
        setObjectDate(result);
        result.setWfmID(1L);
        result.setInsUser("1");
        result.setLogin("1");
        result.setStatus(Request.Status.SAVED);
//        result.setCameraDocument(generateDocument());
//        result.setScannedDocument(generateDocument());
        return result;
    }

    public static StopList generateStopList(){
        StopList result = new StopList();
        setObjectDate(result);
        result.setStopListName("StopListName");
        return result;
    }

    private static void setObjectDate(AbstractObject object) {
        object.setObjectDate(new Date());
    }
}
