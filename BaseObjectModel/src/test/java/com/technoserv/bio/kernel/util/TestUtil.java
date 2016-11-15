package com.technoserv.bio.kernel.util;

import com.technoserv.bio.kernel.model.configuration.FrontEndConfiguration;
import com.technoserv.bio.kernel.model.configuration.FrontEnds;
import com.technoserv.bio.kernel.model.objectmodel.Document;
import com.technoserv.bio.kernel.model.objectmodel.DocumentType;

/**
 * Created by VBasakov on 16.11.2016.
 */
public class TestUtil {

    public static FrontEnds generateFrontEnds(){
        FrontEnds result = new FrontEnds();
        result.setVersion(1);
        return result;
    }

    public static FrontEndConfiguration generateFrontEndConfiguration(){
        FrontEndConfiguration result = new FrontEndConfiguration();
        result.setVersion(1);
        return result;
    }

    public static Document generateDocument(){
        Document result = new Document();
        result.setDocumentType(DocumentType.PHOTO);
        result.setFaceSquare("faceSquare");
        result.setOrigImageURL("origImageURL");
        return result;
    }
}
