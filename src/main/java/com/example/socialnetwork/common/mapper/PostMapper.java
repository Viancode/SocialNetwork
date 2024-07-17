package com.example.socialnetwork.common.mapper;


import com.example.socialnetwork.application.request.PostRequest;
import com.example.socialnetwork.application.response.PostResponse;
import com.example.socialnetwork.domain.model.PostDomain;
import com.example.socialnetwork.infrastructure.entity.Comment;
import com.example.socialnetwork.infrastructure.entity.Post;
import com.example.socialnetwork.infrastructure.entity.PostReaction;
import com.example.socialnetwork.infrastructure.entity.Tag;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.stream.Collectors;

@Mapper
public interface PostMapper {
    PostMapper INSTANCE = Mappers.getMapper(PostMapper.class);

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "createdAt", target = "createdAt")
    @Mapping(source = "updatedAt", target = "updatedAt")
    @Mapping(source = "photoLists", target = "photoLists")
    @Mapping(target = "postReactionsIds", expression = "java(convertPostReactionsToIds(post.getPostReactions()))")
    @Mapping(target = "commentsIds", expression = "java(convertCommentsToIds(post.getComments()))")
    @Mapping(target = "tagsIds", expression = "java(convertTagsToIds(post.getTags()))")
    PostDomain postToPostDomain(Post post);

    @Mapping(source = "userId", target = "user.id")
    @Mapping(source = "createdAt", target = "createdAt")
    @Mapping(source = "updatedAt", target = "updatedAt")
    @Mapping(source = "photoLists", target = "photoLists")
    @Mapping(target = "postReactions", ignore = true) // MapStruct không tự động hỗ trợ chuyển đổi danh sách ID sang đối tượng
    @Mapping(target = "comments", ignore = true)  // MapStruct không tự động hỗ trợ chuyển đổi danh sách ID sang đối tượng
    @Mapping(target = "tags", ignore = true)      // MapStruct không tự động hỗ trợ chuyển đổi danh sách ID sang đối tượng
    Post postDomainToPost(PostDomain postDomain);

    @Mapping(source = "userId", target = "userId")
    @Mapping(source = "content", target = "content")
    @Mapping(source = "visibility", target = "visibility")
    @Mapping(source = "photoLists", target = "photoLists")
    PostDomain postRequestToPostDomain(PostRequest postRequest);

    @Mapping(source = "userId", target = "userId")
    @Mapping(source = "content", target = "content")
    @Mapping(source = "visibility", target = "visibility")
    @Mapping(source = "createdAt", target = "createdAt")
    @Mapping(source = "updatedAt", target = "updatedAt")
    @Mapping(source = "photoLists", target = "photoLists")
    @Mapping(source = "postReactionsIds", target = "postReactionsIds")
    @Mapping(source = "commentsIds", target = "commentsIds")
    @Mapping(source = "tagsIds", target = "tagsIds")
    PostResponse postDomainToPostResponse(PostDomain postDomain);

//    default LocalDateTime convertToLocalDateTime(Instant instant) {
//        return instant != null ? instant.atZone(ZoneId.systemDefault()).toLocalDateTime() : null;
//    }
//
//    default Instant convertToInstant(LocalDateTime localDateTime) {
//        return localDateTime != null ? localDateTime.atZone(ZoneId.systemDefault()).toInstant() : null;
//    }

    default List<Long> convertPostReactionsToIds(List<PostReaction> reactions) {
        return reactions != null ? reactions.stream().map(PostReaction::getId).collect(Collectors.toList()) : null;
    }

    default List<Long> convertCommentsToIds(List<Comment> comments) {
        return comments != null ? comments.stream().map(Comment::getId).collect(Collectors.toList()) : null;
    }

    default List<Long> convertTagsToIds(List<Tag> tags) {
        return tags != null ? tags.stream().map(Tag::getId).collect(Collectors.toList()) : null;
    }
}
