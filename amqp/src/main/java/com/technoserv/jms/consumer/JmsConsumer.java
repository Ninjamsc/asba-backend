package com.technoserv.jms.consumer;

import com.technoserv.jms.HttpRestClient;
import com.technoserv.jms.trusted.RetryMessage;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class JmsConsumer {

    private static final Log log = LogFactory.getLog(JmsConsumer.class);

    @Autowired
    private HttpRestClient httpRestClient;

    @Autowired
    private JmsTemplate jmsTemplate;

    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy_hh_mm_ss");

    public static final int MAX_TRY_COUNT = 10;

    public void onReceive(String message) {
        if(!httpRestClient.put(message)) {
            jmsTemplate.convertAndSend(new RetryMessage(message));
        }
    }

    public void onRetry(RetryMessage message) {
        if(message.getTryCount()<MAX_TRY_COUNT) {
            if (!httpRestClient.put(message.getMessage())) {
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

    private void writeToFile(RetryMessage message) throws IOException {
        File file = new File(simpleDateFormat.format(new Date()) + ".txt");
        log.info("create file " + file.getAbsolutePath());
        if (!file.exists()) {
            file.createNewFile();
        }
        FileWriter fileWriter = new FileWriter(file);
        fileWriter.write(message.getMessage());
        fileWriter.close();
    }
 }