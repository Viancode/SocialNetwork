package com.example.socialnetwork.config;

import com.example.socialnetwork.common.mapper.TagMapper;
import com.example.socialnetwork.config.aws.S3Properties;
import com.example.socialnetwork.domain.port.api.*;
import com.example.socialnetwork.domain.port.spi.PostDatabasePort;
import com.example.socialnetwork.domain.port.spi.TagDatabasePort;
import com.example.socialnetwork.domain.port.spi.UserDatabasePort;
import com.example.socialnetwork.domain.service.*;
import com.example.socialnetwork.infrastructure.adapter.PostDatabaseAdapter;
import com.example.socialnetwork.infrastructure.adapter.TagDatabaseAdapter;
import com.example.socialnetwork.infrastructure.adapter.UserDatabaseAdapter;
import com.example.socialnetwork.infrastructure.repository.PostRepository;
import com.example.socialnetwork.infrastructure.repository.TagRepository;
import com.example.socialnetwork.infrastructure.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.thymeleaf.TemplateEngine;
import software.amazon.awssdk.services.docdbelastic.model.Auth;
import software.amazon.awssdk.services.s3.S3Client;

@Configuration
public class BeanConfig {

    @Value("${AWS_BUCKET_NAME}")
    private String bucketName;


    @Bean
    public S3ServicePort s3Service(S3Client s3Client) {
        return new S3ServiceImpl(s3Client, bucketName);
    }

    @Bean
    public StorageServicePort storageService(S3ServicePort s3Service) {
        return new StorageServiceImpl(s3Service);
    }

    @Bean
    public TokenServicePort tokenServicePort(RedisTemplate<String, String> redisTemplate) {
        return new TokenServiceImpl(redisTemplate);
    }

    @Bean
    public JwtServicePort jwtServicePort(TokenProperties tokenProperties) {
        return new JwtServiceImpl(tokenProperties);
    }

    @Bean
    public AuthServicePort authServicePort(JwtServicePort jwtService, TokenServicePort tokenService, UserRepository userRepository, UserServicePort userService, UserDatabasePort userDatabase, AuthenticationManager authenticationManager) {
        return new AuthServiceImpl(jwtService, tokenService, userRepository, userService, userDatabase, authenticationManager);
    }

    @Bean
    public EmailServicePort emailServicePort(JavaMailSender emailSender, TemplateEngine templateEngine) {
        return new EmailServiceImpl(emailSender, templateEngine);
    }

    @Bean
    public UserServicePort userServicePort(UserRepository userRepository, EmailServicePort emailService, TokenServicePort tokenService) {
        return new UserServiceImpl(userRepository, emailService, tokenService);
    }


    @Bean
    public UserDatabasePort userDatabasePort(UserRepository userRepository, PasswordEncoder encoder) {
        return new UserDatabaseAdapter(encoder,userRepository);
    }


    @Bean
    public PostDatabasePort postDatabasePort(PostRepository repository) {
        return new PostDatabaseAdapter(repository);
    }

    @Bean
    public PostServicePort postServicePort(PostDatabasePort postDatabasePort, S3ServicePort s3Service) {
        return new PortServiceImpl(postDatabasePort,s3Service);
    }

    @Bean
    public TagMapper tagMapper(UserRepository userRepository, PostRepository postRepository) {
        return  new TagMapper(userRepository,postRepository);
    }

    @Bean
    public TagDatabasePort tagDatabasePort(TagRepository repository, TagMapper tagMapper) {
        return new TagDatabaseAdapter(repository,tagMapper);
    }

    @Bean
    public TagServicePort tagServicePort(TagDatabasePort tagDatabasePort) {
        return new  TagServiceImpl(tagDatabasePort);
    }
}
