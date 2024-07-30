package com.example.socialnetwork.domain.service;

import com.example.socialnetwork.common.constant.ERelationship;
import com.example.socialnetwork.common.constant.Visibility;
import com.example.socialnetwork.domain.model.RelationshipDomain;
import com.example.socialnetwork.domain.model.UserDomain;
import com.example.socialnetwork.domain.port.api.RelationshipServicePort;
import com.example.socialnetwork.domain.port.spi.RelationshipDatabasePort;
import com.example.socialnetwork.domain.port.spi.UserDatabasePort;
import com.example.socialnetwork.exception.custom.NotFoundException;
import com.example.socialnetwork.exception.custom.RelationshipException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;

import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class RelationshipServiceImpl implements RelationshipServicePort {
    private final RelationshipDatabasePort relationshipDatabasePort;
    private final UserDatabasePort userDatabasePort;

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
            throw new RelationshipException("Cannot send friend request because you two are already friends");
        else {
            throw new RelationshipException("Cannot send friend request");
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
            throw new RelationshipException("Cannot delete friend request because you two are already friends");
        else {
            throw new RelationshipException("Cannot delete friend request");
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
            throw new RelationshipException("Cannot accept friend request");
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
            throw new RelationshipException("Cannot refuse friend request because you two are already friends");
        else {
            throw new RelationshipException("Cannot refuse friend request");
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
    public Page<UserDomain> findFriend(int page, int pageSize, String keyWord) {
        long userId = getCurrentUser();
        return relationshipDatabasePort.findFriendByKeyWord(page, pageSize, userId, keyWord);
    }

    @Override
    public Page<UserDomain> getListReceiveRequest(int page, int pageSize) {
        long userId = getCurrentUser();
        return relationshipDatabasePort.getListReceiveRequest(page, pageSize, userId);
    }

    @Override
    public Page<UserDomain> getListSendRequest(int page, int pageSize) {
        long userId = getCurrentUser();
        return relationshipDatabasePort.getListSendRequest(page, pageSize, userId);
    }

    @Override
    public Page<UserDomain> getListFriend(int page, int pageSize, long userId, String sortDirection, String sortBy) {
        long currentUserId = getCurrentUser();
        var pageable = PageRequest.of(page - 1, pageSize);
        UserDomain friend = userDatabasePort.findById(userId);
        if (friend == null)
            throw new NotFoundException("Not found user");
        if (userId == currentUserId) {
            Sort.Direction direction = sortDirection.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
            Sort sort = Sort.by(direction, sortBy);
            return relationshipDatabasePort.getListFriend(page, pageSize, userId, sort);
        } else {
            Visibility visibility = friend.getVisibility();
            RelationshipDomain relationshipDomain = relationshipDatabasePort.find(userId, currentUserId);
            if (visibility == Visibility.PRIVATE || (relationshipDomain != null && relationshipDomain.getRelation() != ERelationship.FRIEND && visibility == Visibility.FRIEND)) {
                List<UserDomain> mutualFriends =  getListMutualFriends(userId, currentUserId);
                int start = Math.min((int) pageable.getOffset(), mutualFriends.size());
                int end = Math.min((start + pageable.getPageSize()), mutualFriends.size());
                List<UserDomain> pagedUsers = mutualFriends.subList(start, end);
                return new PageImpl<>(pagedUsers, pageable, mutualFriends.size()) ;
            } else {
                Sort sort = Sort.by(Sort.Direction.ASC, "username");
                return relationshipDatabasePort.getListFriend(page, pageSize, userId, sort);
            }
        }
    }

    @Override
    public Page<UserDomain> getListBlock(int page, int pageSize, String sortDirection, String sortBy) {
        long currentUserId = getCurrentUser();
        Sort.Direction direction = sortDirection.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Sort sort = Sort.by(direction, sortBy);
        return relationshipDatabasePort.getListBlock(page, pageSize,currentUserId, sort);
    }

    @Override
    public Page<UserDomain> getFriendSuggestions(int page, int pageSize) {
        long userId = getCurrentUser();
        var pageable = PageRequest.of(page - 1, pageSize);
        List<UserDomain> userDomains =  getFriendSuggest(userId);
        int start = Math.min((int) pageable.getOffset(), userDomains.size());
        int end = Math.min((start + pageable.getPageSize()), userDomains.size());
        List<UserDomain> pagedUsers = userDomains.subList(start, end);
        return new PageImpl<>(pagedUsers, pageable, 30) ;
    }

    @Override
    public Page<UserDomain> searchUser(int page, int pageSize, String keyWord) {
        long userId = getCurrentUser();
        var pageable = PageRequest.of(page - 1, pageSize);
        List<UserDomain> friends = relationshipDatabasePort.findFriendByKeyWord(userId, keyWord);
        List<UserDomain> suggestUsers =  getFriendSuggest(userId);
        for(UserDomain suggestUser : suggestUsers) {
            if(suggestUser.getEmail().toLowerCase().contains(keyWord.toLowerCase()) || suggestUser.getUsername().toLowerCase().contains(keyWord.toLowerCase())) {
                friends.add(suggestUser);
            }
        }
        int start = Math.min((int) pageable.getOffset(), friends.size());
        int end = Math.min((start + pageable.getPageSize()), friends.size());
        List<UserDomain> pagedUsers = friends.subList(start, end);
        return new PageImpl<>(pagedUsers, pageable, friends.size()) ;
    }

    private List<UserDomain> getFriendSuggest(long userId){
        UserDomain userCurrent = userDatabasePort.findById(userId);
        List<UserDomain> userList = userDatabasePort.getAllUser();
        List<UserDomain> friends = relationshipDatabasePort.getListFriend(userId);
        friends.add(userCurrent);
        userList.removeAll(friends);
        return userList.stream()
                .collect(Collectors.toMap(
                        user -> user,
                        user -> calculateScore(userCurrent, user)))
                .entrySet().stream()
                .sorted(Map.Entry.<UserDomain, Integer>comparingByValue().reversed())
                .limit(30)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    private int calculateScore(UserDomain userCurrent, UserDomain user) {
        int score = getListMutualFriends(userCurrent.getId(), user.getId()).size();
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

    private List<UserDomain> getListMutualFriends(long userId1, long userId2) {
        List<UserDomain> friends1 = relationshipDatabasePort.getListFriend(userId1);
        List<UserDomain> friends2 = relationshipDatabasePort.getListFriend(userId2);
        HashSet<UserDomain> set1 = new HashSet<>(friends1);
        HashSet<UserDomain> set2 = new HashSet<>(friends2);
        set1.retainAll(set2);
        return new ArrayList<>(set1);
    }
}
