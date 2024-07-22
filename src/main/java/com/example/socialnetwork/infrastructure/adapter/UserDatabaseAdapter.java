package com.example.socialnetwork.infrastructure.adapter;

import com.example.socialnetwork.application.request.RegisterRequest;
import com.example.socialnetwork.common.mapper.UserMapper;
import com.example.socialnetwork.domain.model.UserDomain;
import com.example.socialnetwork.domain.port.spi.UserDatabasePort;
import com.example.socialnetwork.infrastructure.entity.User;
import com.example.socialnetwork.infrastructure.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.example.socialnetwork.infrastructure.entity.Role;


import java.time.LocalDateTime;

@RequiredArgsConstructor
public class UserDatabaseAdapter implements UserDatabasePort {
    private final PasswordEncoder encoder;
    private final UserRepository userRepository;
    private final UserMapper userMapper;
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
            user.setDateOfBirth(registerRequest.getDateOfBirth());
            user.setRole(Role.builder().id(1L).build());
            user.setIsEmailVerified(false);
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
    public UserDomain findById(long id) {
        return userMapper.toUserDomain(userRepository.findById(id).get());
    }
}
