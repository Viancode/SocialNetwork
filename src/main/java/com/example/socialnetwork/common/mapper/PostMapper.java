package com.example.socialnetwork.common.mapper;

import com.example.socialnetwork.application.response.PostResponse;
import com.example.socialnetwork.domain.model.PostDomain;
import com.example.socialnetwork.infrastructure.entity.Comment;
import com.example.socialnetwork.infrastructure.entity.Post;
import com.example.socialnetwork.infrastructure.entity.PostReaction;
import com.example.socialnetwork.infrastructure.entity.Tag;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.stream.Collectors;

@Mapper
public interface PostMapper {
    PostMapper INSTANCE = Mappers.getMapper(PostMapper.class);


    @Mapping(source = "user.id", target = "userId")
    @Mapping(target = "postReactionsIds", source = "postReactions", qualifiedByName = "postReactionsToIds")
    @Mapping(target = "commentsIds", source = "comments", qualifiedByName = "commentsToIds")
    @Mapping(target = "tagsIds", source = "tags", qualifiedByName = "tagsToIds")
    PostDomain postToPostDomain(Post post);

    @Mapping(source = "userId", target = "user.id")
    @Mapping(target = "postReactions", ignore = true)
    @Mapping(target = "comments", ignore = true)
    @Mapping(target = "tags", ignore = true)
    Post postDomainToPost(PostDomain postDomain);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "userId", target = "userId")
    PostResponse postDomainToPostResponse(PostDomain postDomain);

    @org.mapstruct.Named("postReactionsToIds")
    default List<Long> postReactionsToIds(List<PostReaction> reactions) {
        return reactions != null ? reactions.stream().map(PostReaction::getId).collect(Collectors.toList()) : null;
    }

    @org.mapstruct.Named("commentsToIds")
    default List<Long> commentsToIds(List<Comment> comments) {
        return comments != null ? comments.stream().map(Comment::getId).collect(Collectors.toList()) : null;
    }

    @org.mapstruct.Named("tagsToIds")
    default List<Long> tagsToIds(List<Tag> tags) {
        return tags != null ? tags.stream().map(Tag::getId).collect(Collectors.toList()) : null;
    }
}
