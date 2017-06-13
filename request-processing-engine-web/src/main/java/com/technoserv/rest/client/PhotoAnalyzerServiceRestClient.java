package com.technoserv.rest.client;

import com.technoserv.db.model.configuration.SystemSettingsType;
import com.technoserv.db.service.configuration.impl.SystemSettingsBean;
import com.technoserv.rest.exception.PhotoAnalyzerServiceException;
import com.technoserv.rest.request.Base64Photo;
import com.technoserv.rest.response.PhotoAnalyzeResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

/**
 * Created by VBasakov on 22.11.2016.
 */
@Service
public class PhotoAnalyzerServiceRestClient {

    private static final Logger log = LoggerFactory.getLogger(PhotoAnalyzerServiceRestClient.class);

    private RestTemplate rest = new RestTemplate();

    @Autowired
    private SystemSettingsBean systemSettingsBean;

    /**
     * В случае успеха (библиотека анализа изображений не нашла несоответствий) возврат HTTP 200 OK безJSON документа
     */
    public void analyzePhoto(byte[] request) {
        log.debug("analyzePhoto request (size): {}", request.length);
        if (request.length == 0) {
            log.warn("An empty photo is not allowed for the analysis. Photo will not analysed.");
            return;
        }

        try {
            HttpHeaders requestHeaders = new HttpHeaders();
            requestHeaders.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<Base64Photo> requestEntity = new HttpEntity<>(new Base64Photo(request), requestHeaders);
            rest.exchange(URI.create(getUrl()), HttpMethod.PUT, requestEntity, PhotoAnalyzeResult.class);
            log.debug("Photo analysis complete.");

        } catch (RestClientResponseException e) {
            log.error(String.format("Can't analyze photo. Size: %s", request.length), e);
            switch (e.getRawStatusCode()) {
                // На первом этапе сервис выполняется в виде заглушки, всегда возвращающей HTTP 200 ОК.
                // Стандартные названия ошибок не совпадают с нашей документацией только коды
                case OurErrorCodes.PHOTO_ANALYSIS_ERROR:
                    log.error("Ошибка анализа изображения.", e);
                    throw new PhotoAnalyzerServiceException(e.getResponseBodyAsString());

                case OurErrorCodes.BAD_REQUEST:
                    log.error("Неполный/неверный запрос.", e);
                    throw new PhotoAnalyzerServiceException(e.getResponseBodyAsString());

                default:
                    throw new RuntimeException(e.getResponseBodyAsString());
            }
        }
    }

    public String getUrl() {
        return systemSettingsBean.get(SystemSettingsType.PHOTO_ANALYZER_SERVICE_URL);
    }

    public static void main(String[] args) {
        PhotoAnalyzerServiceRestClient restClient = new PhotoAnalyzerServiceRestClient() {
            @Override
            public String getUrl() {
                return "http://localhost:8080/quality/rest/test";
            }
        };
        restClient.analyzePhoto("/9j/4AAQSkZJRgABA".getBytes());
    }

}
