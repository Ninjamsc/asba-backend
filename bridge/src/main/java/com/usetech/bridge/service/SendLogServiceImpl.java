/*** Eclipse Class Decompiler plugin, copyright (c) 2016 Chen Chao (cnfree2000@hotmail.com) ***/
package com.usetech.bridge.service;

import com.usetech.bridge.bean.LogBean;
import com.usetech.bridge.config.CommonConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.JmsException;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

@Service
public class SendLogServiceImpl implements SendLogService {
    private final Logger log;
    @Autowired
    private CommonConfig config;
    @Autowired
    private JmsTemplate jmsTemplate;

    public SendLogServiceImpl() {
        this.log = LoggerFactory.getLogger(this.getClass());
    }

    @Override
    public boolean send(LogBean logBean) {
        this.log.info("sending '{}' to '{}'", (Object) this.jmsTemplate.getDefaultDestinationName(),
                (Object) logBean);
        try {
            logBean.setFileContent(handleLog(logBean.getFileContent()));
            this.jmsTemplate.convertAndSend((Object) logBean);
        } catch (JmsException e) {
            this.log.error("Failed to send message using jmsTemplate: {}, {}", (Object) this.jmsTemplate, (Object) e);
            return false;
        }
        return true;
    }

    private String handleLog(String file) { //TODO ...
        if (file != null && "".equals(file.trim())) {
            return null;
        }
        if (file.contains("data:image")) {
            String base64Image = file.split(",")[1];
//            byte[] imageBytes = javax.xml.bind.DatatypeConverter.parseBase64Binary(base64Image);
            //return new String(imageBytes);
            return base64Image;
        } else {
            return file;
        }
    }

}