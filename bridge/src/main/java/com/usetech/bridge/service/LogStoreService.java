package com.usetech.bridge.service;


import com.usetech.bridge.bean.LogStoreBean;

/**
 * Created by User on 14.11.2016.
 */
public interface LogStoreService {

    boolean saveFile(LogStoreBean logStoreBean);

    byte[] getFile(String url);
}
