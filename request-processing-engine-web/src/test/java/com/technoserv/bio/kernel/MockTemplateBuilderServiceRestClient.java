package com.technoserv.bio.kernel;

import com.technoserv.rest.client.TemplateBuilderServiceRestClient;
import com.technoserv.rest.request.PhotoTemplate;
import org.jvnet.hk2.annotations.Service;
import org.springframework.context.annotation.Profile;

/**
 * Created by Adrey on 25.11.2016.
 */
@Service
@Profile("test")
public class MockTemplateBuilderServiceRestClient extends TemplateBuilderServiceRestClient {

    @Override
    public PhotoTemplate getPhotoTemplate(byte[] request) {
        PhotoTemplate photoTemplate = new PhotoTemplate();
        photoTemplate.version = 1;
        photoTemplate.template = new double[]{1.1, 1.2};
        return photoTemplate;
    }
}