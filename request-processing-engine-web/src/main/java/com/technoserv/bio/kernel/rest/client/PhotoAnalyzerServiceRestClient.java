package com.technoserv.bio.kernel.rest.client;

import com.technoserv.bio.kernel.rest.exception.PhotoAnalizerServiceException;
import com.technoserv.bio.kernel.rest.response.PhotoAnalyzeResult;
import com.technoserv.db.model.configuration.SystemSettingsType;
import com.technoserv.db.service.configuration.impl.SystemSettingBean;
import com.technoserv.rest.request.Base64Photo;
import com.technoserv.rest.request.PhotoSaveRequest;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
public class PhotoAnalyzerServiceRestClient {

    private static final Log log = LogFactory.getLog(PhotoAnalyzerServiceRestClient.class);

    private RestTemplate rest = new RestTemplate();

    @Autowired
    private SystemSettingBean systemSettingBean;

    /**
     * В случае успеха (библиотека анализа изображений не нашла несоответствий) возврат HTTP 200 OK безJSON документа
     * */
    public void analyzePhoto(byte[] request) {
        String url = getUrl();
        if(request==null) {
            return;
        }
        if (log.isInfoEnabled()) {
            log.info(url + " ANALYZING TEMPLATE: '" + request + "'");
        }
        try {
            HttpHeaders requestHeaders = new HttpHeaders();
            requestHeaders.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<Base64Photo> requestEntity = new HttpEntity<Base64Photo>(new Base64Photo(request),requestHeaders);
            rest.exchange(URI.create(url), HttpMethod.PUT, requestEntity, PhotoAnalyzeResult.class);
            if (log.isInfoEnabled()) {
                log.info("ANALYZING TEMPLATE:  DONE");
            }
        } catch (RestClientResponseException e) {
            switch (e.getRawStatusCode()) {
                //На первом этапе сервис выполняется в виде заглушки, всегда возвращающей HTTP 200 ОК.
                /*Стандартные названия ошибок не совпадают с нашей документацией  только коды */
                case 510:log.error("510 ошибка анализа изображения");throw new PhotoAnalizerServiceException(e.getResponseBodyAsString());
                case 400:log.error("Неполный/неверный запрос");throw new PhotoAnalizerServiceException(e.getResponseBodyAsString());
//                case 500://log.error("Прочие ошибки");
                default:
                    throw new RuntimeException(e.getResponseBodyAsString());
            }
        }

    }

    public String getUrl() {
        return systemSettingBean.get(SystemSettingsType.PHOTO_ANALYZER_SERVICE_URL);
    }

    public static void main(String[] args) {
        PhotoAnalyzerServiceRestClient restClient = new PhotoAnalyzerServiceRestClient(){
            @Override
            public String getUrl() {
                return "http://localhost:8080/quality/rest/test";
            }
        };
        restClient.analyzePhoto("/9j/4AAQSkZJRgABA".getBytes());

    }
}
