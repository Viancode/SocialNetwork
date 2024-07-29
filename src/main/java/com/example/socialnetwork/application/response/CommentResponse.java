package com.example.socialnetwork.application.response;

import lombok.Builder;
import lombok.Value;

import java.time.LocalDateTime;

@Value
@Builder
public class CommentResponse {
    Long commentId;
    Long userId;
    String username;
    String avatar;
    Long postId;
    Long parentComment;
    Long numberOfChild;
    String content;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
    Boolean isHidden;
    String image;
    Long reactCount;
}
