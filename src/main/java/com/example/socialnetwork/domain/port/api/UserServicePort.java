package com.example.socialnetwork.domain.port.api;

import com.example.socialnetwork.infrastructure.entity.User;

public interface UserServicePort {
    void sendVerificationEmail(User user, String confirmToken);

    void sendEmailResetPassword(User user, String resetToken);
}
