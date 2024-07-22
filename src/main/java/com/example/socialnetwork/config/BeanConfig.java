package com.example.socialnetwork.config;

import com.example.socialnetwork.common.mapper.RelationshipMapper;
import com.example.socialnetwork.common.mapper.UserMapper;
import com.example.socialnetwork.config.aws.S3Properties;
import com.example.socialnetwork.domain.port.api.*;
import com.example.socialnetwork.domain.port.spi.RelationshipDatabasePort;
import com.example.socialnetwork.domain.port.spi.UserDatabasePort;
import com.example.socialnetwork.domain.service.*;
import com.example.socialnetwork.infrastructure.adapter.RelationshipDatabaseAdapter;
import com.example.socialnetwork.infrastructure.adapter.UserDatabaseAdapter;
import com.example.socialnetwork.infrastructure.repository.RelationshipRepository;
import com.example.socialnetwork.infrastructure.repository.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.thymeleaf.TemplateEngine;
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

    @Bean
    public TokenServicePort tokenServicePort(TokenProperties tokenProperties, RedisTemplate<String, String> redisTemplate) {
        return new TokenServiceImpl(tokenProperties, redisTemplate);
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
    public UserDatabasePort userDatabasePort(UserRepository userRepository, PasswordEncoder encoder, UserMapper userMapper) {
        return new UserDatabaseAdapter(encoder,userRepository, userMapper);
    }

    @Bean
    RelationshipServicePort relationshipServicePort(RelationshipDatabasePort relationshipDatabasePort, UserMapper userMapper) {
        return new RelationshipServiceImpl(relationshipDatabasePort, userMapper);
    }

    @Bean
    RelationshipDatabasePort relationshipDatabasePort(RelationshipRepository relationshipRepository, RelationshipMapper relationshipMapper, UserRepository userRepository, UserMapper userMapper) {
        return new RelationshipDatabaseAdapter(relationshipRepository, relationshipMapper, userRepository, userMapper);
    }
}
