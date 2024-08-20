package com.example.socialnetwork.common.mapper;

import com.example.socialnetwork.application.response.FriendSuggestionResponse;
import com.example.socialnetwork.application.response.SearchFriendResponse;
import com.example.socialnetwork.common.constant.Status;
import com.example.socialnetwork.common.util.SecurityUtil;
import com.example.socialnetwork.domain.model.SuggestionDomain;
import com.example.socialnetwork.domain.model.UserDomain;
import com.example.socialnetwork.infrastructure.entity.Suggestion;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SuggestionMapper {
    SuggestionMapper INSTANCE = Mappers.getMapper(SuggestionMapper.class);

    SuggestionDomain toSuggestionDomain(Suggestion suggestion);

    List<SuggestionDomain> toSuggestionDomains(List<Suggestion> suggestions);

    default FriendSuggestionResponse toFriendSuggestionResponse(SuggestionDomain suggestionDomain) {
        if (suggestionDomain == null) {
            return null;
        }else {
            FriendSuggestionResponse response = new FriendSuggestionResponse();
            UserDomain user = suggestionDomain.getUser();
            if (user.getId() == SecurityUtil.getCurrentUserId()) user = suggestionDomain.getFriend();
            response.setId(user.getId());
            response.setAvatar(user.getAvatar());
            response.setUsername(user.getUsername());
            response.setEmail(user.getEmail());
            response.setMutualFriends(suggestionDomain.getMutualFriends());
            return response;
        }
    }

    List<FriendSuggestionResponse> toFriendSuggestionResponses(List<SuggestionDomain> suggestionDomains);

}
