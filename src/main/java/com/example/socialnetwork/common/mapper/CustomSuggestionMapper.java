package com.example.socialnetwork.common.mapper;

import com.example.socialnetwork.application.response.SearchFriendResponse;
import com.example.socialnetwork.common.constant.Status;
import com.example.socialnetwork.common.util.SecurityUtil;
import com.example.socialnetwork.domain.model.SuggestionDomain;
import com.example.socialnetwork.domain.model.UserDomain;
import com.example.socialnetwork.infrastructure.repository.CloseRelationshipRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Component
@RequiredArgsConstructor
public class CustomSuggestionMapper {
    private final CloseRelationshipRepository closeRelationshipRepository;
    public SearchFriendResponse toSearchFriendResponse(SuggestionDomain suggestionDomain) {
        if (suggestionDomain == null) {
            return null;
        }else {
            SearchFriendResponse searchFriendResponse = new SearchFriendResponse();
            UserDomain user = suggestionDomain.getUser();
            if (user.getId() == SecurityUtil.getCurrentUserId()) user = suggestionDomain.getFriend();
            searchFriendResponse.setId(user.getId());
            searchFriendResponse.setUsername(user.getUsername());
            searchFriendResponse.setEmail(user.getEmail());
            searchFriendResponse.setMutualFriends(suggestionDomain.getMutualFriends());
            if(suggestionDomain.getStatus() == Status.FRIEND)
                searchFriendResponse.setStatus("Friend");
            else searchFriendResponse.setStatus(null);
            searchFriendResponse.setCloseRelationship(closeRelationshipRepository.findCloseRelationship(suggestionDomain.getUser().getId(), suggestionDomain.getFriend().getId()));
            return searchFriendResponse;
        }
    }

    public List<SearchFriendResponse> toSearchFriendResponses(List<SuggestionDomain> suggestionDomains){
        if (suggestionDomains == null) {
            return null;
        } else {
            List<SearchFriendResponse> list = new ArrayList(suggestionDomains.size());
            Iterator var3 = suggestionDomains.iterator();

            while(var3.hasNext()) {
                SuggestionDomain suggestionDomain = (SuggestionDomain)var3.next();
                list.add(this.toSearchFriendResponse(suggestionDomain));
            }

            return list;
        }
    }
}
