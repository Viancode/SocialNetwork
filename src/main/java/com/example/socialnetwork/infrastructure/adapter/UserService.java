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
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

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
    public void confirmEmail(String token) {
        String userId = tokenService.getTokenInfo(token, TokenType.VERIFIED);
        User user = userRepository.findById(Long.valueOf(userId)).orElseThrow(() -> new NotFoundException("User not found"));
        user.setEmailVerified(true);
        userRepository.save(user);

        org.springframework.security.core.userdetails.User userDetails = (org.springframework.security.core.userdetails.User) org.springframework.security.core.userdetails.User.builder()
                .username(String.valueOf(user.getId()))
                .authorities(user.getRole().getName())
                .password(user.getPassword())
                .build();

        tokenService.revokeAllUserTokens(userDetails, TokenType.VERIFIED);
    }
}
