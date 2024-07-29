package com.example.socialnetwork.common.util;

import com.example.socialnetwork.domain.port.api.UserServicePort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserUtil {
    private final UserServicePort userServicePort ;

    public String getUserName(Long userId){
        return userServicePort.findUserById(userId).getUsername();
    }

    public String getAvatarUrl(Long userId){
        return userServicePort.findUserById(userId).getAvatar();
    }
}
