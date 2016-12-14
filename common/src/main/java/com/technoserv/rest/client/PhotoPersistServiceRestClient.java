package com.technoserv.rest.client;

import com.technoserv.db.model.configuration.SystemSettingsType;
import com.technoserv.db.service.configuration.impl.SystemSettingsBean;
import com.technoserv.rest.exception.PhotoPersistServiceException;
import com.technoserv.rest.request.PhotoSaveRequest;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.Arrays;

/**
 * Created by VBasakov on 22.11.2016.
 */
@Service
public class PhotoPersistServiceRestClient {

    private static final Log log = LogFactory.getLog(PhotoPersistServiceRestClient.class);

    @Autowired
    private SystemSettingsBean systemSettingsBean;

    private  RestTemplate rest = new RestTemplate();

    public PhotoPersistServiceRestClient() {
        this.rest = new RestTemplate();
        this.rest.getMessageConverters().add(new ByteArrayHttpMessageConverter());
    }

    public byte[] getPhoto(String photoUrl) {
        if(photoUrl==null) {
            return null;
        }
        if(log.isInfoEnabled()) {
            writeLog("REQUESTING PHOTO: '" + photoUrl + "'");
        }
        try {
            HttpHeaders  headers = new HttpHeaders();
            headers.setAccept(Arrays.asList(MediaType.APPLICATION_OCTET_STREAM));

            HttpEntity<String> entity = new HttpEntity<String>(headers);

            ResponseEntity<byte[]> response = rest.exchange(URI.create(photoUrl), HttpMethod.GET, entity, byte[].class);
            if(log.isInfoEnabled()) {
                writeLog("REQUESTING PHOTO: '" + photoUrl + "' DONE");
            }
            return response.getBody();
        } catch (RestClientResponseException e) {
            writeError(e.getMessage());
            writeError("Error status code"+e.getRawStatusCode());
            switch (e.getRawStatusCode()){
                case 500://log.error("Прочие ошибки");break;
                case 404://log.error("Фото не найдено");break;
                case 400://log.error("Неполный/неверный запрос");break;
                default:
                    writeError(e.getResponseBodyAsString());
                    throw new PhotoPersistServiceException(e.getResponseBodyAsString());
            }
        }
    }
    public String putPhoto(String file_content, String file_name) {
        String url = systemSettingsBean.get(SystemSettingsType.PHOTO_PERSIST_SERVICE_URL);
        if(file_content==null) {
            writeLog("No Content.");
            return null;
        }
        file_name = String.format("%s.jpg", file_name);
        PhotoSaveRequest request = new PhotoSaveRequest(file_content, file_name);

        if(log.isInfoEnabled()) {
            writeLog("SAVING PHOTO: '" + file_name + "'" + " content:'" + file_content+"'");
        }
        try {
            String finalUrl = String.format("%s/%s", url, file_name);
            //todo request -> json with jackson
            HttpHeaders requestHeaders = new HttpHeaders();
            requestHeaders.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<PhotoSaveRequest> requestEntity = new HttpEntity<PhotoSaveRequest>(request,requestHeaders);
            ResponseEntity<String> response = rest.exchange(URI.create(url), HttpMethod.PUT, requestEntity, String.class);

            if(log.isInfoEnabled()) {
               writeLog("SAVING PHOTO: '" + url + "' DONE");
            }
            return finalUrl;
        } catch (RestClientResponseException e) {
            writeError(e.getMessage());
            writeError("Error status code"+e.getRawStatusCode());
            switch (e.getRawStatusCode()){
                case 500://log.error("Прочие ошибки");break;
                case 400://log.error("Неполный/неверный запрос");break;
                default:
                    writeError(e.getResponseBodyAsString());
                    throw new PhotoPersistServiceException(e.getResponseBodyAsString());
            }

        }
    }

    private void writeLog(String message) {
        System.out.println(message);
        log.info(message);
    }
    private void writeError(String message) {
        System.out.println(message);
        log.error(message);
    }



//    public static void main(String[] args) {
//        String url = "http://localhost:8080/storage/rest/image";
//        String file_name = "1";
//        String file_content = "content";
//        String urlTemplate = String.format("%s/%s.jpg", url, "%s");
//        String finalUrl = String.format(urlTemplate, file_name);
//        System.out.println(finalUrl);
//        RestTemplate rest = new RestTemplate();
//        HttpHeaders requestHeaders = new HttpHeaders();
//        requestHeaders.setContentType(MediaType.APPLICATION_JSON);
//        PhotoSaveRequest request = new PhotoSaveRequest(file_content, file_name);
//        HttpEntity<PhotoSaveRequest> requestEntity = new HttpEntity<PhotoSaveRequest>(request,requestHeaders);
//        ResponseEntity<String> response = rest.exchange(URI.create(url), HttpMethod.PUT, requestEntity, String.class);
//
//
//    }

}
