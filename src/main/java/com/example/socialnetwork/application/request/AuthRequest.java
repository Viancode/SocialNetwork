package com.example.socialnetwork.application.request;

import com.example.socialnetwork.common.ValidationRegex;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Value;

@Value
@Builder
public class AuthRequest {
    @Email(message = "Email should be valid", regexp = ValidationRegex.EMAIL_REGEX)
    @NotNull(message = "Email cannot be null")
    @NotBlank(message = "Email cannot be blank")
    String email;
    @Pattern(message = "Password should be valid", regexp = ValidationRegex.PASSWORD_REGEX)
    @NotNull(message = "Password cannot be null")
    @NotBlank(message = "Password cannot be blank")
    String password;
}