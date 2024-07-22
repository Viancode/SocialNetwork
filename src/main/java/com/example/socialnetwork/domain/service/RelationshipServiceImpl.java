package com.example.socialnetwork.domain.service;

import com.example.socialnetwork.application.response.FriendSuggestionsResponse;
import com.example.socialnetwork.application.response.ListFriendResponse;
import com.example.socialnetwork.common.constant.ERelationship;
import com.example.socialnetwork.common.mapper.UserMapper;
import com.example.socialnetwork.domain.model.RelationshipDomain;
import com.example.socialnetwork.domain.model.UserDomain;
import com.example.socialnetwork.domain.port.api.RelationshipServicePort;
import com.example.socialnetwork.domain.port.spi.RelationshipDatabasePort;
import com.example.socialnetwork.domain.port.spi.UserDatabasePort;
import com.example.socialnetwork.exception.custom.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class RelationshipServiceImpl implements RelationshipServicePort {
    private final RelationshipDatabasePort relationshipDatabasePort;
    private final UserDatabasePort userDatabasePort;
    private final UserMapper userMapper;

    @Override
    public void deleteRelationship(long userId, long friendId) {
        checkCurrentUser(userId);
        checkFriend(friendId);
        relationshipDatabasePort.deleteFriend(userId, friendId);
    }

    @Override
    public void sendRequestMakeFriendship(long senderId, long receiverId) {
        checkCurrentUser(senderId);
        checkCurrentUser(receiverId);
        RelationshipDomain relationshipDomain = relationshipDatabasePort.find(senderId, receiverId);
        if(relationshipDomain != null) {
            throw new RuntimeException("Cannot make friendship");
        }else {
            relationshipDatabasePort.createRelationship(senderId, receiverId, ERelationship.PENDING);
            }
    }

    @Override
    public void deleteRequestMakeFriendship(long senderId, long receiverId) {
        checkCurrentUser(senderId);
        checkCurrentUser(receiverId);
        RelationshipDomain relationshipDomain = relationshipDatabasePort.find(senderId, receiverId);
        if(relationshipDomain.getRelation() == ERelationship.PENDING) {
            relationshipDatabasePort.deleteRequest(senderId, receiverId);
        }else {
            throw new RuntimeException("Cannot delete friend request");
        }
    }

    @Override
    public void acceptRequestMakeFriendship(long senderId, long receiverId) {
        checkCurrentUser(receiverId);
        checkCurrentUser(senderId);
        RelationshipDomain relationshipDomain = relationshipDatabasePort.find(senderId, receiverId);
        if(relationshipDomain.getRelation() == ERelationship.PENDING) {
            relationshipDatabasePort.updateRelation(senderId, receiverId, ERelationship.FRIEND);
        }else {
            throw new RuntimeException("Cannot accept friend request");
        }
    }

    @Override
    public void refuseRequestMakeFriendship(long senderId, long receiverId) {
        checkCurrentUser(receiverId);
        checkCurrentUser(senderId);
        RelationshipDomain relationshipDomain = relationshipDatabasePort.find(senderId, receiverId);
        if(relationshipDomain.getRelation() == ERelationship.PENDING) {
            relationshipDatabasePort.deleteRequest(senderId, receiverId);
        }else {
            throw new RuntimeException("Cannot refuse friend request");
        }
    }

    @Override
    public void block(long userId, long friendId) {
        checkCurrentUser(userId);
        checkFriend(friendId);
        RelationshipDomain relationshipDomain = relationshipDatabasePort.find(userId, friendId);
        if(relationshipDomain == null) {
            relationshipDatabasePort.createRelationship(userId, friendId, ERelationship.BLOCK);
        }else {
            relationshipDatabasePort.updateRelation(userId, friendId, ERelationship.BLOCK);
        }
    }

    @Override
    public List<RelationshipDomain> getListRequest(long userId) {
        checkCurrentUser(userId);
        return relationshipDatabasePort.getListRequest(userId);
    }

    @Override
    public List<ListFriendResponse> getListFriend(long userId) {
        checkCurrentUser(userId);
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

    private void checkCurrentUser(long userId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        org.springframework.security.core.userdetails.User user = (User) authentication.getPrincipal();
        if(Long.parseLong(user.getUsername()) != userId) {
            throw new InvalidParameterException("Id of user invalid");
        }

    }

    private void checkFriend(long friendId) {
        if(userDatabasePort.findById(friendId) == null)
            throw new NotFoundException("Not found friend");
    }
}
