package com.usetech.imagestorage.service;

import com.usetech.imagestorage.bean.FileStoreBean;
import com.usetech.imagestorage.config.CommonConfig;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created by User on 14.11.2016.
 */
@Service
public class FileStoreServiceImpl implements FileStoreService {

    private final Logger log = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private CommonConfig config;

    @PostConstruct
    private void validate() {
        Path path = Paths.get(config.getRootDir());
        File rootPath = path.toFile();
        if (!(rootPath.exists() && rootPath.isDirectory())) {
            throw new RuntimeException("Root path '" + config.getRootDir() + "' doesn't exist");
        }
        if (!(rootPath.canRead() && rootPath.canWrite())) {
            throw new RuntimeException("Root path '" + config.getRootDir() + "' doesn't have have enough permission for read/write");
        }
        log.info("Root path: '{}' is valid", rootPath.getAbsolutePath());
    }

    @Override
    public boolean saveFile(FileStoreBean fileStoreBean) {
    	String base64Image;
    	String encoded_file = fileStoreBean.getFileContent(); 
    	if(encoded_file.startsWith("data:image"))
          base64Image = encoded_file.split(",")[1];
    	else
    		base64Image = encoded_file;

        byte[] data = Base64.decodeBase64(base64Image.getBytes());
        File file = new File(config.getRootDir() + fileStoreBean.getFileName());
        try (OutputStream stream = new FileOutputStream(file)) {
            stream.write(data);
        } catch (IOException e) {
            log.error("{}", e);
            return false;
        }
        return true;
    }

    @Override
    public byte[] getFile(String fileName) {
        byte[] result = null;
        try {
            Path path = Paths.get(config.getRootDir() + fileName);
            if (!path.toFile().exists()) {
                log.info("File '{}' doesn't exists", fileName);
                return null;
            }
            result = Files.readAllBytes(path);
        } catch (IOException e) {
            log.error("{}", e);
        }
        return result;
    }
}
