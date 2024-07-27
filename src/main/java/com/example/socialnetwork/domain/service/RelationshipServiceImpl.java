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
        if (relationshipDomain == null) {
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
        if (relationshipDomain == null) {
            relationshipDatabasePort.createRelationship(senderId, userId, ERelationship.PENDING);
        } else if (relationshipDomain.getRelation() == ERelationship.FRIEND)
            throw new RuntimeException("Cannot send friend request because you two are already friends");
        else {
            throw new RuntimeException("Cannot send friend request");
        }
    }

    @Override
    public void deleteRequestMakeFriendship(long userId) {
        long senderId = getCurrentUser();
        checkFriend(userId);
        RelationshipDomain relationshipDomain = relationshipDatabasePort.find(senderId, userId);
        if (relationshipDomain == null) {
            throw new NotFoundException("Friend request not found");
        } else if (relationshipDomain.getRelation() == ERelationship.PENDING && relationshipDomain.getUser().getId() == senderId)
            relationshipDatabasePort.deleteRequest(senderId, userId);
        else if (relationshipDomain.getRelation() == ERelationship.FRIEND)
            throw new RuntimeException("Cannot delete friend request because you two are already friends");
        else {
            throw new RuntimeException("Cannot delete friend request");
        }
    }

    @Override
    public void acceptRequestMakeFriendship(long userId) {
        long receiverId = getCurrentUser();
        checkFriend(userId);
        RelationshipDomain relationshipDomain = relationshipDatabasePort.find(userId, receiverId);
        if (relationshipDomain == null) {
            throw new NotFoundException("Friend request not found");
        } else if (relationshipDomain.getRelation() == ERelationship.PENDING && relationshipDomain.getUser().getId() == userId) {
            relationshipDatabasePort.updateRelation(userId, receiverId, ERelationship.FRIEND);
        } else if (relationshipDomain.getRelation() == ERelationship.BLOCK) {
            throw new RuntimeException("Cannot accept friend request");
        }
    }

    @Override
    public void refuseRequestMakeFriendship(long userId) {
        long receiverId = getCurrentUser();
        checkFriend(userId);
        RelationshipDomain relationshipDomain = relationshipDatabasePort.find(userId, receiverId);
        if (relationshipDomain == null) {
            throw new NotFoundException("Friend request not found");
        } else if (relationshipDomain.getRelation() == ERelationship.PENDING && relationshipDomain.getUser().getId() == userId) {
            relationshipDatabasePort.deleteRequest(userId, receiverId);
        } else if (relationshipDomain.getRelation() == ERelationship.FRIEND)
            throw new RuntimeException("Cannot refuse friend request because you two are already friends");
        else {
            throw new RuntimeException("Cannot refuse friend request");
        }
    }

    @Override
    public void block(long friendId) {
        long userId = getCurrentUser();
        checkFriend(friendId);
        RelationshipDomain relationshipDomain = relationshipDatabasePort.find(userId, friendId);
        if (relationshipDomain == null) {
            relationshipDatabasePort.createRelationship(userId, friendId, ERelationship.BLOCK);
        } else {
            relationshipDatabasePort.updateRelation(userId, friendId, ERelationship.BLOCK);
        }
    }

    @Override
    public List<UserDomain> findFriend(long userId, String keyWord) {
        return relationshipDatabasePort.findFriendByKeyWord(userId, keyWord);
    }

    @Override
    public List<UserDomain> getListReceiveRequest() {
        long userId = getCurrentUser();
        List<UserDomain> userDomains = new ArrayList<>();
        List<RelationshipDomain> relationshipDomains =  relationshipDatabasePort.getListReceiveRequest(userId);
        for (RelationshipDomain relationshipDomain : relationshipDomains) {
            userDomains.add(relationshipDomain.getUser());
        }
        return userDomains;
    }

    @Override
    public List<UserDomain> getListSendRequest() {
        long userId = getCurrentUser();
        List<UserDomain> userDomains = new ArrayList<>();
        List<RelationshipDomain> relationshipDomains =  relationshipDatabasePort.getListSendRequest(userId);
        for (RelationshipDomain relationshipDomain : relationshipDomains) {
            userDomains.add(relationshipDomain.getFriend());
        }
        return userDomains;
    }

    @Override
    public List<UserDomain> getListFriend(long userId) {
        long currentUserId = getCurrentUser();
        if (userDatabasePort.findById(userId) == null)
            throw new NotFoundException("Not found user");
        if (userId == currentUserId) {
            return relationshipDatabasePort.getListFriend(userId);
        } else {
            RelationshipDomain relationshipDomain = relationshipDatabasePort.find(userId, currentUserId);
            if ((relationshipDomain != null && relationshipDomain.getRelation() == ERelationship.FRIEND)) {
                return relationshipDatabasePort.getListFriend(userId);
            } else {
                throw new RuntimeException("Cannot get list friend of this user");
            }
        }
    }

    @Override
    public List<UserDomain> getListBlock() {
        long currentUserId = getCurrentUser();
        return relationshipDatabasePort.getListBlock(currentUserId);
    }

    @Override
    public List<UserDomain> getFriendSuggestions(long userId) {
        UserDomain userCurrent = userDatabasePort.findById(userId);
        List<UserDomain> userlist = userDatabasePort.getAllUser();
        List<UserDomain> friends = relationshipDatabasePort.getListFriend(userId);
        friends.add(userCurrent);
        userlist.removeAll(friends);
        Map<UserDomain, Integer> map = new HashMap<>();
        int commonScore = 0;
        for (UserDomain user : userlist) {
            commonScore += calculateScore(userCurrent, user);
            if (map.size() == 10) break;
            map.put(user, commonScore);
            commonScore = 0;
        }
        return map.entrySet()
                .stream()
                .sorted(Map.Entry.<UserDomain, Integer>comparingByValue().reversed())
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    private int calculateScore(UserDomain userCurrent, UserDomain user) {
        int score = getNumberOfMutualFriends(userCurrent.getId(), user.getId());
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
        if (userDatabasePort.findById(friendId) == null)
            throw new NotFoundException("Not found friend");
    }

    private int getNumberOfMutualFriends(long userId1, long userId2) {
        List<UserDomain> friends1 = relationshipDatabasePort.getListFriend(userId1);
        List<UserDomain> friends2 = relationshipDatabasePort.getListFriend(userId2);
        HashSet<UserDomain> set1 = new HashSet<>(friends1);
        HashSet<UserDomain> set2 = new HashSet<>(friends2);
        set1.retainAll(set2);
        return set1.size();
    }
}
