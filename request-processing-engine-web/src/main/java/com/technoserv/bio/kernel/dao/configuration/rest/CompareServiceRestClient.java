package com.technoserv.bio.kernel.dao.configuration.rest;


import com.technoserv.bio.kernel.dao.configuration.rest.request.CompareServiceRequest;
import com.technoserv.bio.kernel.dao.configuration.rest.response.PhotoTemplate;
import com.technoserv.rest.request.Base64Photo;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

/**
 * Created by VBasakov on 22.11.2016.
 */
@Service
//@PropertySource("classpath:compareServiceRestClient.properties")
public class CompareServiceRestClient {

    private static final Log log = LogFactory.getLog(CompareServiceRestClient.class);

//    @Value("${http.compare.service.client.url}")
    //TODO fix it
    private String url;
    private RestTemplate rest = new RestTemplate();

    public String compare(CompareServiceRequest request) {
        if(log.isInfoEnabled()) {
            log.info("REQUESTING TEMPLATE: '" + request + "'");
        }
        try {
            //todo request -> json with jackson
            ResponseEntity<String> response = rest.exchange(URI.create(url), HttpMethod.PUT, new HttpEntity<>(request), String.class);
            if(log.isInfoEnabled()) {
                log.info("REQUESTING TEMPLATE: DONE");
            }
            return response.getBody();
        } catch (HttpClientErrorException e) {
            switch (e.getStatusCode()){
                /*Стандартные названия ошибок не совпадают с нашей документацией
                 * только коды */
                //todo унаследовать от HttpStatus и добавить/заменить наши
                //todo 512	outOfMemory на GPU, такого номера просто нет, скорее всего exception будет другой
                case BAD_REQUEST: log.error("Неполный/неверный запрос"); break;
                case INTERNAL_SERVER_ERROR:
                default: log.error("Неизвестная ошибка");
            }
        }
        return null;
    }
}