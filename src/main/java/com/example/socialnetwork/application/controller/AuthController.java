package com.example.socialnetwork.application.controller;

import com.example.socialnetwork.application.request.AuthRequest;
import com.example.socialnetwork.application.request.RegisterRequest;
import com.example.socialnetwork.application.response.AuthResponse;
import com.example.socialnetwork.domain.port.api.AuthServicePort;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController extends BaseController {
    private final AuthServicePort authService;

    @PostMapping("/register") ///
    public ResponseEntity<?> register(
            @Valid @RequestBody RegisterRequest registerRequest
    ) {
        authService.register(registerRequest);
        return buildResponse("Please check your email to verify your account");
    }

    @PostMapping("register/verify")
    public ResponseEntity<?> verifyRegisterToken(
            @RequestParam("token") String token
    ) {
        authService.verifyRegisterToken(token);
        return buildResponse("Token is valid");
    }

    @PostMapping("/forgot-pass")
    public ResponseEntity<?> forgotPassword(
            @RequestParam("email") String email
    ) {
        authService.forgotPassword(email);
        return buildResponse("Reset password request has been sent to your email");
    }

//    @PostMapping("/forgot-pass/verify")
//    public ResponseEntity<?> verifyForgotPassToken(
//            @RequestParam("token") String token
//    ) {
//        authService.verifyForgetPassToken(token);
//        return ResponseEntity.ok("Token is valid");
//    }

    @PostMapping("/reset-pass")
    public ResponseEntity<?> resetPassword(
            @RequestParam("token") String token,
            @RequestParam("newPassword") String newPassword
    ) {
        authService.resetPasswordWithToken(token, newPassword);
        return buildResponse("Password has been reset");
    }

    @PostMapping("/change-pass") ///
    public ResponseEntity<?> changePassword(
            @RequestParam("newPassword") String newPassword,
            @RequestParam("oldPassword") String oldPassword,
            Authentication authentication
    ) {
        User user = (User) authentication.getPrincipal();
        authService.changePassword(user, newPassword, oldPassword);
        return buildResponse("Change password successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticate(
            @Valid @RequestBody AuthRequest authRequest
    ) {
        AuthResponse authResponse = authService.login(authRequest);
        return buildResponse("Successfully logged in", authResponse);
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(
            @RequestParam("refreshToken") String refreshToken
    ) {
        AuthResponse authResponse = authService.refreshToken(refreshToken);
        return buildResponse("Successfully refreshed token", authResponse);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(
            @RequestParam("refreshToken") String refreshToken,
            Authentication authentication
    ) {
        User user = (User) authentication.getPrincipal();
        authService.logout(refreshToken, user);
        return buildResponse("Logout successfully");
    }

    @PostMapping("/logout/all")
    public ResponseEntity<?> logoutAllDevices(
            Authentication authentication
    ) {
        User user = (User) authentication.getPrincipal();
        authService.logoutAllDevices(user);
        return buildResponse("Logout from all devices successfully");
    }
}