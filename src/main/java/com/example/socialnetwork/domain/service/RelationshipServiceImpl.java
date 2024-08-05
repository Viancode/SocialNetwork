package com.example.socialnetwork.domain.service;

import com.example.socialnetwork.application.response.FriendSuggestionResponse;
import com.example.socialnetwork.application.response.SearchFriendResponse;
import com.example.socialnetwork.common.constant.ERelationship;
import com.example.socialnetwork.common.constant.Visibility;
import com.example.socialnetwork.common.mapper.SuggestionMapper;
import com.example.socialnetwork.common.publisher.CustomEventPublisher;
import com.example.socialnetwork.domain.model.RelationshipDomain;
import com.example.socialnetwork.domain.model.SuggestionDomain;
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

@RequiredArgsConstructor
public class RelationshipServiceImpl implements RelationshipServicePort {
    private final RelationshipDatabasePort relationshipDatabasePort;
    private final UserDatabasePort userDatabasePort;
    private final CustomEventPublisher customEventPublisher;
    private final SuggestionMapper suggestionMapper;

    @Override
    public ERelationship getRelationship(long sourceUserID, long targetUserID) {
        RelationshipDomain relationshipDomain = relationshipDatabasePort.find(sourceUserID, targetUserID).orElse(null);
        if (relationshipDomain == null) {
//            throw new NotFoundException("Not found relationship");
            return null;
        }
        return relationshipDomain.getRelation();
    }

    @Override
    public void deleteRelationship(long friendId) {
        long userId = getCurrentUser();
        checkFriend(friendId);
        relationshipDatabasePort.deleteFriend(userId, friendId);
        customEventPublisher.publishFriendDeletedEvent(userId, friendId);
    }

    @Override
    public void sendRequestMakeFriendship(long userId) {
        long senderId = getCurrentUser();
        checkFriend(userId);
        RelationshipDomain relationshipDomain = relationshipDatabasePort.find(senderId, userId).orElse(null);
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
        RelationshipDomain relationshipDomain = relationshipDatabasePort.find(senderId, userId).orElse(null);
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
        RelationshipDomain relationshipDomain = relationshipDatabasePort.find(userId, receiverId).orElse(null);
        if (relationshipDomain == null) {
            throw new NotFoundException("Friend request not found");
        } else if (relationshipDomain.getRelation() == ERelationship.PENDING && relationshipDomain.getUser().getId() == userId) {
            relationshipDatabasePort.updateRelation(userId, receiverId, ERelationship.FRIEND);
            customEventPublisher.publishFriendRequestAcceptedEvent(receiverId, userId);
        } else if (relationshipDomain.getRelation() == ERelationship.BLOCK) {
            throw new RelationshipException("Cannot accept friend request");
        }
    }

    @Override
    public void refuseRequestMakeFriendship(long userId) {
        long receiverId = getCurrentUser();
        checkFriend(userId);
        RelationshipDomain relationshipDomain = relationshipDatabasePort.find(userId, receiverId).orElse(null);
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
        RelationshipDomain relationshipDomain = relationshipDatabasePort.find(userId, friendId).orElse(null);
        customEventPublisher.publishBlockedEvent(userId, friendId);
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
            RelationshipDomain relationshipDomain = relationshipDatabasePort.find(userId, currentUserId).orElse(null);
            if (visibility == Visibility.PRIVATE || (relationshipDomain != null && relationshipDomain.getRelation() != ERelationship.FRIEND && visibility == Visibility.FRIEND)) {
                List<UserDomain> mutualFriends = getListMutualFriends(userId, currentUserId);
                int start = Math.min((int) pageable.getOffset(), mutualFriends.size());
                int end = Math.min((start + pageable.getPageSize()), mutualFriends.size());
                List<UserDomain> pagedUsers = mutualFriends.subList(start, end);
                return new PageImpl<>(pagedUsers, pageable, mutualFriends.size());
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
        return relationshipDatabasePort.getListBlock(page, pageSize, currentUserId, sort);
    }

    @Override
    public Page<FriendSuggestionResponse> getFriendSuggestions(int page, int pageSize) {
        long userId = getCurrentUser();
        List<SuggestionDomain> suggestionDomains = relationshipDatabasePort.getListSuggestionUser(userId);
        List<FriendSuggestionResponse> friendSuggestions = suggestionMapper.toFriendSuggestionResponses(suggestionDomains);
        var pageable = PageRequest.of(page - 1, pageSize);
        int start = Math.min((int) pageable.getOffset(), friendSuggestions.size());
        int end = Math.min((start + pageable.getPageSize()), friendSuggestions.size());
        List<FriendSuggestionResponse> pagedUsers = friendSuggestions.subList(start, end);
        return new PageImpl<>(pagedUsers, pageable, friendSuggestions.size());
    }

    @Override
    public Page<SearchFriendResponse> searchUser(int page, int pageSize, String keyWord) {
        long userId = getCurrentUser();
        List<SuggestionDomain> suggestionDomains = relationshipDatabasePort.searchUserByKeyWord(userId, keyWord);
        List<SearchFriendResponse> searchFriendResponses = suggestionMapper.toSearchFriendResponses(suggestionDomains);
        var pageable = PageRequest.of(page - 1, pageSize);
        int start = Math.min((int) pageable.getOffset(), searchFriendResponses.size());
        int end = Math.min((start + pageable.getPageSize()), searchFriendResponses.size());
        List<SearchFriendResponse> pagedUsers = searchFriendResponses.subList(start, end);
        return new PageImpl<>(pagedUsers, pageable, searchFriendResponses.size());
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
