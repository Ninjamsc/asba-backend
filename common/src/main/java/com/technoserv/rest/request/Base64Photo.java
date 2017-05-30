package com.technoserv.rest.request;

import com.google.common.base.MoreObjects;
import org.apache.commons.codec.binary.Base64;

/**
 * Фото в формате base64.
 * Структура класса соответствует структуре json-запроса к большинству сервисов *
 * Created by VBasakov on 22.11.2016.
 */
public class Base64Photo {

    /**
     * base46 encoded изображение
     */
    public String photo;

    public Base64Photo() {
    }

    public Base64Photo(byte[] imageByteArray) {
        photo = "data:image/jpg;base64," + new String(Base64.encodeBase64(imageByteArray));
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("photo", photo)
                .toString();
    }
}
