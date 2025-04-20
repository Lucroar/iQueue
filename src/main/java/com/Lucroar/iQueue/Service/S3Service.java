package com.Lucroar.iQueue.Service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.net.URLConnection;

@Service
public class S3Service {
    private final S3Client s3Client;

    public S3Service(@Value("${AWS_ACCESS_KEY_ID}") String accessKey,
                     @Value("${AWS_SECRET_ACCESS_KEY}") String privateKey) {
        this.s3Client = S3Client.builder()
                .region(Region.AP_SOUTHEAST_2)
                .credentialsProvider(StaticCredentialsProvider.create(AwsBasicCredentials.create(accessKey, privateKey)))
                .build();
    }

    public String uploadFile(MultipartFile file) throws IOException {
        String key = "uploads/" + file.getOriginalFilename();

        String contentType = URLConnection.guessContentTypeFromName(file.getOriginalFilename());

        String bucketName = "iqueue-bucket";
        PutObjectRequest objectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .contentType(contentType)
                .build();

        s3Client.putObject(objectRequest, RequestBody.fromInputStream(file.getInputStream(), file.getSize()));

        return "https://" + bucketName + ".s3.amazonaws.com/" + key;
    }
}
