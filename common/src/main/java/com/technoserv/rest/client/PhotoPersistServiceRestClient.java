package com.technoserv.rest.client;

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
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.net.URI;

/**
 * Created by VBasakov on 22.11.2016.
 */
@Service
@PropertySource("classpath:photoPersistServiceRestClient.properties")
public class PhotoPersistServiceRestClient {

    private static final Log log = LogFactory.getLog(PhotoPersistServiceRestClient.class);

    @Value("${http.photo.persist.service.hostname}")
    private String hostname;

    @Value("${http.photo.persist.service.port}")
    private String port;

    private String urlTemplate = String.format("HTTPS://%s:%s/storage/rest/image/%s.jpg", hostname, port, "%s");

    private RestTemplate rest = new RestTemplate();

    public Base64Photo getPhoto(String guid) {
        if(log.isInfoEnabled()) {
            log.info("REQUESTING PHOTO: '" + guid + "'");
        }
        try {
            String url = String.format(urlTemplate, guid);
            ResponseEntity<Base64Photo> response = rest.getForEntity(URI.create(url), Base64Photo.class);
            if(log.isInfoEnabled()) {
                log.info("REQUESTING PHOTO: '" + url + "' DONE");
            }
            return response.getBody();
        } catch (HttpClientErrorException e) {
            switch (e.getStatusCode()){
                case INTERNAL_SERVER_ERROR:log.error("Прочие ошибки");break;
                case NOT_FOUND:log.error("Фото не найдено");break;
                case BAD_REQUEST:log.error("Неполный/неверный запрос");break;
                default:
                    if (e.getStatusCode() != null){
                        log.error(String.format("%s:  %s", e.getStatusCode(), e.getMessage()), e);
                    } else {
                        log.error(e.getMessage(), e);
                    }
            }

        }
        return null;
    }
    public String putPhoto(String base64photo) {
        throw new NotImplementedException();//todo implement;
    }

}
