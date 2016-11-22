package com.technoserv.bio.kernel.rest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

/**
 * Created by VBasakov on 22.11.2016.
 */
@Service
@PropertySource("classpath:TemplateBuilderServiceRestClient.properties")
public class TemplateBuilderServiceRestClient {

    private static final Log log = LogFactory.getLog(TemplateBuilderServiceRestClient.class);

    @Value("${http.rest.client.url}")
    private String url;

    private RestTemplate rest = new RestTemplate();

    public boolean getPhotoTemplate(String base64photo) {
        if(log.isInfoEnabled()) {
            log.info("REQUESTING TEMPLATE: '" + base64photo + "'");
        }
        try {
            rest.put(URI.create(url), base64photo);
            /* Вариант использования
            ResponseEntity<String> response = rest.exchange(URI.create(url), HttpMethod.POST, new HttpEntity<String>(base64photo), String.class);*/
            if(log.isInfoEnabled()) {
                log.info("SENDING MESSAGE: '" + base64photo + "' DONE");
            }
            return true;
        } catch (HttpClientErrorException e) {
            /**                  /* Шаблон URL вызова	HTTPS://<hostname>:<port>/api/bio/build
             Метод	PUT
             Коды возврата	200 OK	Успешная операция
             510	base64 не является фотографией
             511	не удалось рассчитать биометрический шаблон. Внутренняя ошибка (в CUDA)
             512	outOfMemory на GPU
             500 ERROR	Прочие ошибки

             {
             photos: '"base64 encoded image'"
             }
             */
            switch (e.getStatusCode()){

            }

        }
        return false;
    }
}
