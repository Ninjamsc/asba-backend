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
        result.setDocumentType(DocumentType.PHOTO);
        result.setFaceSquare("faceSquare_" + result.getObjectDate());
        result.setOrigImageURL("origImageURL_" + result.getObjectDate());
        return result;
    }

    public static BPMReport generateBPMReport(){
        BPMReport result = new BPMReport();
        setObjectDate(result);
        result.setPhoto(generateDocument());
        result.setScan(generateDocument());
        return result;
    }

    public static Contractor generateContractor(){
        Contractor result = new Contractor();
        setObjectDate(result);
        result.setContractorName("testContractorName");
        result.setSimilarityThreshold(12.45f);
        return result;
    }

    public static Convolution generateConvolution(){
        Convolution result = new Convolution();
        setObjectDate(result);
        result.setCnnVersion(1);
        result.setConvolution(new byte[]{1,2,4});
        return result;
    }

    public static Person generatePerson(){
        Person result = new Person();
        setObjectDate(result);
        result.setPersonNumber("1");
        return result;
    }

    public static Request generateRequest(){
        Request result = new Request();
        setObjectDate(result);
        result.setBpmRequestNumber("1");
//        result.setCameraDocument(generateDocument());
//        result.setScannedDocument(generateDocument());
        return result;
    }

    public static StopList generateStopList(){
        StopList result = new StopList();
        setObjectDate(result);
        result.setCommon(true);
        result.setStopListName("StopListName");
        return result;
    }

    private static void setObjectDate(AbstractObject object) {
        object.setObjectDate(new Date());
    }
}
