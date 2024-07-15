package com.example.socialnetwork.config;

import com.example.socialnetwork.config.aws.S3Properties;
import com.example.socialnetwork.domain.service.S3ServiceImpl;
import com.example.socialnetwork.domain.service.StorageServiceImpl;
import com.example.socialnetwork.domain.port.api.S3ServicePort;
import com.example.socialnetwork.domain.port.api.StorageServicePort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.services.s3.S3Client;

@Configuration
public class BeanConfig {
    @Bean
    S3ServicePort s3Service(S3Client s3Client, S3Properties s3Properties) {
        return new S3ServiceImpl(s3Client, s3Properties);
    }

    @Bean
    StorageServicePort storageService(S3ServicePort s3Service) {
        return new StorageServiceImpl(s3Service);
    }
}
