package com.example.socialnetwork.config;

import com.example.socialnetwork.common.mapper.*;
import com.example.socialnetwork.domain.publisher.CustomEventPublisher;
import com.example.socialnetwork.domain.port.api.*;
import com.example.socialnetwork.domain.port.spi.*;
import com.example.socialnetwork.domain.service.HideCommentServiceImpl;
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
    public AuthServicePort authServicePort(JwtServicePort jwtService, TokenServicePort tokenService, UserRepository userRepository, UserServicePort userService, UserDatabasePort userDatabase, AuthenticationManager authenticationManager, CustomEventPublisher customEventPublisher) {
        return new AuthServiceImpl(jwtService, tokenService, userRepository, userService, userDatabase, authenticationManager, customEventPublisher);
    }

    @Bean
    public EmailServicePort emailServicePort(JavaMailSender emailSender, TemplateEngine templateEngine) {
        return new EmailServiceImpl(emailSender, templateEngine);
    }

    @Bean
    public UserServicePort userServicePort(EmailServicePort emailService, UserDatabasePort userDatabase, RelationshipDatabasePort relationshipDatabasePort, S3ServicePort s3Service, StorageServicePort storageService, CustomEventPublisher customEventPublisher) {
        return new UserServiceImpl(emailService, userDatabase, relationshipDatabasePort, s3Service, storageService, customEventPublisher);
    }

    @Bean
    public UserDatabasePort userDatabasePort(UserRepository userRepository, PasswordEncoder encoder, UserMapper userMapper) {
        return new UserDatabaseAdapter(encoder,userRepository, userMapper);
    }

    @Bean
    RelationshipServicePort relationshipServicePort(RelationshipDatabasePort relationshipDatabasePort, UserDatabasePort userDatabasePort, CloseRelationshipDatabasePort closeRelationshipDatabasePort,CustomEventPublisher customEventPublisher, SuggestionMapper suggestionMapper) {
        return new RelationshipServiceImpl(relationshipDatabasePort, userDatabasePort, closeRelationshipDatabasePort, customEventPublisher, suggestionMapper);
    }

    @Bean
    RelationshipDatabasePort relationshipDatabasePort(RelationshipRepository relationshipRepository, RelationshipMapper relationshipMapper, UserRepository userRepository, UserMapper userMapper, SuggestionRepository suggestionRepository, SuggestionMapper suggestionMapper) {
        return new RelationshipDatabaseAdapter(relationshipRepository, relationshipMapper, userRepository, userMapper, suggestionRepository, suggestionMapper);
    }


    @Bean
    public PostDatabasePort postDatabasePort(PostRepository repository, RelationshipRepository relationshipRepository, PostMapper postMapper, UserMapper userMapper) {
        return new PostDatabaseAdapter(repository,relationshipRepository,postMapper, userMapper);
    }

    @Bean
    public PostServicePort postServicePort(PostDatabasePort postDatabasePort, RelationshipDatabasePort relationshipDatabasePort, CloseRelationshipDatabasePort closeRelationshipDatabasePort, UserDatabasePort userDatabasePort, PostMapper postMapper) {
        return new PostServiceImpl(postDatabasePort,relationshipDatabasePort, closeRelationshipDatabasePort, userDatabasePort, postMapper);
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
    public CommentServicePort commentServicePort(CommentDatabasePort commentDatabasePort, UserDatabasePort userDatabase, PostDatabasePort postDatabasePort, RelationshipDatabasePort relationshipDatabasePort, CommentMapper commentMapper) {
        return new CommentServiceImpl(commentDatabasePort, userDatabase, postDatabasePort, relationshipDatabasePort, commentMapper);
    }

    @Bean
    public CommentReactionDatabasePort commentReactionDatabasePort(CommentReactionRepository commentReactionRepository) {
        return new CommentReactionDatabaseAdapter(commentReactionRepository);
    }

    @Bean
    public CommentReactionServicePort commentReactionServicePort(CommentReactionDatabasePort commentReactionDatabasePort, RelationshipDatabasePort relationshipDatabasePort, CommentDatabasePort  commentDatabasePort, PostDatabasePort postDatabasePort){
        return new CommentReactionServiceImpl(commentReactionDatabasePort, relationshipDatabasePort, commentDatabasePort, postDatabasePort);
    }

    @Bean
    public CloseRelationshipDatabasePort closeRelationshipDatabasePort(CloseRelationshipRepository closeRelationshipRepository, UserMapper userMapper){
        return new  CloseRelationshipDatabaseAdapter(closeRelationshipRepository, userMapper);
    }

    @Bean
    public CloseRelationshipServicePort closeRelationshipServicePort(CloseRelationshipDatabasePort closeRelationshipDatabasePort, RelationshipDatabasePort relationshipDatabasePort){
        return new CloseRelationshipServiceImpl(closeRelationshipDatabasePort, relationshipDatabasePort);
    }

    @Bean
    public HideCommentServiceImpl hideCommentSchedule(CommentDatabasePort commentDatabasePort) {
        return new HideCommentServiceImpl(commentDatabasePort);
    }

}
