package com.technoserv.rest.client;


import com.technoserv.db.model.configuration.SystemSettingsType;
import com.technoserv.db.service.configuration.impl.SystemSettingsBean;
import com.technoserv.rest.exception.TemplateBuilderServiceException;
import com.technoserv.rest.request.Base64Photo;
import com.technoserv.rest.request.PhotoTemplate;
import edu.emory.mathcs.backport.java.util.Arrays;
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
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

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

        System.out.println("Tevian template builder invoked");

        String url = String.format("%s?version=%s", TEVIAN_CORE_LINK, TEVIAN_CORE_VERSION);
        PhotoTemplate photoTemplate = null;
        try {
            HttpHeaders requestHeaders = new HttpHeaders();
            requestHeaders.setContentType(MediaType.IMAGE_JPEG);
            HttpEntity<byte[]> requestEntity = new HttpEntity<byte[]>(request, requestHeaders);
            ResponseEntity<String> response = rest.exchange(URI.create(url), HttpMethod.PUT, requestEntity, String.class);
            System.out.println("Image vectorBase64: " + response.getBody());
            double[] vector = getVectorFromByteArray(Base64.decode(response.getBody().getBytes()));

            try {
                Thread.sleep(5000l);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // construct result
            photoTemplate = new PhotoTemplate();
            photoTemplate.setType(0);
            photoTemplate.setVersion(Integer.parseInt(TEVIAN_CORE_VERSION));
            photoTemplate.setTemplate(vector);
        } catch (RestClientResponseException e) {
            log.error("BUILDER BIO TEMPLATE ERROR Code: " + e.getRawStatusCode());
            e.printStackTrace();
            throw new TemplateBuilderServiceException(e.getResponseBodyAsString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return photoTemplate;
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

    double[] getVectorFromByteArray(byte[] bytes) {

        int resultLength = bytes.length / 4;
        double[] result = new double[resultLength];

        for (int i = 0; i < resultLength; i++) {
            int start = i * 4;
            int end = start + 4;
            byte[] fourBytes = Arrays.copyOfRange(bytes, start, end);
            float f = ByteBuffer.wrap(fourBytes).order(ByteOrder.LITTLE_ENDIAN).getFloat();
            result[i] = f;
        }
//        Arrays.stream(result).forEach(System.out::println);
        return result;
    }
    public static void main(String[] args) {

        String base64Photo1 = "/9j/4AAQSkZJRgABAQAAAQABAAD/2wCEAAkGBxISEhUSEhIVFRUVFRUVFRUVFRUVFRUVFRUXFhUVFRUYHSggGBolHRUVITEhJSkrLi4uFx8zODMsNygtLisBCgoKDg0OGhAQFy0dHyUtLSsrLS0tLS0tLS0rLS0tLSsrLS0tLS0rLS0tLS0tLS0tKy0tLS0rKy0tLS0tLS0rK//AABEIALcBEwMBIgACEQEDEQH/xAAbAAACAwEBAQAAAAAAAAAAAAAAAQIFBgQDB//EAD0QAAEDAQUFBQYEBQQDAAAAAAEAAhEDBAUSITEGQVFxgSJhkaGxEzJCwdHwUmJy4RQjgrLxBzOi0haSwv/EABkBAQADAQEAAAAAAAAAAAAAAAABAgMEBf/EACERAQEAAgMAAwADAQAAAAAAAAABAhEDITEEEkEiMlFh/9oADAMBAAIRAxEAPwD7EhRlBKqukhRlEoHKJSlKUDlBKUolSBEpJoCUSkmiAhKUSiTQkSqy8L5p0hkQ53CQB1JUZZTGbpJb4s1y2m8aVP3qjR1WHva/KlWWm0Npg/DTlx8RCz77lpOlzn1SeJb9ZXPl8ifjWcX+vqTL5onR8xrGccyuilbqbowvaZ0g6wvixb7M9ioYz1Jb0nRWtxbRUqLg50n4QBxJ9N6Y81t8Tlx6fXJQsdR2t7XuiP1CVprDb2VW4mnnxC2x5McupWdxsdcoUZTWiqSFGUKBKUJIRBpyooQSCcqCcoGE5UZTUBoSlCCKaihWSaajKEDlIlKUigeJOVFAKCQRKSUoJSiVGUSglK8bTaQwST9TyStVoDGlzjACyVst7qxk5N+Fu9w4u4BZcnJMYvhj9nveN41a0ta7Az8uZPXTwVQbupe86XR8Tzl4fRWlGkTBIngBkP2C9SwNIORdun3Wch81xZby7reanUVVOzPA7DGUm8XN7R5MHqT0XPetrpUGTVqD+s4Z6NErqve34QQ0y4/Fv/pHzWNvCy4pe+Blm5xzE95yH3kqdb0srrx2np5+zbRO7NrxP9WFUVS30XnGxuGoDJYD2DGsRlK6atDEf5NOTpidOfU5nyVXb6fs34GkGqPeLR2WH8AjV3E7tF08cx/GGdyjYWC9XPaCGBvGDPiNytbBe1VjgcbhG/6OHzWcuR7nNDiwB+hHEjeI4jPoVfWagKg7Lix+7e0zxWGXVbzuPpez99is0YiJ++GqvJXxa7r0fZ6wZUb7N85Obmxw4r6ncl6tqtjRwGm48l1cXLvqsOTDXcW6JUZRK6GSUpyoolSJSiVGUSgkmFCU1AkmoJgoGhEoRCKEkKUgJpIQCFEoCBoCSAiDQkiUDQhcd52nAzvOQVcstTdTJvpS33ahUcW/A3/k75hc1gs5ccTug3czxXGXGo/CNBr1+ZVlWtDabSNABmePcF59y+13XVJqaTtNpazfzPoAqe1WkmO/3W74/EePLiV5B+KX1Mmtzw8BuHe4/ei9rGwk+0cIcc4/ABk0Ad2nMlVyu1pEP4IMGOp2nncN2WTR3rN3jd76j8TzDdGtHyHzWzLJbiifhaPX/K8P4IAy7N3dx4cgqXc8Wn/WUfZYim3Ko4GIGVNsa/qKqbv2dh2kywFpOZ+IOzOuZb4raOu3CcZ94nM+OS66FiAIMcfv0SZZTpa4xlruughmHQwIO5rtQfHLqVZMswLBUDYzio3ex4OZHP15rQULEJwx8JPgUxRDHEkZOHa5wJPoU7VZ23XeyvTDSc9WP4Hjy4jquO4r0fQqYHyC0xPA8D9wVeWiiKbvyE5/lJ+L6qtvy7iSKoHaGT4+Jm53TTr3K0R0+k3fbBVYHDXeOBXUsJsleRa4MJkHQ8R9Qt0Cu/i5Pti5c8frTQkhaqJISQgaYKimgacqCaBymkhAkISUhpICCUQjKaRKSgSlCSEDRKSEDlZnaW1Zx+H13rSFY++M6megLnHkM1z/ACbrHTbinbzsLcIcTu/uP0C5alQ1H4RmB5kr2quw0gd5GM83afILxu84GufvyDeZyb8yuL903etqAybPZYZceLhr0yiO5SZU0bvdrx+8x1K5LQ/MNGgzPeG/UrrsIzc85wPTTzTe6mRZV6waB3DLqixWcuJcddB3AcPFeTqPxE8FYWEZD771eY7y7T5HneNDIAcfqhlDIKdoqS4749SovqxHcVbKTdO3qKf8xveHD0XnbKQh4/CJ8NfKV7Y+0081F/8AuuG50jxCnUVZ60O7IncSx3I+6T970qJ+B2YiObTkpXjTyewammSN0uZp6BcVlrY2NqZy3XlMLLwrms1J1NxZvaeyeO/zC39yWz2lMHeMisfbqMhr9/uu6e78/BWmztfC+JycJWvDl9clOSbjWylKUold7mSRKjKagOU1EJlA0JApogIQhEiUpRKUqUGkSgFJyJNIlIlCByiVElEoJSglRlCBkrJ35Tkuj4oaeWp+nVatxyWVvatn5nkM/kFzfI8jXicdubicW/CMI8B+647RWgAA8XcssvIea6K1WGTvMnqGkriewvceAODria0+q466IhVdDiOETzgQPFXF3NBpxM4sznuGg8ZVA6kxzyajiAXucc40cYHkuC9b2suXs6lRhyaHNIDcsgO0RIHcr4Y/q1be11Mg3eYVqyq1reQXy+lfdQgezqhxaWgF8EEDUkicyYhaq77wdVAFQBrh7wGnNXm5bT1d0e1LuOinUAjXeoaDJZq/7zrAObTIZHxHMgck10hqPagASQF42i8qQfPtGghw3/NfLjelJnaqPqVSDnL3RJmJwjLQ71oNn9prK5wZ7JoJPxQ4k8zmr/Wq7jT30BIIP4xP6mH5wvnOwF5nFVaZLWZuH5SYJAX0e/WtqUjgykHxhfK/9O6ZFW0/lpQfEn5FVuM1kru7j6fQphzcMyCIB4kCWO6hedikOa7e09r76LiuqqW5TlkW8gVb1GgOkaO1WUWrUsdIBUpXNYnywcgveV6Uu45L6khRBTlEJSiVGUpRCcpgrzBUgUE5QoyhEhJyZUHKRIJlRCZQIpJkpFAJJFCBppIQRrHsnkViL6qdlx4lrRyJz8gtfeLuwRx9N6xN+1MLATvLiPJrfVcXyb3p0cMK8KwDKZn4SepcD6L2u5ocXnhWnoXBw/tKobQ84Jdv92eG70CttmK4ms3UsYCecH/sFhLttZpx2m7H1mnDAkESeJJJ9VRv2JPsy12byZDiSI7o4LeXXAYB3KzbTB3LbDcnSM8ZfXz27NmC0PLodUcRFT3cOERk0CDP0yWloWMswknMAAkTB8VeuprktA9VOW76YyTx2SS3oqS9rpNVpAMA5nKSRwV0z3EMKDBWjYljnlwBGIglrYDctOzHefEwrulspQJFV7P5uUkZCAIjCFqmjuXnWCv2rJHHWpYWL53slSFM20gbyJ/9o9V9Ht1QCm48AvnN0jDZ7S/e8mPAx6rPPzS2mgu900wd7fQ/5CuhVmBuLWme8NH7qiuBwhnBwE+EH0Vs2QcO9oA/uIWP6itNdjsiOHpqF2qpulxDsJ4Zct3ll0VsvQ4rvFy5+miUkpWiqSUpIlBIJhQUpUCUoUZQgmkUFRKkNCSEAgoSKAKEkKQwhJOVA47zHZjjA8SsftJRxmk0HLESeQJJ+S2ltZLeRB8CsXf1QyA3i/oJK4vkTt08N6VFrZ7R7YyY0+TRM+K59nDUbWtFRzT7N4FNroyxlwAE8gu68m4aYYNXAA9w39TKy197U1KRFmbAZTrNrSB2nOMZOJOgEGI1KpxY7Wyum/u6roFeUnrL3PVyHBXramSnFrXXXrABVrXEwSciTC6A2QSdT5Kkt13PdGGq8R7sGI+vVWV01DGDDqFzWqk5pDhplpn3Kibaa3uwQdMUZDvAXrYLI5mtWo4HUOcXT4/JNml/RtQIUalZVlQEGW9VzV6r0uSdFtXbJpGjT954DTGoxZZd+p6LPWyyClSbTboXNzLg4ns4pka9MlT7eWt7G02scWvNUODgYIwCZnnC5KN4uqVWYnEhrTmfFxjdOFvilm5tnb/LTTXK+KbOILvJzh6FaiJqMducBPQz/wDR8FlLE7DSaTlDiTyLs/Ja+xNGBhO4nrB0WU/sm+La72dtv6Pv1VqVw2KnDv0taOpH0hdq7+OajlyvYSQhXVCEkBBJCQTQNCSEE0kFCAQkiUAUkEoQBSSKUoJIlRTlA3iRCyt42YA4juk9ZzWpBWX2ufhpuG9xDOh19VzfInW23D7pmHVMfa7sXTX0jxXy69apq1KjzvefCYHoF9FvOthoVMOuENHh/hYFlDXn6rPhut1fkm292IvEVKTQT2m9l3MaeIgrZ03ZL4vctvNltGL4SAHjumJ6L61YbUHAFpBBCZTVaY5biVttlVmfsyW9xE+G9cI2hbMYYP5jCvAZEFVFru3PIDkRIKhrh9f16/8Ak1OM6ef6hC4zfznGG0geRy8V6NogZCz0sWQxbsp7102OwHUnPdAyCWr/AMJ+PWx0KsY3uGfwgZDrqVKvAPLM9F1vkNAmTGZWX2kvTBTfBzwu6AKLGW2C2mvEV7S7Dm2n2BwnVx8RHReVkdOM8Ypt5GB6AlUliJJP5vnqr2xsJfTYM+0MXed8eELbOamnJhbldt2yDSAGhkxzJIWqsNPs0J07RPOGgepWWs9OSB06AR81t7M0e0o0vwMFR3cTk30K5sJvJvl1iuaDIGepJJ5n78l6lRamvRjjCRTSlSEmkUIGEJJyiBKEkKEvRJOVFSBJCEAkhBQBSKaEEUJoQAWX2ypktJHwhviZWoVHtLSxUqvcB/lYc83i04rrJ87t4xNI/N6wsjUbhqEcitecyR3+BjL0WdvqhD9Ny5cK6rFZeVD33cGu9ZXfsVe9Zji2cVOJAO7POErYyaFU78Jb4x+6jsPVLy6nAwsAc4xnJMa8MtO9dE7wrC9ckfUbovZlUAg56Ebwe9XbKTSvlDHOa8uYSMzC0d37WFgio0zxGaymU/XRqtt/DMG5JzQM1k37Y053+BXJadp6j8qbSO8/RTbDVWm0V9NpiG5uIyH1Xznaq2EUgwmX1Tn3NHvfIeK0dOylxxuMuPH70WQvW7LTaLQ/BRqYGnA04HAQ3fmM5MlTx6uW6y5rZjqeqyw08G8FxmACOz3nv+q19xWAs7Th2oybvBOk8DG7vTuPZf2Pbfk6PecQXD9I0bzzK0FmpMbm4hrRpM9ric8zPHeq83JLekcWGp2troswyLtBH1JWnuOmXOfXPxwG/ob7v16rEVbznJoMbmjJzzxPBq2Gz9+Ne0MqD2bxkAcgeR0nqo+Prfaebel+hCUrvcoSTKSARKEIgJykEIGhCFAmkmUpUpIJpJoEUkIQCEJoIwmmiFAS4rbRxYm7ntI8v8LuUXNlRZuJl0+T22zGnWIiJMjmNR6qtvuy4hO/5/crf7T3WCZ3nMQcw7j1WdtFmxCCO0NRx7wvOs+l07JlubYC9Lc2jRLR/uPGEN3RGbj971xbKBzQ9+YmBzXntTYKrarnFpLdGuAkR38Cru7rJhYxgG4E8yF13U4+v1hju8nf4ubJZ5bK9zZOIVhdlDsrpdRyXJXZKpG2LMZL1dR9mZIJ7griwUMRldta7wRmkMqz5tbNRl3Ktt9+vp6Af1OIy5BZXbG82urYaDzhZMuaSAXdx3gcVROtrz8S6MPjWzdcmXyJLZI+h2a/faAnC5sfn+ZaY8V22Ivq5sZh/O8ueeYLl4bK7Kk0KdV5AD2h+cmcQkZLUWSxADMmNwGSxzxkuo3wt1uo2OytYNc95OpPNdbKk5AJMojcF0NplVnS3rqsVowHtCRwl0dBMeS0NltlN/ukcjkVnaVAxmpGktsOW4ssuKVqCkqi77a4HC4y3TPUfsrhdWOcynTmyxuN1SQEFCuqAmkmiAkmkgmUk0kSEBNJA4QhCBITSJ3oGq63XvTpZTid+FvzOgXFeV6uPZp5DjvPLgqg0YWGfLrrFthxftdFo2irzLWsaOBE+cqutl+Wp4jHhG/AMPnqvV1CdV4VmQsLyZf61mGP+KO0Wz2QfUqOIDQXFxzP7rxvnamnQDBUElwBgDQRv3jPmsrtpfofXbRbnTpPBfHxPB93kPWVmr3t7qzy92uQjgOHn5rTHg+2rkxz5tXUfQWbR2atETO/XLnMZd67LKQ93ZEjd39/JYzYW7BVe5x0bAHXNfTrBY2shZcmEwy1G/Flcsd17WGzQD96qNpbngG/XuG9ez7RGmq8qIgyTmdVRq6KZwCG+ML57t9tU+XWWk88Krh/YD6+HFabbC/f4SgSCPavltMb5jN3IT4wvjbnEkkkknMk5kk6kldXBxb/AJVy8/Jr+MRKEJgTlxK63G+62Sths9Jgyimwf8QF10GzAVXTBho4AeiuLKF5l9enPHdTpgL3oUwSuemV12fJB0+yUHMXUwoICvpG3CGDcreyvlo4jIqvAG5dtiGRWnF1WfLNx0JJohdDmCaEICEk0KRJIoQgEIQgaJSQgJXJbXzLR1+iEKuXi+E7UNRuZUCE0LirrjzesntzfJs1AlvvvOBncSM3dAhCnim85Kpy3WNsfIaY1JXm4oQvRedPX0H/AE3pxTefxOPkAFuY4aIQvO5f716fF/SE0L1ADQXHQAuPIZoQqYxe+PjG099Otdd1U+6OzTbwYNOp1PNVCaF6cmpqPKt3d0l62ZsuYOL2jzCEKb4T1919jou+k2EIXl16bppDNdlIIQpg6GOXqSkhXQ8t67bIUIVuP1Tk/q6kShC6nKEIQgJQhCD/2Q==";
        String base64Photo2 = "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";

        TemplateBuilderServiceRestClient restClient = new TemplateBuilderServiceRestClient() {
            public String getUrl() {
                return "http://localhost:8081/api/bio/build/";
            }
        };

        restClient.setTEVIAN_CORE_MODE(true);
        restClient.setTEVIAN_CORE_VERSION("1");
        restClient.setTEVIAN_CORE_LINK("http://192.168.99.100:32768/generate");


        PhotoTemplate photoTemplate1 = restClient.getPhotoTemplate(Base64.decode(base64Photo1.getBytes()));
        System.out.println("photoTemplate1 = " + photoTemplate1);
        try {
            Thread.sleep(5000l);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        PhotoTemplate photoTemplate2 = restClient.getPhotoTemplate(Base64.decode(base64Photo2.getBytes()));
        System.out.println("photoTemplate2 = " + photoTemplate2);

    }

}
