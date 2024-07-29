package com.example.socialnetwork.common.mapper;

import com.example.socialnetwork.application.request.PostReactionRequest;
import com.example.socialnetwork.application.response.PostReactionResponse;
import com.example.socialnetwork.common.util.SecurityUtil;
import com.example.socialnetwork.domain.model.PostReactionDomain;
import com.example.socialnetwork.infrastructure.entity.PostReaction;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.time.LocalDateTime;
@Mapper
public interface PostReactionMapper {
    PostReactionMapper INSTANCE = Mappers.getMapper(PostReactionMapper.class);

    /**
     *
     * @param postReactionRequest
     *     private Long postId;
     *     private String reactionType;
     * @return PostReactionDomain
     *     private Long id = null;
     *     private Long userId;
     *     private Long postId;
     *     private String reactionType;
     *     private LocalDateTime createdAt;
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "userId", expression = "java(getUserId())")
    @Mapping(target = "createdAt", expression = "java(getCreateAt())")
    PostReactionDomain requestToDomain(PostReactionRequest postReactionRequest);


    @org.mapstruct.Named("getUserId")
    default Long getUserId() {
        return SecurityUtil.getCurrentUserId();
    }
    @org.mapstruct.Named("getCreateAt")
    default LocalDateTime getCreateAt() {
        return LocalDateTime.now();
    }

    /**
     *
     * @param postReactionDomain
     *     private Long id = null;
     *     private Long userId;
     *     private Long postId;
     *     private String reactionType;
     *     private LocalDateTime createdAt;
     * @return PostReaction
     *      private Long id;
     *      private User user;
     *      private Post post;
     *      private String reactionType;
     *      private LocalDateTime createdAt;
     */
    @Mapping(source = "userId", target = "user.id")
    @Mapping(source = "postId", target = "post.id")
    PostReaction domainToEntity(PostReactionDomain postReactionDomain);

    /**
     *
     * @param postReaction
     *      private Long id;
     *      private User user;
     *      private Post post;
     *      private String reactionType;
     *      private LocalDateTime createdAt;
     * @return PostReactionDomain
     *     private Long id = null;
     *     private Long userId;
     *     private Long postId;
     *     private String reactionType;
     *     private LocalDateTime createdAt;
     */
    @Mapping(source = "post.id", target = "postId")
    @Mapping(source = "user.id", target = "userId")
    PostReactionDomain entityToDomain(PostReaction postReaction);

    /**
     *
      * @param postReactionDomain
     *     private Long id = null;
     *     private Long userId;
     *     private Long postId;
     *     private String reactionType;
     *     private LocalDateTime createdAt;
     * @return PostReactionResponse
     *     private Long id;
     *     private Long userId;
     *     private Long postId;
     *     private String reactionType;
     *     private LocalDate createdAt;
     */
    PostReactionResponse domainToResponse(PostReactionDomain postReactionDomain);

}
