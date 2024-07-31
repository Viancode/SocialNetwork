package com.example.socialnetwork.application.request;

import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CommentRequest {
    private Long postId;
    @Builder.Default
    private Long parentComment = null;
    private String content;
    @Builder.Default
    private String image = null;
}
