package com.technoserv.bio.kernel.rest.resource;

import java.io.InputStream;
import java.util.Map;

/**
 * Created by Adrey on 19.12.2016.
 */
public interface ImportImagesService {

    Map<String, Map<String, Boolean>> importImages(InputStream uploadedInputStream, String fileName);

}