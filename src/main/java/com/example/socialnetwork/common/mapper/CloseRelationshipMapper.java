package com.example.socialnetwork.common.mapper;

import com.example.socialnetwork.application.request.CloseRelationshipRequest;
import com.example.socialnetwork.application.response.CloseRelationshipResponse;
import com.example.socialnetwork.common.util.SecurityUtil;
import com.example.socialnetwork.domain.model.CloseRelationshipDomain;
import com.example.socialnetwork.domain.model.UserDomain;
import com.example.socialnetwork.domain.port.spi.UserDatabasePort;
import com.example.socialnetwork.infrastructure.entity.CloseRelationship;
import com.example.socialnetwork.infrastructure.repository.UserRepository;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface CloseRelationshipMapper {
    CloseRelationshipMapper INSTANCE = Mappers.getMapper(CloseRelationshipMapper.class);


    @Mapping(target = "user.id", expression = "java(getUserId())")
    @Mapping(target = "targetUser.id", source = "targetUserId")
    CloseRelationshipDomain requestToDomain(CloseRelationshipRequest request);

    @Mapping(source = "user.id", target = "user.id")
    @Mapping(source = "targetUser.id", target = "targetUser.id")
    CloseRelationship domainToEntity(CloseRelationshipDomain domain);

    @Mapping(source = "user.id", target = "user.id")
    @Mapping(source = "targetUser.id", target = "targetUser.id")
    CloseRelationshipDomain entityToDomain(CloseRelationship relationship);

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "targetUser.id", target = "targetUserId")
    @Mapping(source = "targetUser.username", target = "targetUsername")
    CloseRelationshipResponse domainToResponse(CloseRelationshipDomain domain);

    @Named("getUserId")
    default Long getUserId() {
        return SecurityUtil.getCurrentUserId();
    }

//    @Named("mapUserById")
//    public class UserMapperHelper {
//
//        private final UserDatabasePort userRepository;
//
//        public UserMapperHelper(UserDatabasePort userRepository) {
//            this.userRepository = userRepository;
//        }
//
//        @Named("mapUserById")
//        public UserDomain mapUserById(Long userId) {
//            return userRepository.findById(userId);
//        }
//    }
}
