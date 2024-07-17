package com.example.socialnetwork.domain.model;

import com.example.socialnetwork.infrastructure.entity.Comment;
import com.example.socialnetwork.infrastructure.entity.PostReaction;
import com.example.socialnetwork.infrastructure.entity.Tag;
import com.example.socialnetwork.infrastructure.entity.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostDomain {
    private Long userId;

    private String content;

    private String visibility;

    private LocalDate createdAt;

    private LocalDate updatedAt;

    private Boolean isDeleted;

    private String photoLists;

    private List<Long> commentsIds = new ArrayList<>();

    private List<Long> postReactionsIds = new ArrayList<>();

    private List<Long> tagsIds = new ArrayList<>();
}
