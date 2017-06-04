package com.technoserv.jms.consumer;

import com.technoserv.db.model.objectmodel.Person;
import com.technoserv.db.model.objectmodel.Request;
import com.technoserv.db.service.objectmodel.api.DocumentService;
import com.technoserv.db.service.objectmodel.api.PersonService;
import com.technoserv.db.service.objectmodel.api.RequestService;
import com.technoserv.jms.trusted.RequestDTO;
import com.technoserv.utils.JsonUtils;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by Andrey on 23.11.2016.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/applicationContext.xml")
@Transactional
@ActiveProfiles({"test"})
@Ignore
public class ArmRequestJmsConsumerTest {

//    @Autowired
//    private PersonService personService;
//    @Autowired
//    private DocumentService documentService;
//    @Autowired
//    private ArmRequestJmsConsumer consumer;
//    @Autowired
//    private RequestService requestService;
//
//    @Test
//    public void parse() throws Exception {
//        String request = TestUtils.readFile("request-fullframe-1.json");
//        RequestDTO requestDto = JsonUtils.deserializeJson(request, RequestDTO.class);
//        Assert.assertEquals(13169, requestDto.getWfNumber());
//        Assert.assertEquals(11154, requestDto.getIin());
//        Assert.assertEquals("OperatorName", requestDto.getUsername());
//        Assert.assertEquals(RequestDTO.Type.FULLFRAME, requestDto.getType());
//        Assert.assertNotNull(requestDto.getScannedPicture());
//        Assert.assertNotNull(requestDto.getWebCameraPicture());
//    }
//
//    @Test
//    public void save() throws Exception {
//        Long wfmId = 13169L;
//        Long iin = 11154L;
//
//        saveIncoming("request-fullframe-1.json");
//        Request requestEntity = requestService.findByOrderNumber(wfmId);
//        Assert.assertEquals(wfmId, requestEntity.getId());
//        Person personEntity = personService.findById(iin);
//        Assert.assertEquals(iin, personEntity.getId());
//        Assert.assertNotNull(requestEntity.getCameraDocument().getFaceSquare());
//        Assert.assertNotNull(requestEntity.getScannedDocument().getFaceSquare());
//
//        saveIncoming("request-preview-1.json");
//        Request requestEntity2 = requestService.findByOrderNumber(wfmId);
//        Assert.assertEquals(wfmId, requestEntity2.getId());
//        Person personEntity2 = personService.findById(iin);
//        Assert.assertEquals(iin, personEntity2.getId());
//        Assert.assertNotNull(requestEntity.getCameraDocument().getOrigImageURL());
//        Assert.assertNotNull(requestEntity.getScannedDocument().getOrigImageURL());
//    }

//    private void saveIncoming(String fileName) throws Exception {
//        String request = TestUtils.readFile(fileName);
//        consumer.saveRequest(request);
//    }
}