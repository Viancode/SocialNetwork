package com.example.socialnetwork.domain.service;

import com.example.socialnetwork.application.response.FriendSuggestionsResponse;
import com.example.socialnetwork.application.response.ListFriendResponse;
import com.example.socialnetwork.application.response.MakeFriendResponse;
import com.example.socialnetwork.common.constant.ERelationship;
import com.example.socialnetwork.common.mapper.UserMapper;
import com.example.socialnetwork.domain.model.RelationshipDomain;
import com.example.socialnetwork.domain.model.UserDomain;
import com.example.socialnetwork.domain.port.api.RelationshipServicePort;
import com.example.socialnetwork.domain.port.spi.RelationshipDatabasePort;
import com.example.socialnetwork.domain.port.spi.UserDatabasePort;
import lombok.RequiredArgsConstructor;
import org.aspectj.asm.internal.Relationship;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class RelationshipServiceImpl implements RelationshipServicePort {
    private final RelationshipDatabasePort relationshipDatabasePort;
    private final UserMapper userMapper;

    @Override
    public boolean deleteRelationship(long userId, long friendId) {
        return relationshipDatabasePort.deleteRelationship(userId, friendId);
    }

    @Override
    public boolean updateRelationship(long userId, long friendId, String relationship) {
        return relationshipDatabasePort.updateRelationship(userId, friendId, relationship);
    }

    @Override
    public boolean sendRequestMakeFriendship(long userId, long friendId) {
        RelationshipDomain relationshipDomain = relationshipDatabasePort.find(userId, friendId);
        if((relationshipDomain == null) || (relationshipDomain.getStatus() == 2)) {
            return relationshipDatabasePort.changeStatusAndSave(userId, friendId, 0) != null;
        }
        return false;
    }

    @Override
    public boolean deleteRequestMakeFriendship(long userId, long friendId) {
        RelationshipDomain relationshipDomain = relationshipDatabasePort.find(userId, friendId);
        if(relationshipDomain.getStatus() != 0) {
            return false;
        }
        return relationshipDatabasePort.changeStatusAndSave(userId, friendId, 2) != null;
    }

    @Override
    public boolean acceptRequestMakeFriendship(long userId, long friendId) {
        RelationshipDomain relationshipDomain = relationshipDatabasePort.find(friendId, userId);
        if(relationshipDomain.getStatus() != 0) {
            return false;
        }
        return relationshipDatabasePort.changeStatusAndSave(friendId, userId, 1) != null;
    }

    @Override
    public boolean refuseRequestMakeFriendship(long userId, long friendId) {
        RelationshipDomain relationshipDomain = relationshipDatabasePort.find(friendId, userId);
        if(relationshipDomain.getStatus() != 0) {
            return false;
        }
        return relationshipDatabasePort.changeStatusAndSave(friendId, userId, 2) != null;
    }

    @Override
    public List<RelationshipDomain> getListRequest(long userId) {
        return relationshipDatabasePort.getListRequest(userId);
    }

    @Override
    public List<ListFriendResponse> search(long userId, String name) {
        List<RelationshipDomain> relationshipDomains = relationshipDatabasePort.search(userId, name);
        return getListFriendResponses(userId, relationshipDomains);
    }

    @Override
    public List<FriendSuggestionsResponse> viewSuggest(long userId) {
        return List.of();
    }

    @Override
    public List<ListFriendResponse> getListFriend(long userId) {
        List<RelationshipDomain> relationshipDomains = relationshipDatabasePort.getListFriend(userId);
        return getListFriendResponses(userId, relationshipDomains);
    }

    private List<ListFriendResponse> getListFriendResponses(long userId, List<RelationshipDomain> relationshipDomains) {
        List<ListFriendResponse> listFriendResponses = new ArrayList<>();
        for(RelationshipDomain relationshipDomain : relationshipDomains){
            if(relationshipDomain.getUser().getId() == userId){
                UserDomain userDomain = relationshipDomain.getFriend();
                ListFriendResponse listFriendResponse = userMapper.toListFriendResponse(userDomain);
                listFriendResponse.setRelationship(relationshipDomain.getRelation().toString());
                listFriendResponses.add(listFriendResponse);
            }else {
                UserDomain userDomain = relationshipDomain.getUser();
                ListFriendResponse listFriendResponse = userMapper.toListFriendResponse(userDomain);
                listFriendResponse.setRelationship(relationshipDomain.getRelation().toString());
                listFriendResponses.add(listFriendResponse);
            }
        }
        return listFriendResponses;
    }
}
