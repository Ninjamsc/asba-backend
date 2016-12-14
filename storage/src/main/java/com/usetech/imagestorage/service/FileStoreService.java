//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.usetech.imagestorage.service;

import com.usetech.imagestorage.bean.FileStoreBean;

public interface FileStoreService {
    boolean saveFile(FileStoreBean var1);

    byte[] getFile(String var1);
}
