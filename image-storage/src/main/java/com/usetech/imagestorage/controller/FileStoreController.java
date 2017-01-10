package com.usetech.imagestorage.controller;

import com.usetech.imagestorage.bean.ErrorBean;
import com.usetech.imagestorage.bean.ImageStoreBean;
import com.usetech.imagestorage.bean.LogStoreBean;
import com.usetech.imagestorage.service.FileStoreService;
import com.usetech.imagestorage.service.LogStoreService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Created by User on 14.11.2016.
 */
@RestController
@RequestMapping(value = "/rest")
public class FileStoreController {

    private final static Logger log = LoggerFactory.getLogger(FileStoreController.class);

    @Autowired
    private FileStoreService fileStoreService;
    @Autowired
    private LogStoreService logStoreService;

    @RequestMapping(value = "/log", method = RequestMethod.PUT)
    public ResponseEntity storeLog(@RequestBody LogStoreBean logStoreBean) {
        if (logStoreService.saveFile(logStoreBean)) {
            return ResponseEntity.status(HttpStatus.OK).body(null);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
    }

    @RequestMapping(value = "/image", method = RequestMethod.PUT)
    public ResponseEntity storeImage(@RequestBody ImageStoreBean imageStoreBean) {
        if (fileStoreService.saveFile(imageStoreBean)) {
            return ResponseEntity.status(HttpStatus.OK).body(null);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
    }

    @RequestMapping(value = "/image/{fileName:.+}", method = RequestMethod.GET)
    public ResponseEntity get(@PathVariable("fileName") String fileName) {
        byte[] file = fileStoreService.getFile(fileName);
        if (file != null) {
            return ResponseEntity.ok()
                    .contentLength(file.length)
                    .contentType(fileName.endsWith("png") ? MediaType.IMAGE_PNG : MediaType.IMAGE_JPEG)
                    .body(file);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorBean("Image not found"));
    }
}