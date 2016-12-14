//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.usetech.imagestorage.service;

import com.usetech.imagestorage.bean.FileStoreBean;
import com.usetech.imagestorage.config.CommonConfig;
import com.usetech.imagestorage.service.FileStoreService;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import javax.annotation.PostConstruct;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FileStoreServiceImpl implements FileStoreService {
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private CommonConfig config;

    public FileStoreServiceImpl() {
    }

    @PostConstruct
    private void validate() {
        Path path = Paths.get(this.config.getRootDir(), new String[0]);
        File rootPath = path.toFile();
        if(rootPath.exists() && rootPath.isDirectory()) {
            if(rootPath.canRead() && rootPath.canWrite()) {
                this.log.info("Root path: \'{}\' is valid", rootPath.getAbsolutePath());
            } else {
                throw new RuntimeException("Root path \'" + this.config.getRootDir() + "\' doesn\'t have have enough permission for read/write");
            }
        } else {
            throw new RuntimeException("Root path \'" + this.config.getRootDir() + "\' doesn\'t exist");
        }
    }

    public boolean saveFile(FileStoreBean fileStoreBean) {
        String encoded_file = fileStoreBean.getFileContent();
        String base64Image;
        if(encoded_file.startsWith("data:image")) {
            base64Image = encoded_file.split(",")[1];
        } else {
            base64Image = encoded_file;
        }

        byte[] data = Base64.decodeBase64(base64Image.getBytes());
        File file = new File(this.config.getRootDir() + fileStoreBean.getFileName());

        try {
            Throwable e = null;
            Object var7 = null;

            try {
                FileOutputStream stream = new FileOutputStream(file);

                try {
                    stream.write(data);
                } finally {
                    if(stream != null) {
                        stream.close();
                    }

                }

                return true;
            } catch (Throwable var16) {
                if(e == null) {
                    e = var16;
                } else if(e != var16) {
                    e.addSuppressed(var16);
                }

                throw e;
            }
        } catch (IOException var17) {
            this.log.error("{}", var17);
            return false;
        }
    }

    public byte[] getFile(String fileName) {
        byte[] result = null;

        try {
            Path e = Paths.get(this.config.getRootDir() + fileName, new String[0]);
            if(!e.toFile().exists()) {
                this.log.info("File \'{}\' doesn\'t exists", fileName);
                return null;
            }

            result = Files.readAllBytes(e);
        } catch (IOException var4) {
            this.log.error("{}", var4);
        }

        return result;
    }
}
