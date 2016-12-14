package com.technoserv.bio.kernel.rest.client;

import com.technoserv.bio.kernel.rest.response.PhotoTemplate;
import com.technoserv.utils.JsonUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by VBasakov on 25.11.2016.
 */
//@RunWith(SpringJUnit4ClassRunner.class)
public class TemplateBuilderServiceRestClientTest {
    TemplateBuilderServiceRestClient client;
    {
        client = new TemplateBuilderServiceRestClient(){
            @Override
            public String getUrl() {
                return "http://www.mocky.io/v2/5838827111000031118fd37f";
            }
        };
    }

    @Test
    public void testPut(){
        PhotoTemplate template = client.getPhotoTemplate(new byte[]{21,124,46,7});
        System.out.printf("**************************************************");
        System.out.println(JsonUtils.serializeJson(template.template));
        System.out.printf("**************************************************");

    }
}
