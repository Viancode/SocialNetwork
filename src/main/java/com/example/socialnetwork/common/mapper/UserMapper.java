package com.example.socialnetwork.common.mapper;

import com.example.socialnetwork.domain.model.UserDomain;
import com.example.socialnetwork.infrastructure.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toUser(UserDomain userDomain);

    UserDomain toUserDomain(User user);
}
