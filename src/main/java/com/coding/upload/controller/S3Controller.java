package com.coding.upload.controller;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import io.minio.MinioClient;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.UUID;

/**
 * @author guanweiming
 */
@Slf4j
@AllArgsConstructor
@RequestMapping("s3")
@RestController
public class S3Controller {

    private final AmazonS3 s3;

    @PostMapping("files")
    public String upload(MultipartFile file) throws Exception {
        String name = UUID.randomUUID().toString() + "-" + file.getOriginalFilename();
        String bucketName = "s3files";
        // 检查存储桶是否已经存在
        boolean isExist = s3.doesBucketExistV2(bucketName);
        if (isExist) {
            log.info("Bucket already exists.");
        } else {
            // 创建一个存储桶，用于存储文件。
            s3.createBucket(bucketName);
        }

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(file.getContentType());
        s3.putObject(bucketName, name, file.getInputStream(), metadata);
        return name;
    }


}
