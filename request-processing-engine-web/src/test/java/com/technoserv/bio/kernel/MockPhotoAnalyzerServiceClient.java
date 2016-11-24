package com.technoserv.bio.kernel;

import com.technoserv.bio.kernel.rest.client.PhotoAnalyzerServiceRestClient;
import com.technoserv.rest.client.PhotoPersistServiceRestClient;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

/**
 * Created by Adrey on 24.11.2016.
 */
@Service
@Profile("test")
public class MockPhotoAnalyzerServiceClient extends PhotoAnalyzerServiceRestClient {

    @Override
    public void analizePhoto(String base64photo) {

    }
}
