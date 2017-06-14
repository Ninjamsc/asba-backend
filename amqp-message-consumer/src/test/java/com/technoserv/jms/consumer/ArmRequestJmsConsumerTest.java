package com.technoserv.jms.consumer;

import com.technoserv.db.service.objectmodel.api.DocumentService;
import com.technoserv.db.service.objectmodel.api.PersonService;
import com.technoserv.db.service.objectmodel.api.RequestService;
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

    @Autowired
    private PersonService personService;

    @Autowired
    private DocumentService documentService;

    @Autowired
    private ArmRequestJmsConsumer consumer;

    @Autowired
    private RequestService requestService;

    @Test
    public void parse() throws Exception {
//        String request = TestUtils.readFile("request-fullframe-1.json");
//        RequestDTO requestDto = JsonUtils.deserializeJson(request, RequestDTO.class);
//        assertEquals(13169, requestDto.getFaceId());
//        assertEquals(11154, requestDto.getIin());
//        assertEquals("OperatorName", requestDto.getUsername());
//        assertEquals(RequestDTO.Type.FULLFRAME, requestDto.getType());
//        assertNotNull(requestDto.getScannedPicture());
//        assertNotNull(requestDto.getWebCameraPicture());
    }

    @Test
    public void save() throws Exception {
//        Long wfmId = 13169L;
//        Long iin = 11154L;
//
//        saveIncoming("request-fullframe-1.json");
//        Request requestEntity = requestService.findByOrderNumber(wfmId);
//        assertEquals(wfmId, requestEntity.getId());
//        Person personEntity = personService.findById(iin);
//        assertEquals(iin, personEntity.getId());
//        assertNotNull(requestEntity.getCameraDocument().getFaceSquare());
//        assertNotNull(requestEntity.getScannedDocument().getFaceSquare());
//
//        saveIncoming("request-preview-1.json");
//        Request requestEntity2 = requestService.findByOrderNumber(wfmId);
//        assertEquals(wfmId, requestEntity2.getId());
//        Person personEntity2 = personService.findById(iin);
//        assertEquals(iin, personEntity2.getId());
//        assertNotNull(requestEntity.getCameraDocument().getOrigImageURL());
//        assertNotNull(requestEntity.getScannedDocument().getOrigImageURL());
    }

    private void saveIncoming(String fileName) throws Exception {
        String request = TestUtils.readFile(fileName);
        consumer.saveRequest(request);
    }
}