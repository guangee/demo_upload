package com.coding.upload.controller;

import io.minio.MinioClient;
import io.minio.errors.InvalidEndpointException;
import io.minio.errors.InvalidPortException;
import io.minio.errors.MinioException;
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
@RequestMapping("minio")
@RestController
public class MinioController {
    @PostMapping("files")
    public String upload(MultipartFile file) throws Exception {
        log.info("getOriginalFilename:{}", file.getOriginalFilename());
        log.info("getSize:{}", file.getSize());
        String name = UUID.randomUUID().toString() + "-" + file.getOriginalFilename();
//        File outFile = new File(baseDir + name);
//        FileUtils.copyToFile(file.getInputStream(), outFile);
//
        // 使用MinIO服务的URL，端口，Access key和Secret key创建一个MinioClient对象
        MinioClient minioClient = new MinioClient("http://10.211.55.35:9000", "admin", "admin123");

        // 检查存储桶是否已经存在
        boolean isExist = minioClient.bucketExists("files");
        if (isExist) {
            log.info("Bucket already exists.");
        } else {
            // 创建一个名为asiatrip的存储桶，用于存储照片的zip文件。
            minioClient.makeBucket("files");
        }

        // 使用putObject上传一个文件到存储桶中。
        minioClient.putObject("files", name, file.getInputStream(), file.getContentType());

        return name;
    }


}
