package com.example.socialnetwork.infrastructure.adapter;

import com.example.socialnetwork.application.request.RegisterRequest;
import com.example.socialnetwork.domain.port.spi.UserDatabasePort;
import com.example.socialnetwork.infrastructure.entity.Role;
import com.example.socialnetwork.infrastructure.entity.User;
import com.example.socialnetwork.infrastructure.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@RequiredArgsConstructor
public class UserDatabaseAdapter implements UserDatabasePort {
    private final PasswordEncoder encoder;
    private final UserRepository userRepository;
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
}
