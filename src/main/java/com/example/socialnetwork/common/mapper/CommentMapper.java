package com.example.socialnetwork.common.mapper;

import com.example.socialnetwork.application.response.CommentResponse;
import com.example.socialnetwork.domain.model.CommentDomain;
import com.example.socialnetwork.infrastructure.entity.Comment;
import com.example.socialnetwork.infrastructure.entity.CommentReaction;
import com.example.socialnetwork.infrastructure.entity.Post;
import com.example.socialnetwork.infrastructure.entity.PostReaction;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Mapper
public interface CommentMapper {
    CommentMapper INSTANCE = Mappers.getMapper(CommentMapper.class);

    @Mapping(source = "id", target = "commentId")
    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "post.id", target = "postId")
    @Mapping(source = "commentReactions", target = "reactionsId", qualifiedByName = "commentReactionsToIds")
    CommentDomain commentToCommentDomain(Comment comment);

    @org.mapstruct.Named("commentReactionsToIds")
    default List<Long> commentReactionsToIds(List<CommentReaction> commentReactions) {
        return commentReactions != null ? commentReactions.stream().map(CommentReaction::getId).collect(Collectors.toList()) : null;
    }

    @Mapping(source = "reactionsId", target = "reactCount", qualifiedByName = "commentReactionsIdsToNumber")
    CommentResponse commentDomainToCommentResponse(CommentDomain commentDomain);
    @org.mapstruct.Named("commentReactionsIdsToNumber")
    default Long commentReactionsIdsToNumber(List<Long> reactionsId) {
        return (long) reactionsId.size();
    }

    @Mapping(source = "commentId", target = "id")
    @Mapping(source = "userId", target = "user.id")
    @Mapping(source = "postId", target = "post.id")
    Comment commentDomainToComment(CommentDomain commentDomain);
}
