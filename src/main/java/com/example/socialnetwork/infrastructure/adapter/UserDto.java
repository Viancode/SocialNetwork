package com.example.socialnetwork.infrastructure.adapter;

import jakarta.validation.constraints.Size;
import lombok.Value;

import java.io.Serializable;
import java.time.Instant;

/**
 * DTO for {@link com.example.socialnetwork.infrastructure.entity.User}
 */
@Value
public class UserDto implements Serializable {
    @Size(max = 255)
    String username;
    @Size(max = 255)
    String email;
    @Size(max = 255)
    String firstName;
    @Size(max = 255)
    String lastName;
    String visibility;
    @Size(max = 255)
    String bio;
    @Size(max = 255)
    String location;
    @Size(max = 255)
    String work;
    @Size(max = 255)
    String education;
    Instant createdAt;
    Instant updatedAt;
    @Size(max = 255)
    String avatar;
    @Size(max = 255)
    String backgroundImage;
    Integer age;
}