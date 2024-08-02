package com.example.socialnetwork.application.response;

import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentResponse {
    private Long commentId;
    private Long userId;
    private String username;
    private String avatar;
    private Long postId;
    private Long parentCommentId;
    private Long numberOfChild;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Boolean isHidden;
    private String image;
    private Long reactCount;
}
