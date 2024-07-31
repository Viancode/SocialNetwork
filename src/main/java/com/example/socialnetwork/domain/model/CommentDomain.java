package com.example.socialnetwork.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentDomain {
    private Long id;
    private Long userId;
    private String content;
    private LocalDate createdAt;
    private LocalDate updatedAt;
    private Boolean isHidden;
    private Long postId;
    private List<Long> reactionsId;
}
