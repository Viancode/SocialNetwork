package com.example.socialnetwork.domain.service;

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

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class RelationshipServiceImpl implements RelationshipServicePort {
    private final RelationshipDatabasePort relationshipDatabasePort;
    private final UserDatabasePort userDatabasePort;
    private final UserMapper userMapper;

    @Override
    public ERelationship getRelationship(long sourceUserID, long targetUserID) {
        RelationshipDomain relationshipDomain = relationshipDatabasePort.find(sourceUserID, targetUserID);
        if(relationshipDomain == null) {
            throw new NotFoundException("Not found relationship");
        }
        return relationshipDomain.getRelation();
    }

    @Override
    public void deleteRelationship(long friendId) {
        long userId = getCurrentUser();
        checkFriend(friendId);
        relationshipDatabasePort.deleteFriend(userId, friendId);
    }

    @Override
    public void sendRequestMakeFriendship(long userId) {
        long senderId = getCurrentUser();
        checkFriend(userId);
        RelationshipDomain relationshipDomain = relationshipDatabasePort.find(senderId, userId);
        RelationshipDomain relationshipDomain1 = relationshipDatabasePort.find(userId, senderId);
        if(relationshipDomain1 == null ){
            if(relationshipDomain == null){
                relationshipDatabasePort.createRelationship(senderId, userId, ERelationship.PENDING);
            } else if(relationshipDomain.getRelation() == ERelationship.PENDING) {
                throw new RuntimeException("You have sent a friend request to this person before");
            }else if(relationshipDomain.getRelation() == ERelationship.FRIEND)
                throw new RuntimeException("Cannot send friend request because you two are already friends");
            else {
                throw new RuntimeException("Cannot send friend request");
            }
        }else {
            throw new RuntimeException("Cannot send friend request");
        }

    }

    @Override
    public void deleteRequestMakeFriendship(long userId) {
        long senderId = getCurrentUser();
        checkFriend(userId);
        RelationshipDomain relationshipDomain = relationshipDatabasePort.find(senderId, userId);
        RelationshipDomain relationshipDomain1 = relationshipDatabasePort.find(userId, senderId);
        if(relationshipDomain1 == null ) {
            if (relationshipDomain == null) {
                throw new NotFoundException("Friend request not found");
            } else if (relationshipDomain.getRelation() == ERelationship.PENDING)
                relationshipDatabasePort.deleteRequest(senderId, userId);
            else if (relationshipDomain.getRelation() == ERelationship.FRIEND)
                throw new RuntimeException("Cannot delete friend request because you two are already friends");
            else {
                throw new RuntimeException("Cannot delete friend request");
            }
        }else {
            throw new RuntimeException("Cannot delete friend request");
        }
    }

    @Override
    public void acceptRequestMakeFriendship(long userId) {
        long receiverId = getCurrentUser();
        checkFriend(userId);
        RelationshipDomain relationshipDomain = relationshipDatabasePort.find(userId, receiverId);
        RelationshipDomain relationshipDomain1 = relationshipDatabasePort.find(receiverId, userId);
        if(relationshipDomain1 == null ) {
            if (relationshipDomain == null) {
                throw new NotFoundException("Friend request not found");
            } else if (relationshipDomain.getRelation() == ERelationship.PENDING) {
                relationshipDatabasePort.updateRelation(userId, receiverId, ERelationship.FRIEND);
            } else if (relationshipDomain.getRelation() == ERelationship.BLOCK) {
                throw new RuntimeException("Cannot accept friend request");
            }
        }else {
            throw new RuntimeException("Cannot accept friend request");
        }
    }

    @Override
    public void refuseRequestMakeFriendship(long userId) {
        long receiverId = getCurrentUser();
        checkFriend(userId);
        RelationshipDomain relationshipDomain = relationshipDatabasePort.find(userId, receiverId);
        RelationshipDomain relationshipDomain1 = relationshipDatabasePort.find(receiverId, userId);
        if(relationshipDomain1 == null ) {
            if(relationshipDomain == null) {
                throw new NotFoundException("Friend request not found");
            }else if(relationshipDomain.getRelation() == ERelationship.PENDING) {
                relationshipDatabasePort.deleteRequest(userId, receiverId);
            }else if(relationshipDomain.getRelation() == ERelationship.FRIEND)
                throw new RuntimeException("Cannot refuse friend request because you two are already friends");
            else {
                throw new RuntimeException("Cannot refuse friend request");
            }
        }else {
            throw new RuntimeException("Cannot refuse friend request");
        }
    }

    @Override
    public void block(long friendId) {
        long userId = getCurrentUser();
        checkFriend(friendId);
        RelationshipDomain relationshipDomain = relationshipDatabasePort.find(userId, friendId);
        if(relationshipDomain == null) {
            relationshipDatabasePort.createRelationship(userId, friendId, ERelationship.BLOCK);
        }else {
            relationshipDatabasePort.updateRelation(userId, friendId, ERelationship.BLOCK);
        }
    }

    @Override
    public List<RelationshipDomain> getListReceiveRequest() {
        long userId = getCurrentUser();
        return relationshipDatabasePort.getListReceiveRequest(userId);
    }

    @Override
    public List<RelationshipDomain> getListSendRequest() {
        long userId = getCurrentUser();
        return relationshipDatabasePort.getListSendRequest(userId);
    }

    @Override
    public List<ListFriendResponse> getListFriend(long userId) {
        long currentUserId = getCurrentUser();
        if(userDatabasePort.findById(userId) == null)
            throw new NotFoundException("Not found user");
        if(userId == currentUserId) {
            List<RelationshipDomain> relationshipDomains = relationshipDatabasePort.getListFriend(userId);
            return getListFriendResponses(userId, relationshipDomains);
        }else {
            RelationshipDomain relationshipDomain = relationshipDatabasePort.find(userId, currentUserId);
            RelationshipDomain relationshipDomain1 = relationshipDatabasePort.find(currentUserId, userId);
            if((relationshipDomain != null && relationshipDomain.getRelation() == ERelationship.FRIEND) || (relationshipDomain1 != null && relationshipDomain1.getRelation() == ERelationship.FRIEND)){
                List<RelationshipDomain> relationshipDomains = relationshipDatabasePort.getListFriend(userId);
                return getListFriendResponses(userId, relationshipDomains);
            }else {
                throw new RuntimeException("Cannot get list friend of this user");
            }
        }
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

    private long getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        org.springframework.security.core.userdetails.User user = (User) authentication.getPrincipal();
        return Long.parseLong(user.getUsername());
    }

    private void checkFriend(long friendId) {
        if(userDatabasePort.findById(friendId) == null)
            throw new NotFoundException("Not found friend");
    }
}
