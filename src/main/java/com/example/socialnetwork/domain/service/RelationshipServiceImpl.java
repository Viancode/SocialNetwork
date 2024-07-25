package com.example.socialnetwork.domain.service;

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

import java.util.*;
import java.util.stream.Collectors;

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
        RelationshipDomain reverseRelationshipDomain = relationshipDatabasePort.find(userId, senderId);
        if(reverseRelationshipDomain == null ){
            RelationshipDomain relationshipDomain = relationshipDatabasePort.find(senderId, userId);
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
        RelationshipDomain reverseRelationshipDomain = relationshipDatabasePort.find(userId, senderId);
        if(reverseRelationshipDomain == null ) {
            RelationshipDomain relationshipDomain = relationshipDatabasePort.find(senderId, userId);
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
        RelationshipDomain reverseRelationshipDomain = relationshipDatabasePort.find(receiverId, userId);
        if(reverseRelationshipDomain == null ) {
            RelationshipDomain relationshipDomain = relationshipDatabasePort.find(userId, receiverId);
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
        RelationshipDomain reverseRelationshipDomain = relationshipDatabasePort.find(receiverId, userId);
        if(reverseRelationshipDomain == null ) {
            RelationshipDomain relationshipDomain = relationshipDatabasePort.find(userId, receiverId);
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
    public List<UserDomain> findFriend(long userId, String keyWord) {
        return relationshipDatabasePort.findFriendByKeyWord(userId, keyWord);
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
    public List<UserDomain> getListFriend(long userId) {
        long currentUserId = getCurrentUser();
        if(userDatabasePort.findById(userId) == null)
            throw new NotFoundException("Not found user");
        if(userId == currentUserId) {
            return relationshipDatabasePort.getListFriend(userId);
        }else {
            RelationshipDomain relationshipDomain = relationshipDatabasePort.find(userId, currentUserId);
            RelationshipDomain relationshipDomain1 = relationshipDatabasePort.find(currentUserId, userId);
            if((relationshipDomain != null && relationshipDomain.getRelation() == ERelationship.FRIEND) || (relationshipDomain1 != null && relationshipDomain1.getRelation() == ERelationship.FRIEND)){
                return relationshipDatabasePort.getListFriend(userId);
            }else {
                throw new RuntimeException("Cannot get list friend of this user");
            }
        }
    }

    @Override
    public List<UserDomain> getFriendSuggestions(long userId) {
        UserDomain userCurrent = userDatabasePort.findById(userId);
        List<UserDomain> friends = relationshipDatabasePort.getListFriend(userId);
        friends.add(userCurrent);
        Set<Long> friendIds = friends.stream().map(UserDomain::getId).collect(Collectors.toSet());

        return userDatabasePort.getAllUser().stream()
                .filter(user -> !friendIds.contains(user.getId())) // Loại bỏ bạn bè khỏi danh sách người dùng
                .map(user -> new AbstractMap.SimpleEntry<>(user, calculateScore(userCurrent, user, userId)))
                .sorted(Map.Entry.<UserDomain, Integer>comparingByValue().reversed())
                .limit(10)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    private int calculateScore(UserDomain userCurrent, UserDomain user, long userId) {
        int score = getNumberOfMutualFriends(userId, user.getId());
        if (Objects.equals(userCurrent.getLocation(), user.getLocation())) score += 15;
        if (Objects.equals(userCurrent.getEducation(), user.getEducation())) score += 10;
        if (Objects.equals(userCurrent.getWork(), user.getWork())) score += 5;
        return score;
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

    private int getNumberOfMutualFriends(long userId1, long userId2){
        List<UserDomain> friends1 = relationshipDatabasePort.getListFriend(userId1);
        List<UserDomain> friends2 = relationshipDatabasePort.getListFriend(userId2);
        HashSet<UserDomain> set1 = new HashSet<>(friends1);
        HashSet<UserDomain> set2 = new HashSet<>(friends2);
        set1.retainAll(set2);
        return set1.size();
    }
}
