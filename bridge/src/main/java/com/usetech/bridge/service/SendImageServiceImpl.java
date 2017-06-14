package com.usetech.bridge.service;

import com.usetech.bridge.bean.FrameBean;
import com.usetech.bridge.config.CommonConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.JmsException;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

@Service
public class SendImageServiceImpl implements SendImageService {

    private final static Logger log = LoggerFactory.getLogger(SendImageServiceImpl.class.getSimpleName());

    private CommonConfig config;

    private JmsTemplate jmsTemplate;

    @Autowired
    public SendImageServiceImpl(CommonConfig config, JmsTemplate jmsTemplate) {
        this.config = config;
        this.jmsTemplate = jmsTemplate;
    }

    @Override
    public boolean send(FrameBean frameBean) {
        log.info("sending '{}' to '{}'", frameBean, jmsTemplate.getDefaultDestinationName());
        try {
            frameBean.setCamPic(handlePicture(frameBean.getCamPic()));
            frameBean.setScanPic(handlePicture(frameBean.getScanPic()));
            this.jmsTemplate.convertAndSend(frameBean);
        } catch (JmsException e) {
            log.error("Failed to send message using jmsTemplate: {}, {}", jmsTemplate, e);
            return false;
        }
        return true;
    }

    private String handlePicture(String picture) { //TODO ...
        if (picture != null && "".equals(picture.trim())) {
            return null;
        }
        if (picture.contains("data:image")) {
            String base64Image = picture.split(",")[1];
//            byte[] imageBytes = javax.xml.bind.DatatypeConverter.parseBase64Binary(base64Image);
            //return new String(imageBytes);
            return base64Image;
        } else {
            return picture;
        }
    }

}