package com.example.socialnetwork.common.mapper;

import com.example.socialnetwork.common.constant.ERole;
import com.example.socialnetwork.domain.model.UserDomain;
import com.example.socialnetwork.infrastructure.entity.Role;
import com.example.socialnetwork.infrastructure.entity.User;
import org.mapstruct.Mapper;

import java.util.Optional;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toUser(UserDomain userDomain);

    UserDomain toUserDomain(Optional<User> user);

    default Role toRole(ERole ERole){
        Role role = new Role();
        role.setName(ERole.name());
        return role;
    }

    default ERole toERole(Role Role){
        return ERole.valueOf(Role.getName());
    }
}