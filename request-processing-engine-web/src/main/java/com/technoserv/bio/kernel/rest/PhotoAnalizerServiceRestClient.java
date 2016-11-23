package com.technoserv.bio.kernel.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.technoserv.bio.kernel.rest.exception.PhotoAnalizerServiceException;
import com.technoserv.bio.kernel.rest.response.PhotoAnalyzeResult;
import com.technoserv.rest.request.Base64Photo;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.URI;

import static org.springframework.http.HttpStatus.NOT_EXTENDED;

/**
 * Created by VBasakov on 22.11.2016.
 */
@Service
//@PropertySource("classpath:TemplateBuilderServiceRestClient.properties")
public class PhotoAnalizerServiceRestClient {

    private static final Log log = LogFactory.getLog(PhotoAnalizerServiceRestClient.class);

//    @Value("${http.rest.client.url}")
    //todo fix it
    private String url;

    private RestTemplate rest = new RestTemplate();

    /**
     * В случае успеха (библиотека анализа изображений не нашла несоответствий) возврат HTTP 200 OK безJSON документа
     * */
    public void analizePhoto(String base64photo) { //todo make correct implementation
        if (log.isInfoEnabled()) {
            log.info("ANALYZING TEMPLATE: '" + base64photo + "'");
        }
        try {
            Base64Photo request = new Base64Photo(base64photo);
            ResponseEntity<PhotoAnalyzeResult> response = rest.exchange(URI.create(url), HttpMethod.PUT, new HttpEntity<>(request), PhotoAnalyzeResult.class);
            if (log.isInfoEnabled()) {
                log.info("ANALYZING TEMPLATE: '" + base64photo + "' DONE");
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
}
