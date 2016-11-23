package com.technoserv.bio.kernel.rest;


import com.technoserv.bio.kernel.rest.response.PhotoTemplate;
import com.technoserv.rest.request.Base64Photo;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
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
//@PropertySource("classpath:templateBuilderServiceRestClient.properties")
public class TemplateBuilderServiceRestClient {

    private static final Log log = LogFactory.getLog(TemplateBuilderServiceRestClient.class);

//    @Value("${http.template.builder.service.client.url}")
    //todo fix it
    private String url;

    private RestTemplate rest = new RestTemplate();

    public PhotoTemplate getPhotoTemplate(Base64Photo request) {
        if (log.isInfoEnabled()) {
            log.info("REQUESTING TEMPLATE: '" + request.photos + "'");
        }
        try {
            //todo request -> json with jackson
            ResponseEntity<PhotoTemplate> response = rest.exchange(URI.create(url), HttpMethod.PUT, new HttpEntity<>(request), PhotoTemplate.class);
            if (log.isInfoEnabled()) {
                log.info("REQUESTING TEMPLATE: DONE");
            }
            return response.getBody();
        } catch (HttpClientErrorException e) {
            switch (e.getStatusCode()) {
                /*Стандартные названия ошибок не совпадают с нашей документацией
                 * только коды */
                //todo унаследовать от HttpStatus и добавить/заменить наши
                //todo 512	outOfMemory на GPU, такого номера просто нет, скорее всего exception будет другой
                case NOT_EXTENDED://
                    log.error("510 base64 не является фотографией");
                    break;
                case NETWORK_AUTHENTICATION_REQUIRED:
                    log.error("511 не удалось рассчитать биометрический шаблон. Внутренняя ошибка (в CUDA)");
                    break;
                case INTERNAL_SERVER_ERROR:
                default:
                    log.error("Неизвестная ошибка");
            }

        }
        return null;
    }
}
