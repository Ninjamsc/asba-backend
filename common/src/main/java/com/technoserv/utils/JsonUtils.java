package com.technoserv.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.log4j.Logger;

import java.io.IOException;

/**
 * Created by Andrey on 23.11.2016.
 */
public class JsonUtils {

    private static final Logger logger = Logger.getLogger(JsonUtils.class);

    public static <T> String serializeJson(T object) {
        String json;
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
        try {
            json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(object);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
        return json;
    }

    public static <T> T deserializeJson(String jsonData, Class<T> clazz) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
        T data;
        try {
            data = mapper.readValue(jsonData, clazz);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
        return data;
    }
}