package com.example.socialnetwork.infrastructure.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "comment_reactions")
public class CommentReaction {
    @EmbeddedId
    private CommentReactionId id;

    @MapsId("userId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Lob
    @Column(name = "reaction_type")
    private String reactionType;

    @Column(name = "created_at")
    private Instant createdAt;

}