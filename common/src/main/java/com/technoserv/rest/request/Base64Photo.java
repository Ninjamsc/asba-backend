package com.technoserv.rest.request;

/**
 * Фото в формате base64.
 * Структура класса соответствует структуре json-запроса к большинству сервисов *
 * Created by VBasakov on 22.11.2016.
 */
public class Base64Photo {

    /** base46 encoded изображение */
    public String photos;

    public Base64Photo(String base64photo) {

    }
}
