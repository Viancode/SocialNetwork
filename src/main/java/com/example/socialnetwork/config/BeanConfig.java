package com.example.socialnetwork.config;

import com.example.socialnetwork.common.mapper.*;
import com.example.socialnetwork.domain.port.api.*;
import com.example.socialnetwork.domain.port.spi.*;
import com.example.socialnetwork.domain.service.*;
import com.example.socialnetwork.infrastructure.adapter.*;
import com.example.socialnetwork.infrastructure.repository.*;
import org.springframework.beans.factory.annotation.Value;
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
    public UserServicePort userServicePort(EmailServicePort emailService, UserDatabasePort userDatabase, RelationshipServicePort relationshipService, S3ServicePort s3Service, StorageServicePort storageService) {
        return new UserServiceImpl(emailService, userDatabase, relationshipService, s3Service, storageService);
    }

    @Bean
    public UserDatabasePort userDatabasePort(UserRepository userRepository, PasswordEncoder encoder, UserMapper userMapper) {
        return new UserDatabaseAdapter(encoder,userRepository, userMapper);
    }

    @Bean
    RelationshipServicePort relationshipServicePort(RelationshipDatabasePort relationshipDatabasePort, UserDatabasePort userDatabasePort, UserMapper userMapper) {
        return new RelationshipServiceImpl(relationshipDatabasePort, userDatabasePort, userMapper);
    }

    @Bean
    RelationshipDatabasePort relationshipDatabasePort(RelationshipRepository relationshipRepository, RelationshipMapper relationshipMapper, UserRepository userRepository, UserMapper userMapper) {
        return new RelationshipDatabaseAdapter(relationshipRepository, relationshipMapper, userRepository, userMapper);
    }


    @Bean
    public PostDatabasePort postDatabasePort(PostRepository repository, RelationshipRepository relationshipRepository, PostMapper postMapper) {
        return new PostDatabaseAdapter(repository,relationshipRepository,postMapper);
    }

    @Bean
    public PostServicePort postServicePort(PostDatabasePort postDatabasePort, RelationshipServicePort relationshipService, PostMapper postMapper) {
        return new PostServiceImpl(postDatabasePort,relationshipService, postMapper);
    }

    @Bean
    public TagMapper tagMapper(UserRepository userRepository, PostRepository postRepository) {
        return  new TagMapper(userRepository,postRepository);
    }

    @Bean
    public TagDatabasePort tagDatabasePort(TagRepository repository, TagMapper tagMapper, PostRepository postRepository, RelationshipRepository relationshipRepository) {
        return new TagDatabaseAdapter(repository,tagMapper, postRepository, relationshipRepository);
    }

    @Bean
    public TagServicePort tagServicePort(TagDatabasePort tagDatabasePort) {
        return new  TagServiceImpl(tagDatabasePort);
    }

    @Bean
    PostReactionDatabasePort postReactionDatabasePort(PostReactionRepository postReactionRepository) {
        return new PostReactionDatabaseAdapter(postReactionRepository);
    }

    @Bean
    PostReactionServicePort postReactionServicePort(PostReactionDatabasePort postReactionDatabasePort, PostDatabasePort postDatabasePort, RelationshipDatabasePort relationshipDatabasePort){
        return new PostReactionServiceImpl(postReactionDatabasePort, postDatabasePort, relationshipDatabasePort);
    }

    @Bean
    public CommentDatabasePort commentDatabasePort(CommentRepository commentRepository, CommentMapper commentMapper) {
        return new CommentDatabaseAdapter(commentRepository, commentMapper);
    }

    @Bean
    public CommentServicePort commentServicePort(CommentDatabasePort commentDatabasePort, UserDatabasePort userDatabase, PostDatabasePort postDatabasePort, RelationshipServicePort relationshipServicePort, CommentMapper commentMapper) {
        return new CommentServiceImpl(commentDatabasePort, userDatabase, postDatabasePort, relationshipServicePort, commentMapper);
    }

    @Bean
    public CommentReactionDatabasePort commentReactionDatabasePort(CommentReactionRepository commentReactionRepository) {
        return new CommentReactionDatabaseAdapter(commentReactionRepository);
    }

    @Bean
    public CommentReactionServicePort commentReactionServicePort(CommentReactionDatabasePort commentReactionDatabasePort, RelationshipDatabasePort relationshipDatabasePort, CommentDatabasePort  commentDatabasePort, PostDatabasePort postDatabasePort){
        return new CommentReactionServiceImpl(commentReactionDatabasePort, relationshipDatabasePort, commentDatabasePort, postDatabasePort);
    }

}
