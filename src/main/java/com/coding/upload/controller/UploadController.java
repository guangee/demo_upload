package com.coding.upload.controller;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.UUID;

/**
 * @author guanweiming
 */
@Slf4j
@RequestMapping("upload")
@RestController
public class UploadController {

    private static String baseDir = "files/";


    @PostMapping("files")
    public String upload(MultipartFile file) throws IOException {
        log.info("getOriginalFilename:{}", file.getOriginalFilename());
        log.info("getSize:{}", file.getSize());
        String name = UUID.randomUUID().toString() + "-" + file.getOriginalFilename();
        File outFile = new File(baseDir + name);
        FileUtils.copyToFile(file.getInputStream(), outFile);
        return name;
    }

    @GetMapping("download")
    public void download(String name, HttpServletResponse response) throws IOException {
        File file = new File(baseDir + name);
        IOUtils.copy(FileUtils.openInputStream(file), response.getOutputStream());
    }

    @GetMapping("show/{name:.+}")
    public void show(@PathVariable("name") String name, HttpServletResponse response) throws IOException {
        File file = new File(baseDir + name);
        IOUtils.copy(FileUtils.openInputStream(file), response.getOutputStream());
    }

}
