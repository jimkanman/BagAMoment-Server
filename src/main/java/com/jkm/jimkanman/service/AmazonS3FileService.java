package com.jkm.jimkanman.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Objects;
import java.util.UUID;

@Service
public class AmazonS3FileService implements FileService{
    private final AmazonS3 amazonS3;
    private final String bucketName;

    public AmazonS3FileService(
            final AmazonS3 amazonS3,
            @Value("${cloud.aws.s3.bucket}")final String bucketName
    ) {
        this.amazonS3 = amazonS3;
        this.bucketName = bucketName;
    }

    @Override
    public String saveFile(MultipartFile file) {
        if(file == null) return null;
        if(file.getOriginalFilename() == null) throw new RuntimeException("파일 이름이 비어 있습니다.");

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(file.getContentType());
        metadata.setContentLength(file.getSize());

        String originalFilename = file.getOriginalFilename();
        String filename;
        try {
            int extIdx = Objects.requireNonNull(file.getOriginalFilename()).lastIndexOf(".");
            String filenameWithoutExt = originalFilename.substring(0, extIdx);
            String ext = originalFilename.substring(extIdx);
            String uuid = UUID.randomUUID().toString();
            filename = filenameWithoutExt + "_" + uuid + ext;

            amazonS3.putObject(bucketName, filename, file.getInputStream(), metadata);
        } catch (IOException e) {
            System.out.println("AmazonS3FileService: IOException on saveFile() -> " + e.getMessage());
            return null;
        } catch (Exception e) {
            System.out.println("AmazonS3FileService: exception on saveFile() -> " + e.getMessage());
            return null;
        }
        return amazonS3.getUrl(bucketName, filename).toString();
    }

    @Override
    public void deleteFile(String filePath) {
        if (StringUtils.isBlank(filePath)) return;

        try {
            amazonS3.deleteObject(bucketName, filePath);
        } catch (Exception e) {
            System.out.println("AmazonS3FileService: IOException on deleteFile() -> " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public String getFile(String filename) {
        if (StringUtils.isBlank(filename)) return null;
        return amazonS3.getUrl(bucketName, filename).toString();
    }
}
