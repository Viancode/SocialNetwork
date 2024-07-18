package com.example.socialnetwork.infrastructure.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", nullable = false)
    private Long id;

    @Size(max = 255)
    @Column(name = "username")
    private String username;

    @Size(max = 255)
    @Column(name = "email")
    private String email;

    @Size(max = 255)
    @Column(name = "password")
    private String password;

    @Size(max = 255)
    @Column(name = "first_name")
    private String firstName;

    @Size(max = 255)
    @Column(name = "last_name")
    private String lastName;

    @Lob
    @Column(name = "gender")
    private String gender;

    @Lob
    @Column(name = "visibility")
    private String visibility;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id")
    private Role role;

    @Size(max = 255)
    @Column(name = "bio")
    private String bio;

    @Size(max = 255)
    @Column(name = "location")
    private String location;

    @Size(max = 255)
    @Column(name = "work")
    private String work;

    @Size(max = 255)
    @Column(name = "education")
    private String education;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Size(max = 255)
    @Column(name = "avatar")
    private String avatar;

    @Size(max = 255)
    @Column(name = "background_image")
    private String backgroundImage;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @ColumnDefault("0")
    @Column(name = "is_email_verified")
    private Boolean isEmailVerified;

    @OneToMany(mappedBy = "user")
    private Set<ChatMember> chatMembers = new LinkedHashSet<>();

    @OneToMany(mappedBy = "user")
    private Set<CommentReaction> commentReactions = new LinkedHashSet<>();

    @OneToMany(mappedBy = "user")
    private Set<Comment> comments = new LinkedHashSet<>();

    @OneToMany(mappedBy = "sender")
    private Set<Message> messages = new LinkedHashSet<>();

    @OneToMany(mappedBy = "user")
    private Set<PostReaction> postReactions = new LinkedHashSet<>();

    @OneToMany(mappedBy = "user")
    private Set<Post> posts = new LinkedHashSet<>();

}