package com.example.socialnetwork.application.request;

import com.example.socialnetwork.common.ValidationRegex;
import com.example.socialnetwork.common.constant.Gender;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {
    @NotNull(message = "Email cannot be null")
    @Email(message = "Email should be valid", regexp = ValidationRegex.EMAIL_REGEX)
    @NotBlank(message = "Email cannot be blank")
    private String email;
    @Size(min = 1, max = 100, message = "First name must be between 1 and 100 characters")
    private String firstName;
    private String lastName;
    private String bio;
    private String location;
    private String gender;
    private String work;
    private String education;
    private String avatar;
    private String backgroundImage;
    private LocalDate dateOfBirth;
    @NotNull(message = "Password cannot be null")
    @NotBlank(message = "Password cannot be blank")
    private String password;

}
