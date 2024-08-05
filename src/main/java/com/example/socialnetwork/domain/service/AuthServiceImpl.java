package com.example.socialnetwork.domain.service;

import com.example.socialnetwork.application.request.AuthRequest;
import com.example.socialnetwork.application.request.RegisterRequest;
import com.example.socialnetwork.application.response.AuthResponse;
import com.example.socialnetwork.common.constant.TokenType;
import com.example.socialnetwork.common.publisher.CustomEventPublisher;
import com.example.socialnetwork.domain.port.api.AuthServicePort;
import com.example.socialnetwork.domain.port.api.JwtServicePort;
import com.example.socialnetwork.domain.port.api.TokenServicePort;
import com.example.socialnetwork.domain.port.api.UserServicePort;
import com.example.socialnetwork.domain.port.spi.UserDatabasePort;
import com.example.socialnetwork.exception.custom.DuplicateException;
import com.example.socialnetwork.exception.custom.NotFoundException;
import com.example.socialnetwork.infrastructure.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
public class AuthServiceImpl implements AuthServicePort {
    private final JwtServicePort jwtService;
    private final TokenServicePort tokenService;
    private final UserRepository userRepository;
    private final UserServicePort userService;
    private final UserDatabasePort userDatabase;
    private final AuthenticationManager authenticationManager;
    private final CustomEventPublisher customEventPublisher;
    @Value("${token.verified-expiration}")
    private long verifyExpiration;
    @Value("${token.refresh-expiration}")
    private long refreshExpiration;

    @Override
    public void register(RegisterRequest registerRequest) {
        com.example.socialnetwork.infrastructure.entity.User user = userRepository.findByEmail(registerRequest.getEmail()).orElse(null);
        if (user == null) {
            user = userDatabase.createUser(registerRequest);
        } else {
            boolean isEmailVerified = user.getIsEmailVerified();
            boolean isExitVerifyToken = tokenService.getTokenByUserId(String.valueOf(user.getId()), TokenType.VERIFIED) != null; // true = exist
            // user is not verified and there is no token in the database => create new user
            if (!isEmailVerified && !isExitVerifyToken) {
                userRepository.delete(user);
                user = userDatabase.createUser(registerRequest);
            } else {
                throw new DuplicateException("This email is being used by another user");
            }
        }

        String confirmToken = jwtService.generateVerifyToken();
        System.out.println(confirmToken);
        tokenService.saveToken(confirmToken, String.valueOf(user.getId()), TokenType.VERIFIED, verifyExpiration);
        userService.sendVerificationEmail(user, confirmToken);
    }

    @Override
    @Transactional
    public void verifyRegisterToken(String token) {
        String userId = tokenService.getTokenInfo(token, TokenType.VERIFIED);
        com.example.socialnetwork.infrastructure.entity.User user = userRepository.findById(Long.valueOf(userId)).orElseThrow(() -> new NotFoundException("User not found"));
        user.setIsEmailVerified(true);
        user.getRole().getName(); // This line is just to trigger the lazy loading within the transaction
        userRepository.save(user);
        customEventPublisher.publishRegisterEvent(user.getId());
        tokenService.revokeAllUserTokens(String.valueOf(user.getId()), TokenType.VERIFIED);
    }

    @Override
    public void verifyForgetPassToken(String token) {
        String userId = tokenService.getTokenInfo(token, TokenType.VERIFIED);

        if (userId == null) {
            throw new NotFoundException("Invalid token");
        }
    }


    @Override
    public void forgotPassword(String email) {
        com.example.socialnetwork.infrastructure.entity.User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("User not found"));

        String resetToken = jwtService.generateVerifyToken();
        tokenService.saveToken(resetToken, String.valueOf(user.getId()), TokenType.VERIFIED, verifyExpiration);
        userService.sendEmailResetPassword(user, resetToken);
    }

    @Override
    public void changePassword(User user, String newPassword, String oldPassword) {
        com.example.socialnetwork.infrastructure.entity.User currentUser = userRepository.findById(Long.parseLong(user.getUsername()))
                .orElseThrow(() -> new NotFoundException("User not found"));

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        // Check if the old password matches the one in the database
        if (!encoder.matches(oldPassword, currentUser.getPassword())) {
            throw new IllegalArgumentException("Old password does not match");
        }

        tokenService.revokeAllUserTokens(user.getUsername(), TokenType.REFRESH);
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

        boolean isEmailVerify = userRepository.findUserById(Long.parseLong(user.getUsername())).orElseThrow(() -> new NotFoundException("User not found")).getIsEmailVerified();

        if (!isEmailVerify) {
            throw new NotFoundException("Email is not verified");
        } else {
            String accessToken = jwtService.generateAccessToken(user);
            String refreshToken = jwtService.generateRefreshToken();
            tokenService.saveToken(refreshToken, user.getUsername(), TokenType.REFRESH, refreshExpiration );
            return AuthResponse.builder()
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .build();
        }
    }

    @Override
    public AuthResponse refreshToken(String refreshToken) {
        String userId = tokenService.getTokenInfo(refreshToken, TokenType.REFRESH);


        UserDetails user = userRepository.findUserById(Long.parseLong(userId)).map(userInfo -> {
            // build user detail
            return User.builder()
                    .username(String.valueOf(userInfo.getId()))
                    .password("")
                    .authorities(userInfo.getRole().getName())
                    .build();
        }).orElseThrow(() -> new NotFoundException("User not found"));

        // Remove the refreshToken from Redis
        tokenService.revokeAllUserTokens(user.getUsername(), TokenType.REFRESH);

        // Generate new accessToken
        String newAccessToken = jwtService.generateAccessToken((User) user);

        return AuthResponse.builder()
                .accessToken(newAccessToken)
                .refreshToken(refreshToken)
                .build();
    }

    @Override
    public void logout(String refreshToken, User user) {
        tokenService.revokeRefreshToken(refreshToken, user);
    }

    @Override
    public void logoutAllDevices(User user) {
        tokenService.revokeAllUserTokens(user.getUsername(), TokenType.REFRESH);
    }

    @Override
    public void resetPasswordWithToken(String token, String newPassword) {
        String userId = tokenService.getTokenInfo(token, TokenType.VERIFIED);
        com.example.socialnetwork.infrastructure.entity.User user = userRepository.findById(Long.parseLong(userId))
                .orElseThrow(() -> new NotFoundException("User not found or invalid token"));

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String hashedPassword = encoder.encode(newPassword);
        userRepository.updatePassword(Integer.valueOf(userId), hashedPassword);
        tokenService.revokeAllUserTokens(String.valueOf(user.getId()), TokenType.REFRESH);
    }
}
