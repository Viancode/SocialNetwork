package com.example.socialnetwork.application.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostResponse {
    private Long id;

    private Long userId;

    private String username;

    private String avatar;

    private String content;

    private String visibility;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private List<String> photoLists;

    private  Long numberOfComments;

    private Long numberOfReacts;

    private Long numberTags;
}