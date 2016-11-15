package com.technoserv.bio.kernel.util;

import com.technoserv.bio.kernel.model.configuration.FrontEndConfiguration;
import com.technoserv.bio.kernel.model.configuration.FrontEnds;
import com.technoserv.bio.kernel.model.objectmodel.AbstractObject;
import com.technoserv.bio.kernel.model.objectmodel.Document;
import com.technoserv.bio.kernel.model.objectmodel.DocumentType;

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
        result.setFaceSquare("faceSquare");
        result.setOrigImageURL("origImageURL");
        return result;
    }

    private static void setObjectDate(AbstractObject object) {
        object.setObjectDate(new Date());
    }
}
