package com.technoserv.rest.request;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

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
        StringBuilder sb = new StringBuilder();
        sb.append("data:image/jpg;base64,");
        sb.append(new String(Base64.encodeBase64(imageByteArray)));
        photo = sb.toString();
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }
    //    public static void main(String[] args) throws IOException {
//        FileInputStream fileInputStream=null;
//
//        File file = new File("D:\\images\\01-spitzer-omeganebula.jpg");
//
//        byte[] bFile = new byte[(int) file.length()];
//
//            fileInputStream = new FileInputStream(file);
//            fileInputStream.read(bFile);
//            fileInputStream.close();
//
//
//            System.out.println(new Base64Photo(bFile).photo);
//    }

}
