package com.example.socialnetwork.domain.port.api;

import com.example.socialnetwork.application.request.ProfileRequest;
import com.example.socialnetwork.domain.model.UserDomain;
import com.example.socialnetwork.infrastructure.entity.User;

public interface UserServicePort {
    UserDomain getProfile(Long sourceUserId, Long targetUserID);
    void deleteProfile(Long userId);
    void sendVerificationEmail(User user, String confirmToken);
    void sendEmailResetPassword(User user, String resetToken);
    void updateProfile(Long userId, ProfileRequest profileRequest);
}
