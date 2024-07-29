package com.example.socialnetwork.application.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostReactionRequest {
    private Long postId;
    private String reactionType;
}
