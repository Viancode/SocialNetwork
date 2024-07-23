package com.example.socialnetwork.domain.port.spi;

import com.example.socialnetwork.application.request.RegisterRequest;
import com.example.socialnetwork.domain.model.UserDomain;
import com.example.socialnetwork.infrastructure.entity.User;

public interface UserDatabasePort {
    User createUser(RegisterRequest registerRequest);
    UserDomain findById(long id);
    void deleteById(long id);
    void save(UserDomain user);
}
