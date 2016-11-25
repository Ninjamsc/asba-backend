package com.technoserv.bio.kernel.rest.client;


import com.technoserv.bio.kernel.rest.exception.TemplateBuilderServiceException;
import com.technoserv.bio.kernel.rest.response.PhotoTemplate;
import com.technoserv.rest.request.Base64Photo;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

/**
 * Created by VBasakov on 22.11.2016.
 */
@Service
@PropertySource("classpath:templateBuilderServiceRestClient.properties")
public class TemplateBuilderServiceRestClient {

    private static final Log log = LogFactory.getLog(TemplateBuilderServiceRestClient.class);

    @Value("${http.template.builder.service.client.url}")
    private String url;

    private RestTemplate rest = new RestTemplate();

    public PhotoTemplate getPhotoTemplate(byte[] request) {
        if (log.isInfoEnabled()) {
            log.info("BUILDER BIO TEMPLATE: '" + request + "'");
        }
        try {
            //todo request -> json with jackson
            HttpHeaders requestHeaders = new HttpHeaders();
            requestHeaders.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<Base64Photo> requestEntity = new HttpEntity<Base64Photo>(new Base64Photo(request),requestHeaders);
            ResponseEntity<PhotoTemplate> response = rest.exchange(URI.create(url), HttpMethod.PUT, requestEntity, PhotoTemplate.class);
            if (log.isInfoEnabled()) {
                log.info("BUILDER BIO TEMPLATE: DONE");
            }
            return response.getBody();
        } catch (RestClientResponseException e) {
            log.info("BUILDER BIO TEMPLATE ERROR Code: " + e.getRawStatusCode());
            e.printStackTrace();
            switch (e.getRawStatusCode()) {
                /*Стандартные названия http-ошибок не совпадают с нашей документацией только коды */
                case 510:log.error("510 base64 не является фотографией");
                case 511:log.error("511 не удалось рассчитать биометрический шаблон. Внутренняя ошибка (в CUDA)");
//                case 500:log.error("500 Internal Server Error");
//                case 512:log.error("512 outOfMemory на GPU");
                default:
                    throw new TemplateBuilderServiceException(e.getResponseBodyAsString());
            }
        }
    }
}
