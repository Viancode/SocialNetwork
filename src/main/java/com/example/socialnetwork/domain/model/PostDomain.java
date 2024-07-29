package com.example.socialnetwork.domain.model;


import com.example.socialnetwork.common.constant.Visibility;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostDomain {
    private Long id;

    private Long userId;

    private String content;

    private Visibility visibility;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private String photoLists;

    private List<Long> commentsIds = new ArrayList<>();

    private List<Long> postReactionsIds = new ArrayList<>();

    private List<Long> tagsIds = new ArrayList<>();
}