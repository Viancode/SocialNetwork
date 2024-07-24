package com.example.socialnetwork.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TagDomain {
    private Long id = null;

    private Long taggedUserId;

    private Long postId;

    private Long taggedByUserId;
}
