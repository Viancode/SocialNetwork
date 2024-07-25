package com.example.socialnetwork.infrastructure.entity;

import com.example.socialnetwork.common.constant.Visibility;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "posts")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Size(max = 255)
    @Column(name = "content")
    private String content;

    @Lob
    @Column(name = "visibility")
    @Enumerated(EnumType.STRING)
    private Visibility visibility;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "photo_lists", columnDefinition = "MEDIUMTEXT")
    private String photoLists;

    @JsonIgnore
    @OneToMany(fetch = FetchType.EAGER,mappedBy = "post", cascade = CascadeType.ALL)
    private List<Comment> comments = new ArrayList<>();

    @JsonIgnore
    @OneToMany(fetch = FetchType.EAGER,mappedBy = "post", cascade = CascadeType.ALL)
    private List<PostReaction> postReactions = new ArrayList<>();

    @JsonIgnore
    @OneToMany(fetch = FetchType.EAGER,mappedBy = "post", cascade = CascadeType.ALL)
    private List<Tag> tags = new ArrayList<>();

}