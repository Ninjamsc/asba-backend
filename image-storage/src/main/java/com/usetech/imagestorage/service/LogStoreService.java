package com.usetech.imagestorage.service;


import com.usetech.imagestorage.bean.LogStoreBean;

/**
 * Created by User on 14.11.2016.
 */
public interface LogStoreService {

    boolean saveFile(LogStoreBean logStoreBean);

    byte[] getFile(String url);
}
