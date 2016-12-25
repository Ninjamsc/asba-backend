/*** Eclipse Class Decompiler plugin, copyright (c) 2016 Chen Chao (cnfree2000@hotmail.com) ***/
package com.technoserv.bridge.service;

import com.technoserv.bridge.bean.FrameBean;
import com.technoserv.bridge.config.CommonConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.JmsException;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

@Service
public class SendImageServiceImpl implements SendImageService {
	private final Logger log;
	@Autowired
	private CommonConfig config;
	@Autowired
	private JmsTemplate jmsTemplate;

	public SendImageServiceImpl() {
		this.log = LoggerFactory.getLogger(this.getClass());
	}

	@Override
	public boolean send(FrameBean frameBean) {
		this.log.info("sending '{}' to '{}'", (Object) this.jmsTemplate.getDefaultDestinationName(),
				(Object) frameBean);
		try {
			frameBean.setCamPic( handlePicture(frameBean.getCamPic()  ));
			  frameBean.setScanPic( handlePicture(frameBean.getScanPic() ));
			this.jmsTemplate.convertAndSend((Object) frameBean);
		} catch (JmsException e) {
			this.log.error("Failed to send message using jmsTemplate: {}, {}", (Object) this.jmsTemplate, (Object) e);
			return false;
		}
		return true;
	}
	
    private String handlePicture(String picture) { //TODO ...
        if(picture!=null && "".equals(picture.trim())) {
            return null;
        }
        if(picture.contains("data:image")) {
            String base64Image = picture.split(",")[1];
//            byte[] imageBytes = javax.xml.bind.DatatypeConverter.parseBase64Binary(base64Image);
            //return new String(imageBytes);
            return base64Image;
        } else {
        return picture;
        }
    }

}