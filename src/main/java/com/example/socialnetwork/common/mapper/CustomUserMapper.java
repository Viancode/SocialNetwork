package com.example.socialnetwork.common.mapper;

import com.example.socialnetwork.application.response.CloseFriendResponse;
import com.example.socialnetwork.common.util.SecurityUtil;
import com.example.socialnetwork.domain.model.UserDomain;
import com.example.socialnetwork.infrastructure.repository.CloseRelationshipRepository;
import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomUserMapper {
    private final CloseRelationshipRepository closeRelationshipRepository;

    public CloseFriendResponse toCloseFriendResponse(UserDomain userDomain) {
        if (userDomain == null) {
            return null;
        } else {
            CloseFriendResponse closeFriendResponse = new CloseFriendResponse();
            closeFriendResponse.setId(userDomain.getId());
            closeFriendResponse.setAvatar(userDomain.getAvatar());
            closeFriendResponse.setUsername(userDomain.getUsername());
            closeFriendResponse.setEmail(userDomain.getEmail());
            closeFriendResponse.setCloseRelationship(closeRelationshipRepository.findCloseRelationship(userDomain.getId(), SecurityUtil.getCurrentUserId()));
            return closeFriendResponse;
        }
    }

}
