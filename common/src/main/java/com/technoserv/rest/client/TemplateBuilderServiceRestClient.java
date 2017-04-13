package com.technoserv.rest.client;


import com.technoserv.db.model.configuration.SystemSettingsType;
import com.technoserv.db.service.configuration.impl.SystemSettingsBean;
import com.technoserv.rest.exception.TemplateBuilderServiceException;
import com.technoserv.rest.request.Base64Photo;
import com.technoserv.rest.request.PhotoTemplate;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.*;
import org.springframework.security.crypto.codec.Base64;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

/**
 * Created by VBasakov on 22.11.2016.
 */
@Service
@PropertySource("classpath:/application.properties")
public class TemplateBuilderServiceRestClient {

    @Value("${tevian.core.mode}")
    private boolean TEVIAN_CORE_MODE;

    @Value("${tevian.core.version}")
    private String TEVIAN_CORE_VERSION;

    @Value("${tevian.core.link}")
    private String TEVIAN_CORE_LINK;

    private static final Log log = LogFactory.getLog(TemplateBuilderServiceRestClient.class);

    private RestTemplate rest = new RestTemplate();

    @Autowired
    private SystemSettingsBean systemSettingsBean;

    public String getUrl() {
        return systemSettingsBean.get(SystemSettingsType.TEMPLATE_BUILDER_SERVICE_URL);
    }

    public PhotoTemplate getPhotoTemplate(byte[] request) {

        if (TEVIAN_CORE_MODE) {
            return getPhotoTemplateFromTevianCore(request);
        } else {
            return getPhotoTemplateFromTechCore(request);
        }
    }

    public PhotoTemplate getPhotoTemplateFromTevianCore(byte[] request) {

        System.out.println("Tevian Core worked");

        String url = String.format("%s?version=%s", TEVIAN_CORE_LINK, TEVIAN_CORE_VERSION);
        try {
            //todo request -> json with jackson
            HttpHeaders requestHeaders = new HttpHeaders();
            requestHeaders.setContentType(MediaType.IMAGE_JPEG);
            HttpEntity<byte[]> requestEntity = new HttpEntity<byte[]>(request, requestHeaders);
            ResponseEntity<String> response = rest.exchange(URI.create(url), HttpMethod.PUT, requestEntity, String.class);
            if (log.isInfoEnabled()) {
                log.info("BUILDER BIO TEMPLATE: DONE");
            }
            System.out.println("Image vectorBase64: " + response.getBody());
            System.exit(0);
            return new PhotoTemplate();
//            return response.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public PhotoTemplate getPhotoTemplateFromTechCore(byte[] request) {

        String url = getUrl();
        if (request == null) {
            return null;
        }
        if (log.isInfoEnabled()) {
            log.info(url + " BUILDER BIO TEMPLATE: '" + request + "'");
        }
        try {
            //todo request -> json with jackson
            HttpHeaders requestHeaders = new HttpHeaders();
            requestHeaders.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<Base64Photo> requestEntity = new HttpEntity<Base64Photo>(new Base64Photo(request), requestHeaders);
            ResponseEntity<PhotoTemplate> response = rest.exchange(URI.create(url), HttpMethod.PUT, requestEntity, PhotoTemplate.class);
            if (log.isInfoEnabled()) {
                log.info("BUILDER BIO TEMPLATE: DONE");
            }
            return response.getBody();
        } catch (RestClientResponseException e) {
            log.info("BUILDER BIO TEMPLATE ERROR Code: " + e.getRawStatusCode());
            e.printStackTrace();
            switch (e.getRawStatusCode()) {
                /*Стандартные названия http-ошибок не совпадают с нашей документацией только коды */
                case 510:
                    log.error("510 base64 не является фотографией");
                    throw new TemplateBuilderServiceException(e.getResponseBodyAsString());
                case 511:
                    log.error("511 не удалось рассчитать биометрический шаблон. Внутренняя ошибка (в CUDA)");
                    throw new TemplateBuilderServiceException(e.getResponseBodyAsString());
//                case 500:log.error("500 Internal Server Error");
//                case 512:log.error("512 outOfMemory на GPU");
                default:
                    throw new RuntimeException(e.getResponseBodyAsString());
            }
        }
    }

    public void setTEVIAN_CORE_MODE(boolean TEVIAN_CORE_MODE) {
        this.TEVIAN_CORE_MODE = TEVIAN_CORE_MODE;
    }

    public void setTEVIAN_CORE_VERSION(String TEVIAN_CORE_VERSION) {
        this.TEVIAN_CORE_VERSION = TEVIAN_CORE_VERSION;
    }

    public void setTEVIAN_CORE_LINK(String TEVIAN_CORE_LINK) {
        this.TEVIAN_CORE_LINK = TEVIAN_CORE_LINK;
    }

    public static void main(String[] args) {
        String base64Photo = "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";
//        TemplateBuilderServiceRestClient restClient = new TemplateBuilderServiceRestClient();

        TemplateBuilderServiceRestClient restClient = new TemplateBuilderServiceRestClient() {
            public String getUrl() {
                return "http://localhost:8081/api/bio/build/";
            }
        };

        restClient.setTEVIAN_CORE_MODE(true);
        restClient.setTEVIAN_CORE_VERSION("1");
        restClient.setTEVIAN_CORE_LINK("http://192.168.99.100:32768/generate");


        PhotoTemplate photoTemplate = restClient.getPhotoTemplate(Base64.decode(base64Photo.getBytes()));
        System.out.println("photoTemplate = " + photoTemplate);


//        TemplateBuilderServiceRestClient restClient = new TemplateBuilderServiceRestClient(){
//            public String getUrl() {
//                return "http://www.sdorohov.ru/rpe/api/rest/template-builder-stub";
//            }
//        };
//        restClient.getPhotoTemplate(new byte[]{});
    }

}
