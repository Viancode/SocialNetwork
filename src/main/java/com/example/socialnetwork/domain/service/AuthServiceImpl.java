package com.example.socialnetwork.domain.service;

import com.example.socialnetwork.application.request.AuthRequest;
import com.example.socialnetwork.application.request.RegisterRequest;
import com.example.socialnetwork.application.response.AuthResponse;
import com.example.socialnetwork.common.constant.TokenType;
import com.example.socialnetwork.domain.port.api.AuthServicePort;
import com.example.socialnetwork.domain.port.api.JwtServicePort;
import com.example.socialnetwork.domain.port.api.TokenServicePort;
import com.example.socialnetwork.domain.port.spi.UserServicePort;
import com.example.socialnetwork.exception.custom.DuplicateException;
import com.example.socialnetwork.exception.custom.NotFoundException;
import com.example.socialnetwork.infrastructure.adapter.UserService;
import com.example.socialnetwork.infrastructure.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class AuthServiceImpl implements AuthServicePort {
    private final JwtServicePort jwtService;
    private final TokenServicePort tokenService;
    private final UserRepository userRepository;
    private final UserServicePort userService;
    private final AuthenticationManager authenticationManager;
    @Value("${token.verified-expiration}")
    private long verifyExpiration;
    @Value("${token.refresh-expiration}")
    private long refreshExpiration;

    public AuthServiceImpl(JwtServicePort jwtService, TokenServicePort tokenService, UserRepository userRepository, UserServicePort userService, AuthenticationManager authenticationManager) {
        this.jwtService = jwtService;
        this.tokenService = tokenService;
        this.userRepository = userRepository;
        this.userService = userService;
        this.authenticationManager = authenticationManager;
    }

    @Override
    public void register(RegisterRequest registerRequest) {
        com.example.socialnetwork.infrastructure.entity.User user = userRepository.findByEmail(registerRequest.getEmail()).orElse(null);
        if (user == null) {
            user = userService.createUser(registerRequest);
        } else {
            if (!user.isEmailVerified()) {
                tokenService.revokeAllUserTokens((User) User.builder()
                        .username(String.valueOf(user.getId()))
                        .authorities(user.getRole().getName())
                        .password(user.getPassword())
                        .build(),
                        TokenType.VERIFIED);

                userRepository.delete(user);
                user = userService.createUser(registerRequest);
            } else {
                throw new DuplicateException("This email is being used by another user");
            }
        }

        String confirmToken = jwtService.generateVerifyToken();
        tokenService.saveToken(confirmToken, user.getUsername(), TokenType.VERIFIED, verifyExpiration);
        userService.sendVerificationEmail(user, confirmToken);
    }

    @Override
    public void verifyToken(String token) {
        userService.confirmEmail(token);
    }

    @Override
    public AuthResponse forgotPassword(String email) {
        return null;
    }

    @Override
    public void changePassword(User user, String newPassword) {
        tokenService.revokeAllUserTokens(user, TokenType.REFRESH);
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String hashedPassword = encoder.encode(newPassword);
        userRepository.updatePassword(Integer.valueOf(user.getUsername()), hashedPassword);
    }


    @Override
    public AuthResponse login(AuthRequest authRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authRequest.getEmail(),
                        authRequest.getPassword()));

        User user = (User) authentication.getPrincipal();

        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken();
        tokenService.saveToken(refreshToken, user.getUsername(), TokenType.REFRESH, refreshExpiration );
        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    @Override
    public AuthResponse refreshToken(String refreshToken) {
        String userId = tokenService.getTokenInfo(refreshToken, TokenType.REFRESH);


        UserDetails user = userRepository.findUserById(Long.parseLong(userId)).map(userInfo -> {
            // Ensure you're using Spring Security's User class here
            return User.builder()
                    .username(String.valueOf(userInfo.getId()))
                    .password("")
                    .authorities(userInfo.getRole().getName())
                    .build();
        }).orElseThrow(() -> new NotFoundException("User not found"));

        // Remove the refreshToken from Redis
        tokenService.revokeAllUserTokens((User) user, TokenType.REFRESH);

        // Generate new accessToken and refreshToken
        String newAccessToken = jwtService.generateAccessToken((User) user);
        String newRefreshToken = jwtService.generateRefreshToken();
        tokenService.saveToken(newRefreshToken, user.getUsername(), TokenType.REFRESH, refreshExpiration);

        return AuthResponse.builder()
                .accessToken(newAccessToken)
                .refreshToken(newRefreshToken)
                .build();
    }

    @Override
    public void logout(String refreshToken, User user) {
        tokenService.revokeRefreshToken(refreshToken, user);
    }

    @Override
    public void logoutAllDevices(User user) {
        tokenService.revokeAllUserTokens(user, TokenType.REFRESH);
    }
}
