package com.technoserv.bio.kernel.dao.configuration.rest;

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
@PropertySource("classpath:TemplateBuilderServiceRestClient.properties")
public class PhotoAnalizerServiceRestClient {

    private static final Log log = LogFactory.getLog(PhotoAnalizerServiceRestClient.class);

    @Value("${http.rest.client.url}")
    private String url;

    private RestTemplate rest = new RestTemplate();

    public PhotoTemplate getPhotoTemplate(String base64photo) {
        if(log.isInfoEnabled()) {
            log.info("REQUESTING TEMPLATE: '" + base64photo + "'");
        }
        try {
//            rest.put(URI.create(url), base64photo);
            Base64Photo request = new Base64Photo(base64photo);
            ResponseEntity<PhotoTemplate> response = rest.exchange(URI.create(url), HttpMethod.POST, new HttpEntity<Base64Photo>(request), PhotoTemplate.class);
            if(log.isInfoEnabled()) {
                log.info("SENDING MESSAGE: '" + base64photo + "' DONE");
            }
            return response.getBody();
        } catch (HttpClientErrorException e) {
            switch (e.getStatusCode()){
                /*Стандартные названия ошибок не совпадают с нашей документацией
                 * только коды */
                //todo унаследовать от HttpStatus и добавить/заменить наши
                //todo 512	outOfMemory на GPU, такого номера просто нет, скорее всего exception будет другой
                case NOT_EXTENDED://
                    log.error("510 base64 не является фотографией");
                case NETWORK_AUTHENTICATION_REQUIRED:
                    log.error("511 не удалось рассчитать биометрический шаблон. Внутренняя ошибка (в CUDA)");
                case INTERNAL_SERVER_ERROR:
                    default: log.error("Неизвестная ошибка");
            }

        }
        return null;
    }
}
