package com.example.socialnetwork.domain.service;

import com.example.socialnetwork.domain.port.api.EmailServicePort;
import com.example.socialnetwork.domain.port.api.TokenServicePort;
import com.example.socialnetwork.domain.port.api.UserServicePort;
import com.example.socialnetwork.infrastructure.entity.User;
import com.example.socialnetwork.infrastructure.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;

import java.util.HashMap;
import java.util.Map;


@RequiredArgsConstructor
public class UserServiceImpl implements UserServicePort {
    private final UserRepository userRepository;
    private final EmailServicePort emailService;
    private final TokenServicePort tokenService;
    @Value("${link.front-end-domain}")
    private String domain;
    @Value("${link.confirm-email-verify}")
    private String confirmEmailVerifyLink;
    @Value("${link.forgot-password-verify}")
    private String resetPasswordVerifyLink;


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
}
