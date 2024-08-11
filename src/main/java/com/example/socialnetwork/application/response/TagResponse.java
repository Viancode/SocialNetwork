package com.example.socialnetwork.application.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TagResponse {
    private Long id = null;

    private Long userIdTag;

    private String usernameTag;

    private Long postId;

    private Long userIdTagged;

    private String usernameTagged;

    private LocalDateTime createdAt;
}
