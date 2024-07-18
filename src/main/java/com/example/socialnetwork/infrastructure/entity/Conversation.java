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
@Table(name = "conversations")
public class Conversation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "conversation_id", nullable = false)
    private Long id;

    @Size(max = 255)
    @Column(name = "conversation_name")
    private String conversationName;

    @Column(name = "is_group")
    private Boolean isGroup;

    @Column(name = "created_at")
    private Instant createdAt;

    @Column(name = "is_deleted")
    private Boolean isDeleted;

    @OneToMany(mappedBy = "conversation")
    private Set<ChatMember> chatMembers = new LinkedHashSet<>();

    @OneToMany(mappedBy = "conversation")
    private Set<Message> messages = new LinkedHashSet<>();

}