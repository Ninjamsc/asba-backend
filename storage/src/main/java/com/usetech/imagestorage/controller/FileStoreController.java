//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.usetech.imagestorage.controller;

import com.usetech.imagestorage.bean.ErrorBean;
import com.usetech.imagestorage.bean.FileStoreBean;
import com.usetech.imagestorage.service.FileStoreService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping({"/rest"})
public class FileStoreController {
    private static final Logger log = LoggerFactory.getLogger(FileStoreController.class);
    @Autowired
    private FileStoreService fileStoreService;

    public FileStoreController() {
    }

    @RequestMapping(
            value = {"/image"},
            method = {RequestMethod.PUT}
    )
    public ResponseEntity store(@RequestBody FileStoreBean fileStoreBean) {
        return this.fileStoreService.saveFile(fileStoreBean)?ResponseEntity.status(HttpStatus.OK).body((Object)null):ResponseEntity.status(HttpStatus.BAD_REQUEST).body((Object)null);
    }

    @RequestMapping(
            value = {"/image/{fileName:.+}"},
            method = {RequestMethod.GET}
    )
    public ResponseEntity get(@PathVariable("fileName") String fileName) {
        byte[] file = this.fileStoreService.getFile(fileName);
        return file != null?ResponseEntity.ok().contentLength((long)file.length).contentType(fileName.endsWith("png")?MediaType.IMAGE_PNG:MediaType.IMAGE_JPEG).body(file):ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorBean("Image not found"));
    }
}
