package com.example.socialnetwork.domain.port.api;

import com.example.socialnetwork.application.request.AuthRequest;
import com.example.socialnetwork.application.request.RegisterRequest;
import com.example.socialnetwork.application.response.AuthResponse;
import org.springframework.security.core.userdetails.User;

public interface AuthServicePort {
    void register(RegisterRequest registerRequest);
    void verifyToken(String token);
    AuthResponse forgotPassword(String email);
    void changePassword(User user, String newPassword);
    AuthResponse login(AuthRequest authRequest);
    AuthResponse refreshToken(String refreshToken);
    void logout(String refreshToken, User user);
    void logoutAllDevices(User user);
}
