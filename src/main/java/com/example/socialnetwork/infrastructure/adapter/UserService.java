package com.example.socialnetwork.infrastructure.adapter;

import com.example.socialnetwork.application.request.RegisterRequest;
import com.example.socialnetwork.common.constant.TokenType;
import com.example.socialnetwork.domain.port.api.EmailServicePort;
import com.example.socialnetwork.domain.port.api.TokenServicePort;
import com.example.socialnetwork.domain.port.spi.UserServicePort;
import com.example.socialnetwork.exception.custom.NotFoundException;
import com.example.socialnetwork.infrastructure.entity.Role;
import com.example.socialnetwork.infrastructure.entity.User;
import com.example.socialnetwork.infrastructure.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserService implements UserServicePort {
    private final UserRepository userRepository;
    private final PasswordEncoder encoder;
    private final EmailServicePort emailService;
    private final TokenServicePort tokenService;
    @Value("${link.front-end-domain}")
    private String domain;
    @Value("${link.confirm-email-verify}")
    private String confirmEmailVerifyLink;
    @Value("${link.forgot-password-verify}")
    private String resetPasswordVerifyLink;
    @Override
    public User createUser(RegisterRequest registerRequest) {
        User user = userRepository.findByEmail(registerRequest.getEmail()).orElse(null);
        if (user ==null) {
            user = new User();
            user.setEmail(registerRequest.getEmail());
            user.setPassword(encoder.encode(registerRequest.getPassword()));
            user.setFirstName(registerRequest.getFirstName());
            user.setLastName(registerRequest.getLastName());
            user.setBio(registerRequest.getBio());
            user.setLocation(registerRequest.getLocation());
            user.setWork(registerRequest.getWork());
            user.setEducation(registerRequest.getEducation());
            user.setAvatar(registerRequest.getAvatar());
            user.setBackgroundImage(registerRequest.getBackgroundImage());
            user.setAge(registerRequest.getAge());
            user.setRole(Role.builder().id(Long.valueOf(registerRequest.getRoleId())).build());
            user.setEmailVerified(false);
            user.setUsername(registerRequest.getFirstName() + " " + registerRequest.getLastName());

            user.setCreatedAt(LocalDateTime.now());
            user.setUpdatedAt(LocalDateTime.now());
            user.setVisibility("PUBLIC");

            return userRepository.save(user);
        } else {
            return user;
        }
    }

    @Override
    public void sendVerificationEmail(User user, String confirmToken) {
        String subject = "Confirm your email address, " + user.getFirstName() + " " + user.getLastName();
        Map<String, Object> model = new HashMap<>();
        String link = domain + confirmEmailVerifyLink + confirmToken;
        model.put("link", link);
        model.put("name", user.getFirstName());
        emailService.send(subject, user.getEmail(), "email/email-confirmation.html", model);
    }

    @Override
    public void sendEmailResetPassword(User user, String token) {
        String subject = "Reset your password, " + user.getFirstName();
        Map<String, Object> model = new HashMap<>();
        String link = domain + resetPasswordVerifyLink + token;
        model.put("link", link);
        model.put("name", user.getFirstName());
        emailService.send(subject, user.getEmail(), "email/forgot-password.html", model);
    }

    @Override
    @Transactional
    public void confirmEmail(String token) {
        String userId = tokenService.getTokenInfo(token, TokenType.VERIFIED);
        User user = userRepository.findById(Long.valueOf(userId)).orElseThrow(() -> new NotFoundException("User not found"));
        user.setEmailVerified(true);
        user.getRole().getName(); // This line is just to trigger the lazy loading within the transaction
        userRepository.save(user);

        tokenService.revokeAllUserTokens(String.valueOf(user.getId()), TokenType.VERIFIED);
    }
}
