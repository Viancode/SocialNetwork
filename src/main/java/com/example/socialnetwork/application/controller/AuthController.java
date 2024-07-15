package com.example.socialnetwork.application.controller;

import com.example.socialnetwork.application.request.AuthRequest;
import com.example.socialnetwork.application.request.RegisterRequest;
import com.example.socialnetwork.application.response.AuthResponse;
import com.example.socialnetwork.domain.port.api.AuthServicePort;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.parameters.P;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthServicePort authService;

    @PostMapping("/register") ///
    public ResponseEntity<?> register(
            @Valid @RequestBody RegisterRequest registerRequest
    ) {
        authService.register(registerRequest);
        return ResponseEntity.ok("Register successfully");
    }

    @PostMapping("register/verify")
    public ResponseEntity<?> verifyToken(
            @RequestParam("token") String token
    ) {
        authService.verifyToken(token);
        return ResponseEntity.ok("Token is valid");
    }

//    @PostMapping("/forgot-pass") ///
//    public ResponseEntity<?> forgotPassword(
//            @RequestParam("email") String email
//    ) {
//        AuthResponse authResponse = authService.forgotPassword(email);
//        return ResponseEntity.ok(authResponse);
//    }

    @PostMapping("/change-pass") ///
    public ResponseEntity<?> changePassword(
            @RequestParam("newPassword") String newPassword,
            Authentication authentication
    ) {
        User user = (User) authentication.getPrincipal();
        authService.changePassword(user, newPassword);
        return ResponseEntity.ok("Change password successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticate(
            @Valid @RequestBody AuthRequest authRequest
    ) {
        AuthResponse authResponse = authService.login(authRequest);
        return ResponseEntity.ok(authResponse);
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(
            @RequestParam("refreshToken") String refreshToken
    ) {
        AuthResponse authResponse = authService.refreshToken(refreshToken);
        return ResponseEntity.ok(authResponse);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(
            @RequestParam("refreshToken") String refreshToken,
            Authentication authentication
    ) {
        User user = (User) authentication.getPrincipal();
        authService.logout(refreshToken, user);
        return ResponseEntity.ok("Logout successfully");
    }

    @PostMapping("/logout/all")
    public ResponseEntity<?> logoutAllDevices(
            Authentication authentication
    ) {
        User user = (User) authentication.getPrincipal();
        authService.logoutAllDevices(user);
        return ResponseEntity.ok("Logout from all devices successfully");
    }
}

