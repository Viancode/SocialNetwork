package com.example.socialnetwork.domain.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentReactionDomain {
    private Long id;
    private Long userId;
    private Long commentId;
    private String reactionType;
    private LocalDate createdAt;

}
