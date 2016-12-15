package com.usetech.imagestorage.service;


import com.usetech.imagestorage.bean.FileStoreBean;

/**
 * Created by User on 14.11.2016.
 */
public interface FileStoreService {

    boolean saveFile(FileStoreBean fileStoreBean);

    byte[] getFile(String url);
}
