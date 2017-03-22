package com.technoserv.bio.kernel;

import com.technoserv.rest.client.PhotoAnalyzerServiceRestClient;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

/**
 * Created by Adrey on 24.11.2016.
 */
@Service
@Profile("test")
public class MockPhotoAnalyzerServiceClient extends PhotoAnalyzerServiceRestClient {

    @Override
    public void analyzePhoto(byte[] base64photo) {

    }
}
