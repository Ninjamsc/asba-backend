package com.technoserv.jms.consumer;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.technoserv.db.model.objectmodel.Request;
import com.technoserv.db.service.objectmodel.api.RequestService;
import com.technoserv.jms.trusted.ArmRequestRetryMessage;
import com.technoserv.jms.trusted.RequestDTO;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jms.core.JmsTemplate;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by sergey on 22.11.2016.
 */
@PropertySource("classpath:arm-consumer.properties")
public class ArmRequestJmsConsumer {

    private static final Log log = LogFactory.getLog(ArmRequestJmsConsumer.class);

    @Autowired
    private JmsTemplate jmsTemplate;

    @Autowired
    private RequestService requestService;

    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy_hh_mm_ss_SSSSSS");

    @Value("${arm-retry.queue.maxRetryCount}")
    public int maxTryCount;

    public void onReceive(String message) {
        if(!saveRequest(message)) {
            jmsTemplate.convertAndSend(new ArmRequestRetryMessage(message));
        }
    }

    public void onReceive(ArmRequestRetryMessage message) {
        if(message.getTryCount()<=maxTryCount) {
            if (!saveRequest(message.getMessage())) {
                message.incTryCount();
                jmsTemplate.convertAndSend(message);
            }
        } else {
            try {
                writeToFile(message);
            } catch (IOException e) {
                log.error(e);
            }
        }
    }

    public boolean saveRequest(String request) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            RequestDTO requestDTO = objectMapper.readValue(request, RequestDTO.class);
            //TODO Find request to add
            //Request request = requestService.findByUid(requestDTO.getUid());
            //todo merge requestDto to request
            ///request.set...()
            //Todo save request
            // requestService.saveOrUpdate(request);
        } catch (IOException e) {
            log.error(e);
        }
        return false;
    }

    private void writeToFile(ArmRequestRetryMessage message) throws IOException {
        File file = new File("arm_req_" + simpleDateFormat.format(new Date()) + ".txt");
        log.info("create file " + file.getAbsolutePath());
        if (!file.exists()) {
            file.createNewFile();
        }
        FileWriter fileWriter = new FileWriter(file);
        fileWriter.write(message.getMessage());
        fileWriter.close();
    }

}
