package com.example.socialnetwork.domain.port.spi;

import com.example.socialnetwork.application.request.RegisterRequest;
import com.example.socialnetwork.infrastructure.entity.User;

public interface UserServicePort {
    User createUser(RegisterRequest registerRequest);

    void sendVerificationEmail(User user, String confirmToken);

    void sendEmailResetPassword(User user, String resetToken);
    void confirmEmail(String token);
}
