package com.technoserv.jms.consumer;

import com.technoserv.jms.trusted.RequestDTO;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by Andrey on 23.11.2016.
 */
public class ArmRequestJmsConsumerTest {

    @Test
    public void parse() throws Exception {
        String request = TestUtils.readFile("request.json");
        RequestDTO requestDto = JsonUtils.deserializeJson(request, RequestDTO.class);
        Assert.assertEquals(13169, requestDto.getWfNumber());
        Assert.assertEquals(11154, requestDto.getIin());
        Assert.assertEquals("OperatorName", requestDto.getUsername());
        Assert.assertEquals(RequestDTO.Type.FULLFRAME, requestDto.getType());
        Assert.assertNotNull(requestDto.getScannedPicture());
        Assert.assertNotNull(requestDto.getWebCameraPicture());
    }
}
