package com.example.socialnetwork.application.response;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class CommentResponse {
    Long commentId;
    Long userId;
    Long postId;
    Long parentComment;
    String content;
    String createdAt;
    String updatedAt;
    Boolean isHidden;
    String image;
    Long reactCount;
}
