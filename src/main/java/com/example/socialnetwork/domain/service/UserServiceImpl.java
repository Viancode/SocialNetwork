package com.example.socialnetwork.domain.service;

import com.example.socialnetwork.application.request.ProfileRequest;
import com.example.socialnetwork.common.constant.ERelationship;
import com.example.socialnetwork.common.constant.FileType;
import com.example.socialnetwork.common.constant.Gender;
import com.example.socialnetwork.common.constant.Visibility;
import com.example.socialnetwork.domain.model.UserDomain;
import com.example.socialnetwork.domain.port.api.*;
import com.example.socialnetwork.domain.port.spi.UserDatabasePort;
import com.example.socialnetwork.exception.custom.NotAllowException;
import com.example.socialnetwork.exception.custom.NotFoundException;
import com.example.socialnetwork.infrastructure.entity.User;
import com.example.socialnetwork.infrastructure.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;

import java.io.NotActiveException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


@RequiredArgsConstructor
public class UserServiceImpl implements UserServicePort {
    private final EmailServicePort emailService;
    private final UserDatabasePort userDatabase;
    private final RelationshipServicePort relationshipService;
    private final S3ServicePort s3Service;
    private final StorageServicePort storageService;
    @Value("${link.front-end-domain}")
    private String domain;
    @Value("${link.confirm-email-verify}")
    private String confirmEmailVerifyLink;
    @Value("${link.forgot-password-verify}")
    private String resetPasswordVerifyLink;


    @Override
    public UserDomain getProfile(Long sourceUserId, Long targetUserID) {
        UserDomain sourceUser = userDatabase.findById(sourceUserId);
        UserDomain targetUser = userDatabase.findById(targetUserID);

        if (sourceUser == null || targetUser == null) {
            throw new NotFoundException("User not found");
        }

        // allow to see the profile
        // 1. get own profile
        // 2. target user is public
        // 3. visibility is FRIEND + relation between source and target is friend
        boolean isOwnProfile = Objects.equals(sourceUserId, targetUserID);
        boolean isPublicProfile = targetUser.getVisibility() == Visibility.PUBLIC;
        boolean isFriendProfile = targetUser.getVisibility() == Visibility.FRIEND &&
                relationshipService.getRelationship(sourceUserId, targetUserID) == ERelationship.FRIEND;

        if (isOwnProfile || isPublicProfile || isFriendProfile) {
            targetUser.setAvatar(targetUser.getAvatar() == null ?
                    "User does not have avatar" :
                    storageService.getUrl(targetUser.getAvatar()));

            targetUser.setBackgroundImage(targetUser.getBackgroundImage() == null ?
                    "User does not have background image" :
                    storageService.getUrl(targetUser.getBackgroundImage()));

            return targetUser;
        }

        // now allow when target user is private or relation between source and target is block and other case
        throw new NotAllowException("You are not allowed to view this profile");
    }

    @Override
    public void deleteProfile(Long userId) {
        UserDomain user = userDatabase.findById(userId);
        if (user == null) {
            throw new NotFoundException("User not found");
        }
        // delete avatar and background image in s3
        if (user.getAvatar() != null) {
            s3Service.deleteFile(user.getAvatar());
        }
        if (user.getBackgroundImage() != null) {
            s3Service.deleteFile(user.getBackgroundImage());
        }

        // delete in database
        userDatabase.deleteById(userId);
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
    public void updateProfile(Long userId, ProfileRequest profileRequest) {
        UserDomain user = userDatabase.findById(userId);
        if (user == null) {
            throw new NotFoundException("User not found");
        }

        user.setUsername(profileRequest.getFirstName() + " " + profileRequest.getLastName());
        user.setFirstName(profileRequest.getFirstName());
        user.setLastName(profileRequest.getLastName());
        user.setGender(Gender.valueOf(profileRequest.getGender()));
        user.setVisibility(Visibility.valueOf(profileRequest.getVisibility()));
        user.setBio(profileRequest.getBio());
        user.setLocation(profileRequest.getLocation());
        user.setWork(profileRequest.getWork());
        user.setEducation(profileRequest.getEducation());
        user.setUpdatedAt(LocalDateTime.now());
        user.setDateOfBirth(profileRequest.getDateOfBirth());
        user.setAvatar(profileRequest.getAvatar());
        user.setBackgroundImage(profileRequest.getBackground());

        userDatabase.save(user);
    }
}
