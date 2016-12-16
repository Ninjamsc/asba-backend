package com.technoserv.bio.kernel;

import com.technoserv.rest.client.TemplateBuilderServiceRestClient;
import com.technoserv.rest.request.PhotoTemplate;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by Adrey on 15.12.2016.
 */
public class TemplateBuilderServiceRestClientIT {

    private TemplateBuilderServiceRestClient templateBuilderServiceRestClient;

    @Before
    public void setUp() throws Exception {

        templateBuilderServiceRestClient = new TemplateBuilderServiceRestClient() {
            @Override
            public String getUrl() {
                return "http://sdorohov.ru/rpe/api/rest/template-builder-stub";
            }
        };
    }

    @Test
    public void put() throws Exception {
        PhotoTemplate photoTemplate = templateBuilderServiceRestClient.getPhotoTemplate("sfsdfsdf".getBytes());
    }
}