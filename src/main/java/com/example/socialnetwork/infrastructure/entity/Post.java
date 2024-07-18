package com.example.socialnetwork.infrastructure.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.LinkedHashSet;
import java.util.Set;

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
    private String visibility;

    @Column(name = "created_at")
    private Instant createdAt;

    @Column(name = "updated_at")
    private Instant updatedAt;

    @Column(name = "is_deleted")
    private Boolean isDeleted;

    @Size(max = 255)
    @Column(name = "photo_lists")
    private String photoLists;

    @OneToMany(mappedBy = "post")
    private Set<Comment> comments = new LinkedHashSet<>();

    @OneToMany(mappedBy = "post")
    private Set<PostReaction> postReactions = new LinkedHashSet<>();

    @OneToMany(mappedBy = "post")
    private Set<Tag> tags = new LinkedHashSet<>();

}