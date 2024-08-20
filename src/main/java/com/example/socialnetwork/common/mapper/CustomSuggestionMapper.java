package com.example.socialnetwork.common.mapper;

import com.example.socialnetwork.application.response.FriendResponse;
import com.example.socialnetwork.common.constant.ERelationship;
import com.example.socialnetwork.common.util.SecurityUtil;
import com.example.socialnetwork.domain.model.SuggestionDomain;
import com.example.socialnetwork.domain.model.UserDomain;
import com.example.socialnetwork.infrastructure.entity.Relationship;
import com.example.socialnetwork.infrastructure.repository.CloseRelationshipRepository;
import com.example.socialnetwork.infrastructure.repository.RelationshipRepository;
import com.example.socialnetwork.infrastructure.repository.SuggestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Component
@RequiredArgsConstructor
public class CustomSuggestionMapper {
    private final CloseRelationshipRepository closeRelationshipRepository;
    private final RelationshipRepository relationshipRepository;
    private final SuggestionRepository suggestionRepository;
    public FriendResponse toSearchFriendResponse(SuggestionDomain suggestionDomain) {
        if (suggestionDomain == null) {
            return null;
        }else {
            FriendResponse friendResponse = new FriendResponse();
            UserDomain user = suggestionDomain.getUser();
            if (user.getId() == SecurityUtil.getCurrentUserId()) user = suggestionDomain.getFriend();
            friendResponse.setId(user.getId());
            friendResponse.setUsername(user.getUsername());
            friendResponse.setEmail(user.getEmail());
            friendResponse.setAvatar(user.getAvatar());
            friendResponse.setMutualFriends(suggestionDomain.getMutualFriends());
            Relationship relationship = relationshipRepository.findByUser_IdAndFriend_Id(suggestionDomain.getUser().getId(), suggestionDomain.getFriend().getId());
            if(relationship != null) {
                if(relationship.getRelation() == ERelationship.PENDING) friendResponse.setStatus(ERelationship.RECEIVED);
                else
                    friendResponse.setStatus(relationship.getRelation());
            }else {
                relationship = relationshipRepository.getRelationship(suggestionDomain.getFriend().getId(), suggestionDomain.getUser().getId());
                if (relationship != null) {
                    if(relationship.getRelation() == ERelationship.PENDING) friendResponse.setStatus(ERelationship.REQUESTING);
                    else
                        friendResponse.setStatus(relationship.getRelation());
                }else {
                    friendResponse.setStatus(null);
                }
            }
            friendResponse.setCloseRelationship(closeRelationshipRepository.findCloseRelationship(suggestionDomain.getUser().getId(), suggestionDomain.getFriend().getId()));
            return friendResponse;
        }
    }

    public FriendResponse toSearchFriendResponse(UserDomain userDomain) {
        if (userDomain == null) {
            return null;
        }else {
            FriendResponse friendResponse = new FriendResponse();
            friendResponse.setId(userDomain.getId());
            friendResponse.setUsername(userDomain.getUsername());
            friendResponse.setEmail(userDomain.getEmail());
            friendResponse.setAvatar(userDomain.getAvatar());
            friendResponse.setMutualFriends(suggestionRepository.findByUserAndFriend(userDomain.getId(), SecurityUtil.getCurrentUserId()).getMutualFriends());
            Relationship relationship = relationshipRepository.getRelationship(userDomain.getId(), SecurityUtil.getCurrentUserId());
            if(relationship != null) {
                if(relationship.getRelation() == ERelationship.PENDING) friendResponse.setStatus(ERelationship.RECEIVED);
                else
                    friendResponse.setStatus(relationship.getRelation());
            }else {
                relationship = relationshipRepository.getRelationship(SecurityUtil.getCurrentUserId(), userDomain.getId());
                if (relationship != null) {
                    if(relationship.getRelation() == ERelationship.PENDING) friendResponse.setStatus(ERelationship.REQUESTING);
                    else
                        friendResponse.setStatus(relationship.getRelation());
                }else {
                    friendResponse.setStatus(null);
                }
            }
            friendResponse.setCloseRelationship(closeRelationshipRepository.findCloseRelationship(userDomain.getId(), SecurityUtil.getCurrentUserId()));
            return friendResponse;
        }
    }

    public List<FriendResponse> toSearchFriendResponses(List<SuggestionDomain> suggestionDomains){
        if (suggestionDomains == null) {
            return null;
        } else {
            List<FriendResponse> list = new ArrayList(suggestionDomains.size());
            Iterator var3 = suggestionDomains.iterator();

            while(var3.hasNext()) {
                SuggestionDomain suggestionDomain = (SuggestionDomain)var3.next();
                list.add(this.toSearchFriendResponse(suggestionDomain));
            }

            return list;
        }
    }

    public List<FriendResponse> userDomainsToSearchFriendResponses(List<UserDomain> userDomains){
        if (userDomains == null) {
            return null;
        } else {
            List<FriendResponse> list = new ArrayList(userDomains.size());
            Iterator var3 = userDomains.iterator();

            while(var3.hasNext()) {
                UserDomain userDomain = (UserDomain)var3.next();
                list.add(this.toSearchFriendResponse(userDomain));
            }

            return list;
        }
    }

}
