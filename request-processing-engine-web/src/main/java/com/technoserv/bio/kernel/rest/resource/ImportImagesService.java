package com.technoserv.bio.kernel.rest.resource;

import java.io.InputStream;
import java.util.Map;

/**
 * Created by Adrey on 19.12.2016.
 */
public interface ImportImagesService {

    void importImage(Long stopListId, InputStream uploadedInputStream, String fileName);
    Map<String, Map<String, Boolean>> importImageZip(Long stopListId, InputStream uploadedInputStream, String fileName);

}