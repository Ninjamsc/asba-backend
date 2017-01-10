package com.usetech.imagestorage.service;


import com.usetech.imagestorage.bean.ImageStoreBean;

/**
 * Created by User on 14.11.2016.
 */
public interface FileStoreService {

    boolean saveFile(ImageStoreBean imageStoreBean);

    byte[] getFile(String url);
}
