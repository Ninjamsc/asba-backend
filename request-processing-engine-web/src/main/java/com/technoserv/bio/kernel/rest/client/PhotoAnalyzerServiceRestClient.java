package com.technoserv.bio.kernel.rest.client;

import com.technoserv.bio.kernel.rest.exception.PhotoAnalizerServiceException;
import com.technoserv.bio.kernel.rest.response.PhotoAnalyzeResult;
import com.technoserv.rest.request.Base64Photo;
import com.technoserv.rest.request.PhotoSaveRequest;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
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
@PropertySource("classpath:photoAnalyzerServiceRestClient.properties")
public class PhotoAnalyzerServiceRestClient {

    private static final Log log = LogFactory.getLog(PhotoAnalyzerServiceRestClient.class);

    @Value("${http.photo.analyzer.service.url}")
    private String url = "http://localhost:8080/quality/rest/test";

    private RestTemplate rest = new RestTemplate();

    /**
     * В случае успеха (библиотека анализа изображений не нашла несоответствий) возврат HTTP 200 OK безJSON документа
     * */
    public void analyzePhoto(String base64photo) {
        if (log.isInfoEnabled()) {
            log.info("ANALYZING TEMPLATE: '" + base64photo + "'");
        }
        try {
            Base64Photo request = new Base64Photo(base64photo);
            HttpHeaders requestHeaders = new HttpHeaders();
            requestHeaders.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<Base64Photo> requestEntity = new HttpEntity<Base64Photo>(request,requestHeaders);
            rest.exchange(URI.create(url), HttpMethod.PUT, requestEntity, PhotoAnalyzeResult.class);
            if (log.isInfoEnabled()) {
                log.info("ANALYZING TEMPLATE:  DONE");
            }
        } catch (RestClientResponseException e) {
            switch (e.getRawStatusCode()) {
                //На первом этапе сервис выполняется в виде заглушки, всегда возвращающей HTTP 200 ОК.
                /*Стандартные названия ошибок не совпадают с нашей документацией  только коды */
                case 510://log.error("510 ошибка анализа изображения");
                case 400://log.error("Неполный/неверный запрос");
                case 500://log.error("Прочие ошибки");
                default:
                    throw new PhotoAnalizerServiceException(e.getResponseBodyAsString());
            }
        }

    }

    public static void main(String[] args) {
        PhotoAnalyzerServiceRestClient restClient = new PhotoAnalyzerServiceRestClient();
        restClient.analyzePhoto("/9j/4AAQSkZJRgABA");

    }
}
