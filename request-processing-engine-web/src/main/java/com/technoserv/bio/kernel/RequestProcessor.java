package com.technoserv.bio.kernel;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Date;

/**
 * Created by Adrey on 22.11.2016.
 */
public class RequestProcessor {

    private static final Log logger = LogFactory.getLog(RequestProcessor.class);

    public RequestProcessor() {
    }

    public void process() {
        logger.debug("RequestProcessor !!!!!!!" + new Date());
        System.out.println("RequestProcessor SSsadfsdfsdf" + new Date());
    }
}
