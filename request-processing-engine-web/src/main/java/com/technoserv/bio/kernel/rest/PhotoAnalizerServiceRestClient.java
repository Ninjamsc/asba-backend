package com.technoserv.bio.kernel.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.technoserv.bio.kernel.rest.response.PhotoAnalyzeResult;
import com.technoserv.rest.request.Base64Photo;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.URI;

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

    public PhotoAnalyzeResult analizePhoto(String base64photo) { //todo make correct implementation
        if (log.isInfoEnabled()) {
            log.info("REQUESTING TEMPLATE: '" + base64photo + "'");
        }
        try {
//            rest.put(URI.create(url), base64photo);
            Base64Photo request = new Base64Photo(base64photo);
            ResponseEntity<PhotoAnalyzeResult> response = rest.exchange(URI.create(url), HttpMethod.PUT, new HttpEntity<Base64Photo>(request), PhotoAnalyzeResult.class);
            if (log.isInfoEnabled()) {
                log.info("SENDING MESSAGE: '" + base64photo + "' DONE");
            }
            return response.getBody();
        } catch (HttpClientErrorException e) {
            switch (e.getStatusCode()) {
                /*Стандартные названия ошибок не совпадают с нашей документацией
                 * только коды */
                //todo унаследовать от HttpStatus и добавить/заменить наши
                case NOT_EXTENDED://
                    log.error("510 ошибка анализа изображения");
                    return deserializeError(e.getResponseBodyAsString());
                case BAD_REQUEST:
                    log.error("Неполный/неверный запрос");
                    break;
                case INTERNAL_SERVER_ERROR:
                default:
                    log.error("Неизвестная ошибка");
            }

        }
        return null;
    }

    private PhotoAnalyzeResult deserializeError(String json){
        ObjectMapper mapper = new ObjectMapper();
        PhotoAnalyzeResult res = null;
        try {
            res = mapper.readValue(json, PhotoAnalyzeResult.class);
        } catch (IOException e) {
            e.printStackTrace();
            log.error("Ошибка десериализации ответа сервера");
        }
        return res;
    }
}
