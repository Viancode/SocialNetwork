package com.example.socialnetwork.domain.model;

import com.example.socialnetwork.common.constant.ERole;
import com.example.socialnetwork.common.constant.Gender;
import com.example.socialnetwork.common.constant.Visibility;
import com.example.socialnetwork.infrastructure.entity.Role;
import lombok.*;
import lombok.experimental.FieldDefaults;


import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserDomain {
    long id;
    String username;
    String password;
    String email;
    String firstName;
    String lastName;
    String bio;
    Gender gender;
    LocalDate dateOfBirth;
    ERole Erole;
    String location;
    String work;
    String education;
    String avatar;
    String backgroundImage;
    Visibility visibility;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
    Boolean isEmailVerified;

}
