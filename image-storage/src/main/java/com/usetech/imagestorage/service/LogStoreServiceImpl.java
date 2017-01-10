package com.usetech.imagestorage.service;

import com.usetech.imagestorage.bean.LogStoreBean;
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
public class LogStoreServiceImpl implements LogStoreService {

    private final Logger log = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private CommonConfig config;

    @PostConstruct
    private void validate() {
        Path path = Paths.get(config.getLogRootDir());
        File rootPath = path.toFile();
        if (!(rootPath.exists() && rootPath.isDirectory())) {
            throw new RuntimeException("Root path '" + config.getImageRootDir() + "' doesn't exist");
        }
        if (!(rootPath.canRead() && rootPath.canWrite())) {
            throw new RuntimeException("Root path '" + config.getImageRootDir() + "' doesn't have have enough permission for read/write");
        }
        log.info("Root path: '{}' is valid", rootPath.getAbsolutePath());
    }

    @Override
    public boolean saveFile(LogStoreBean logStoreBean) {
    	String base64Image;
    	String encoded_file = logStoreBean.getFileContent();
    	String username = logStoreBean.getUsername();
        String workstation = logStoreBean.getWorkstation();
        String timestamp = logStoreBean.getTimestamp();
        Long wfmId = logStoreBean.getWfmId();
    	if(username.contains("\\"))
            username= username.replace("\\","-");
        if(workstation.contains("\\"))
            workstation = workstation.replace("\\","-");
        StringBuilder fullpath = new StringBuilder();
        fullpath.append(config.getLogRootDir()).append("/").append(timestamp).append(".").append(wfmId).append(".").append(username).append(".").append(workstation).append("/");
        File dir = new File(fullpath.toString());
    	if(encoded_file.startsWith("data:image"))
          base64Image = encoded_file.split(",")[1];
    	else
    		base64Image = encoded_file;
        log.debug("Storing file to"+fullpath.toString() + logStoreBean.getFileName());
        byte[] data = Base64.decodeBase64(base64Image.getBytes());
        File file = new File(fullpath.toString() + logStoreBean.getFileName());
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
            Path path = Paths.get(config.getImageRootDir() + fileName);
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
