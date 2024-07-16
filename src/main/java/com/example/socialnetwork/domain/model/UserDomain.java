package com.example.socialnetwork.domain.model;

import com.example.socialnetwork.common.constant.Role;
import com.example.socialnetwork.common.constant.Visibility;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import software.amazon.awssdk.services.polly.model.Gender;


import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserDomain {
    String username;
    String password;
    String email;
    String firstName;
    String lastName;
    String bio;
    Gender gender;
    LocalDate dateOfBirth;
    Role role;
    String location;
    String work;
    String education;
    String avatar;
    String backgroundImage;
    String refreshToken;
    Visibility visibility;
    int token;
    boolean enabled;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
    boolean isEmailVerified;
}
