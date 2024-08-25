package com.example.socialnetwork.application.controller;

import com.example.socialnetwork.application.request.AuthRequest;
import com.example.socialnetwork.application.request.RegisterRequest;
import com.example.socialnetwork.application.response.AuthResponse;
import com.example.socialnetwork.application.response.ResultResponse;
import com.example.socialnetwork.common.ValidationRegex;
import com.example.socialnetwork.domain.port.api.AuthServicePort;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Validated
@Tag(name = "Authentication")
public class AuthController extends BaseController {
    private final AuthServicePort authService;

    @Operation(
            summary = "Register an account",
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ResultResponse.class),
                                    examples = @ExampleObject(
                                            value = """
                                                        {
                                                            "status": 200,
                                                            "message": "Please check your email to verify your account",
                                                            "result": {},
                                                            "timestamp": "2024-08-25T21:31:07.111115500Z"
                                                        }
                                                    """
                                    )
                            )
                    ),
                    @ApiResponse(description = "The access token provided is expired, revoked, malformed, or invalid for other reasons.",
                            responseCode = "401", content = @Content()),
                    @ApiResponse(description = "", responseCode = "404", content = @Content()),
                    @ApiResponse(description = "", responseCode = "400", content = @Content())
            }
    )
    @PostMapping("/register") ///
    public ResponseEntity<?> register(
            @Valid @RequestBody RegisterRequest registerRequest
    ) {
        authService.register(registerRequest);
        return buildResponse("Please check your email to verify your account");
    }

    @Operation(
            summary = "Verify Register request",
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ResultResponse.class),
                                    examples = @ExampleObject(
                                            value = """
                                                        {
                                                            "status": 200,
                                                            "message": "token is valid",
                                                            "result": {},
                                                            "timestamp": "2024-08-13T00:41:22.073430290Z"
                                                        }
                                                    """
                                    )
                            )
                    ),
                    @ApiResponse(description = "The access token provided is expired, revoked, malformed, or invalid for other reasons.",
                            responseCode = "401", content = @Content()),
                    @ApiResponse(description = "", responseCode = "404", content = @Content()),
                    @ApiResponse(description = "", responseCode = "400", content = @Content())
            }
    )
    @PostMapping("register/verify")
    public ResponseEntity<?> verifyRegisterToken(
            @RequestParam("token") String token
    ) {
        authService.verifyRegisterToken(token);
        return buildResponse("Token is valid");
    }

    @Operation(
            summary = "Forgot password",
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ResultResponse.class),
                                    examples = @ExampleObject(
                                            value = """
                                                        {
                                                            "status": 200,
                                                            "message": "Reset password request has been sent to your email",
                                                            "result": {},
                                                            "timestamp": "2024-08-25T21:35:03.460375Z"
                                                        }
                                                    """
                                    )
                            )
                    ),
                    @ApiResponse(description = "The access token provided is expired, revoked, malformed, or invalid for other reasons.",
                            responseCode = "401", content = @Content()),
                    @ApiResponse(description = "", responseCode = "404", content = @Content()),
                    @ApiResponse(description = "", responseCode = "400", content = @Content())
            }
    )
    @PostMapping("/forgot_pass")
    public ResponseEntity<?> forgotPassword(
            @Email(message = "Email should be valid", regexp = ValidationRegex.EMAIL_REGEX)
            @RequestParam("email") String email
    ) {
        authService.forgotPassword(email);
        return buildResponse("Reset password request has been sent to your email");
    }

    @Operation(
            summary = "Verify forgot password request and create new password",
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ResultResponse.class),
                                    examples = @ExampleObject(
                                            value = """
                                                        {
                                                            "status": 200,
                                                            "message": "Token is valid",
                                                            "result": {},
                                                            "timestamp": "2024-08-25T21:37:40.748317200Z"
                                                        }
                                                    """
                                    )
                            )
                    ),
                    @ApiResponse(description = "The access token provided is expired, revoked, malformed, or invalid for other reasons.",
                            responseCode = "401", content = @Content()),
                    @ApiResponse(description = "", responseCode = "404", content = @Content()),
                    @ApiResponse(description = "", responseCode = "400", content = @Content())
            }
    )
    @PostMapping("/verify_forgot_pass")
    public ResponseEntity<?> verifyForgotPasswordToken(
            @RequestParam("token") String token
    ) {
        authService.verifyForgetPassToken(token);
        return buildResponse("Token is valid");
    }

    @Operation(
            summary = "Verify forgot password request and create new password",
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ResultResponse.class),
                                    examples = @ExampleObject(
                                            value = """
                                                        {
                                                            "status": 200,
                                                            "message": "Password has been reset",
                                                            "result": {},
                                                            "timestamp": "2024-08-25T21:37:40.748317200Z"
                                                        }
                                                    """
                                    )
                            )
                    ),
                    @ApiResponse(description = "The access token provided is expired, revoked, malformed, or invalid for other reasons.",
                            responseCode = "401", content = @Content()),
                    @ApiResponse(description = "", responseCode = "404", content = @Content()),
                    @ApiResponse(description = "", responseCode = "400", content = @Content())
            }
    )
    @PostMapping("/reset_pass")
    public ResponseEntity<?> resetPassword(
            @RequestParam("token") String token,
            @NotBlank(message = "New password cannot be blank")
            @Pattern(message = "Password should be valid", regexp = ValidationRegex.PASSWORD_REGEX)
            @RequestParam(value = "new_password") String newPassword
    ) {
        authService.resetPasswordWithToken(token, newPassword);
        return buildResponse("Password has been reset");
    }

    @Operation(
            summary = "Change password of user",
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ResultResponse.class),
                                    examples = @ExampleObject(
                                            value = """
                                                        {
                                                            "status": 200,
                                                            "message": "Change password successfully",
                                                            "result": {},
                                                            "timestamp": "2024-08-13T00:41:22.073430290Z"
                                                        }
                                                    """
                                    )
                            )
                    ),
                    @ApiResponse(description = "The access token provided is expired, revoked, malformed, or invalid for other reasons.",
                            responseCode = "401", content = @Content()),
                    @ApiResponse(description = "", responseCode = "404", content = @Content()),
                    @ApiResponse(description = "", responseCode = "400", content = @Content())
            }
    )
    @SecurityRequirement(name = "bearerAuth")
    @PostMapping("/change_pass") ///
    public ResponseEntity<?> changePassword(
            @NotBlank(message = "New password cannot be blank")
            @Pattern(message = "Password should be valid", regexp = ValidationRegex.PASSWORD_REGEX)
            @RequestParam(value = "new_password") String newPassword,
            @RequestParam(value = "old_password") String oldPassword
    ) {
        authService.changePassword(newPassword, oldPassword);
        return buildResponse("Change password successfully");
    }

    @Operation(
            summary = "Login",
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ResultResponse.class),
                                    examples = @ExampleObject(
                                            value = """
                                                        {
                                                            "status": 200,
                                                            "message": "Successfully logged in",
                                                            "result": {
                                                                "data": {
                                                                    "accessToken": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwiYXV0aG9yaXRpZXMiOlsiVVNFUiJdLCJpYXQiOjE3MjQ2MjIwNDQsImV4cCI6MTc5NjYyMjA0NH0.6VbPTPxgjxlkkKEQPvVaRGGDnfEW0-thJ_OP44FrLCo",
                                                                    "refreshToken": "6bd9c7c1-5c3d-43c8-825c-596741cf8487"
                                                                }
                                                            },
                                                            "timestamp": "2024-08-25T21:40:44.566582Z"
                                                        }
                                                    """
                                    )
                            )
                    ),
                    @ApiResponse(description = "The access token provided is expired, revoked, malformed, or invalid for other reasons.",
                            responseCode = "401", content = @Content()),
                    @ApiResponse(description = "", responseCode = "404", content = @Content()),
                    @ApiResponse(description = "", responseCode = "400", content = @Content())
            }
    )
    @PostMapping("/login")
    public ResponseEntity<?> authenticate(
            @Valid @RequestBody AuthRequest authRequest
    ) {
        AuthResponse authResponse = authService.login(authRequest);
        return buildResponse("Successfully logged in", authResponse);
    }

    @Operation(
            summary = "Get new access token",
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ResultResponse.class),
                                    examples = @ExampleObject(
                                            value = """
                                                        {
                                                            "status": 200,
                                                            "message": "Successfully refreshed token",
                                                            "result": {
                                                                "data": {
                                                                    "accessToken": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyMUBnbWFpbC5jb20iLCJhdXRob3JpdGllcyI6WyJVU0VSIl0sImlhdCI6MTcyNDYyMjA4MywiZXhwIjoxNzk2NjIyMDgzfQ.GY59Gb3PAkgHIFalpDjLrixrCiTTXNA586ZpvxEhoPI",
                                                                    "refreshToken": "6bd9c7c1-5c3d-43c8-825c-596741cf8487"
                                                                }
                                                            },
                                                            "timestamp": "2024-08-25T21:41:23.866444200Z"
                                                        }
                                                    """
                                    )
                            )
                    ),
                    @ApiResponse(description = "The access token provided is expired, revoked, malformed, or invalid for other reasons.",
                            responseCode = "401", content = @Content()),
                    @ApiResponse(description = "", responseCode = "404", content = @Content()),
                    @ApiResponse(description = "", responseCode = "400", content = @Content())
            }
    )
    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(
            @RequestParam("refresh_token") String refreshToken
    ) {
        AuthResponse authResponse = authService.refreshToken(refreshToken);
        return buildResponse("Successfully refreshed token", authResponse);
    }

    @Operation(
            summary = "Logout",
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ResultResponse.class),
                                    examples = @ExampleObject(
                                            value = """
                                                        {
                                                            "status": 200,
                                                            "message": "Logout successfully",
                                                            "result": {},
                                                            "timestamp": "2024-08-13T00:41:22.073430290Z"
                                                        }
                                                    """
                                    )
                            )
                    ),
                    @ApiResponse(description = "The access token provided is expired, revoked, malformed, or invalid for other reasons.",
                            responseCode = "401", content = @Content()),
                    @ApiResponse(description = "", responseCode = "404", content = @Content()),
                    @ApiResponse(description = "", responseCode = "400", content = @Content())
            }
    )
    @SecurityRequirement(name = "bearerAuth")
    @PostMapping("/logout")
    public ResponseEntity<?> logout(
            @RequestParam("refresh_token") String refreshToken
    ) {
        authService.logout(refreshToken);
        return buildResponse("Logout successfully");
    }

    @Operation(
            summary = "Logout from all device",
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ResultResponse.class),
                                    examples = @ExampleObject(
                                            value = """
                                                        {
                                                            "status": 200,
                                                            "message": "Logout from all devices successfully",
                                                            "result": {},
                                                            "timestamp": "2024-08-13T00:41:22.073430290Z"
                                                        }
                                                    """
                                    )
                            )
                    ),
                    @ApiResponse(description = "The access token provided is expired, revoked, malformed, or invalid for other reasons.",
                            responseCode = "401", content = @Content()),
                    @ApiResponse(description = "", responseCode = "404", content = @Content()),
                    @ApiResponse(description = "", responseCode = "400", content = @Content())
            }
    )
    @SecurityRequirement(name = "bearerAuth")
    @PostMapping("/logout/all")
    public ResponseEntity<?> logoutAllDevices(
            Authentication authentication
    ) {
        User user = (User) authentication.getPrincipal();
        authService.logoutAllDevices(user);
        return buildResponse("Logout from all devices successfully");
    }
}