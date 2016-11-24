package com.technoserv.rest.client;

import com.technoserv.rest.exception.PhotoPersistServiceException;
import com.technoserv.rest.request.Base64Photo;
import com.technoserv.rest.request.PhotoSaveRequest;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientResponseException;
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

    @Value("${http.photo.persist.service.url}")
    private String url;

    private RestTemplate rest = new RestTemplate();

    public Base64Photo getPhoto(String guid) {
        if(log.isInfoEnabled()) {
            log.info("REQUESTING PHOTO: '" + guid + "'");
        }
        try {
            String urlTemplate = String.format("%s/%s.jpg", url, "%s");
            String finalUrl = String.format(urlTemplate, guid);
            ResponseEntity<Base64Photo> response = rest.getForEntity(URI.create(finalUrl), Base64Photo.class);
            if(log.isInfoEnabled()) {
                log.info("REQUESTING PHOTO: '" + finalUrl + "' DONE");
            }
            return response.getBody();
        } catch (RestClientResponseException e) {
            log.error(e);
            switch (e.getRawStatusCode()){
                case 500://log.error("Прочие ошибки");break;
                case 404://log.error("Фото не найдено");break;
                case 400://log.error("Неполный/неверный запрос");break;
                default:
                    throw new PhotoPersistServiceException(e.getResponseBodyAsString());
            }
        }
    }
    public String putPhoto(String timestamp, String file_content, String file_name) {
        PhotoSaveRequest request = new PhotoSaveRequest(timestamp, file_content, file_name);

        if(log.isInfoEnabled()) {
            log.info("SAVING PHOTO: '" + file_name + "'");
        }
        try {
            String urlTemplate = String.format("%s/%s.jpg", url, "%s");
            String finalUrl = String.format(urlTemplate, file_name);
            //todo request -> json with jackson
            ResponseEntity<String> response = rest.exchange(URI.create(finalUrl), HttpMethod.PUT, new HttpEntity<PhotoSaveRequest>(request), String.class);

            if(log.isInfoEnabled()) {
                log.info("SAVING PHOTO: '" + url + "' DONE");
            }
            return finalUrl;
        } catch (RestClientResponseException e) {
            log.error(e);
            switch (e.getRawStatusCode()){
                case 500://log.error("Прочие ошибки");break;
                case 400://log.error("Неполный/неверный запрос");break;
                default:
                    throw new PhotoPersistServiceException(e.getResponseBodyAsString());
            }

        }
    }

}
