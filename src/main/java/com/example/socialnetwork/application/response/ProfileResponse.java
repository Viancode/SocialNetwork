package com.example.socialnetwork.application.response;

import com.example.socialnetwork.common.constant.Gender;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
public class ProfileResponse {
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private Gender gender;
    private String visibility;
    private String bio;
    private String location;
    private String work;
    private String education;
    private LocalDateTime createdAt;
    private String avatar;
    private String backgroundImage;
    private LocalDate dateOfBirth;
}