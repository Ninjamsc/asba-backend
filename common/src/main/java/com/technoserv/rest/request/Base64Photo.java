package com.technoserv.rest.request;

import java.util.Base64;
/**
 * Фото в формате base64.
 * Структура класса соответствует структуре json-запроса к большинству сервисов *
 * Created by VBasakov on 22.11.2016.
 */
public class Base64Photo {

    /** base46 encoded изображение */
    public String photo;

    public Base64Photo(byte[] imageByteArray) {
        StringBuilder sb = new StringBuilder();
        sb.append("data:image/png;base64,");
        sb.append(new String(Base64.getEncoder().encode(imageByteArray)));
        photo = sb.toString();
    }
}
