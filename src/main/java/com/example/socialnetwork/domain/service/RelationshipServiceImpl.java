package com.example.socialnetwork.domain.service;

import com.example.socialnetwork.application.response.CloseFriendResponse;
import com.example.socialnetwork.application.response.FriendSuggestionResponse;
import com.example.socialnetwork.application.response.SearchFriendResponse;
import com.example.socialnetwork.common.constant.ERelationship;
import com.example.socialnetwork.common.constant.Visibility;
import com.example.socialnetwork.common.mapper.CustomSuggestionMapper;
import com.example.socialnetwork.common.mapper.CustomUserMapper;
import com.example.socialnetwork.common.mapper.SuggestionMapper;
import com.example.socialnetwork.common.util.SecurityUtil;
import com.example.socialnetwork.domain.publisher.CustomEventPublisher;
import com.example.socialnetwork.domain.model.RelationshipDomain;
import com.example.socialnetwork.domain.model.SuggestionDomain;
import com.example.socialnetwork.domain.model.UserDomain;
import com.example.socialnetwork.domain.port.api.RelationshipServicePort;
import com.example.socialnetwork.domain.port.spi.CloseRelationshipDatabasePort;
import com.example.socialnetwork.domain.port.spi.RelationshipDatabasePort;
import com.example.socialnetwork.domain.port.spi.UserDatabasePort;
import com.example.socialnetwork.exception.custom.NotFoundException;
import com.example.socialnetwork.exception.custom.RelationshipException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;

import java.util.*;

@RequiredArgsConstructor
public class RelationshipServiceImpl implements RelationshipServicePort {
    private final RelationshipDatabasePort relationshipDatabasePort;
    private final UserDatabasePort userDatabasePort;
    private final CloseRelationshipDatabasePort closeRelationshipDatabasePort;
    private final CustomEventPublisher customEventPublisher;
    private final SuggestionMapper suggestionMapper;
    private final CustomUserMapper customUserMapper;
    private final CustomSuggestionMapper customSuggestionMapper;

    @Override
    public void deleteRelationship(long friendId) {
        long userId = SecurityUtil.getCurrentUserId();
        checkFriend(friendId);
        relationshipDatabasePort.deleteFriend(userId, friendId);
        closeRelationshipDatabasePort.deleteCloseRelationship(friendId);
        customEventPublisher.publishFriendDeletedEvent(userId, friendId);
    }

    @Override
    public void sendRequestMakeFriendship(long userId) {
        long senderId = SecurityUtil.getCurrentUserId();
        checkFriend(userId);
        RelationshipDomain relationshipDomain = relationshipDatabasePort.find(senderId, userId).orElse(null);
        if (senderId == userId) {
            throw new RelationshipException("Cannot send friend request for yourself");
        } else if (relationshipDomain == null) {
            relationshipDatabasePort.createRelationship(senderId, userId, ERelationship.PENDING);
        } else if (relationshipDomain.getRelation() == ERelationship.FRIEND)
            throw new RelationshipException("Cannot send friend request because you two are already friends");
        else {
            throw new RelationshipException("Cannot send friend request");
        }
    }

    @Override
    public void deleteRequestMakeFriendship(long userId) {
        long senderId = SecurityUtil.getCurrentUserId();
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
        long receiverId = SecurityUtil.getCurrentUserId();
        checkFriend(userId);
        RelationshipDomain relationshipDomain = relationshipDatabasePort.find(userId, receiverId).orElse(null);
        if (relationshipDomain == null) {
            throw new NotFoundException("Friend request not found");
        } else if (relationshipDomain.getRelation() == ERelationship.PENDING && relationshipDomain.getUser().getId() == userId) {
            customEventPublisher.publishFriendRequestAcceptedEvent(receiverId, userId);
            relationshipDatabasePort.updateRelation(userId, receiverId, ERelationship.FRIEND);
        } else if (relationshipDomain.getRelation() == ERelationship.BLOCK) {
            throw new RelationshipException("Cannot accept friend request");
        }
    }

    @Override
    public void refuseRequestMakeFriendship(long userId) {
        long receiverId = SecurityUtil.getCurrentUserId();
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
        long userId = SecurityUtil.getCurrentUserId();
        checkFriend(friendId);
        RelationshipDomain relationshipDomain = relationshipDatabasePort.find(userId, friendId).orElse(null);
        customEventPublisher.publishBlockedEvent(userId, friendId);
        if (userId != friendId) {
            if (relationshipDomain == null) {
                relationshipDatabasePort.createRelationship(userId, friendId, ERelationship.BLOCK);
            } else {
                relationshipDatabasePort.updateRelation(userId, friendId, ERelationship.BLOCK);
            }
            closeRelationshipDatabasePort.deleteCloseRelationship(friendId);
        } else {
            throw new RelationshipException("Cannot block yourself");
        }
    }

    @Override
    public void unblock(long friendId) {
        long userId = SecurityUtil.getCurrentUserId();
        checkFriend(friendId);
        RelationshipDomain relationshipDomain = relationshipDatabasePort.find(userId, friendId).orElse(null);
        customEventPublisher.publishUnblockedEvent(userId, friendId);
        if (relationshipDomain != null && relationshipDomain.getUser().getId() == userId && relationshipDomain.getRelation() == ERelationship.BLOCK) {
            relationshipDatabasePort.unblock(userId, friendId);
            customEventPublisher.publishUnblockedEvent(userId, friendId);
        } else {
            throw new RelationshipException("You do not block this user");
        }
    }

    @Override
    public int getNumberOfFriend() {
        long userId = SecurityUtil.getCurrentUserId();
        return relationshipDatabasePort.getListFriend(userId).size();
    }

    @Override
    public Page<UserDomain> findFriend(int page, int pageSize, String keyWord) {
        long userId = SecurityUtil.getCurrentUserId();
        return relationshipDatabasePort.findFriendByKeyWord(page, pageSize, userId, keyWord);
    }

    @Override
    public Page<UserDomain> getListReceiveRequest(int page, int pageSize) {
        long userId = SecurityUtil.getCurrentUserId();
        return relationshipDatabasePort.getListReceiveRequest(page, pageSize, userId);
    }

    @Override
    public Page<UserDomain> getListSendRequest(int page, int pageSize) {
        long userId = SecurityUtil.getCurrentUserId();
        return relationshipDatabasePort.getListSendRequest(page, pageSize, userId);
    }

    @Override
    public Page<CloseFriendResponse> getListFriend(int page, int pageSize, String sortDirection, String sortBy) {
        long currentUserId = SecurityUtil.getCurrentUserId();
        Sort.Direction direction = sortDirection.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Sort sort = Sort.by(direction, sortBy);
        List<UserDomain> userDomains = relationshipDatabasePort.getListFriend(currentUserId);
        List<CloseFriendResponse> closeFriendResponses = new ArrayList<>();
        for (UserDomain userDomain : userDomains) {
            closeFriendResponses.add(customUserMapper.toCloseFriendResponse(userDomain));
        }
        return getPage(page, pageSize, closeFriendResponses, sort);
    }

    @Override
    public Page<UserDomain> getListBlock(int page, int pageSize, String sortDirection, String sortBy) {
        long currentUserId = SecurityUtil.getCurrentUserId();
        Sort.Direction direction = sortDirection.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Sort sort = Sort.by(direction, sortBy);
        return relationshipDatabasePort.getListBlock(page, pageSize, currentUserId, sort);
    }

    @Override
    public Page<FriendSuggestionResponse> getFriendSuggestions(int page, int pageSize) {
        long userId = SecurityUtil.getCurrentUserId();
        List<SuggestionDomain> suggestionDomains = relationshipDatabasePort.getListSuggestionUser(userId);
        List<FriendSuggestionResponse> friendSuggestions = suggestionMapper.toFriendSuggestionResponses(suggestionDomains);
        return getPage(page, pageSize, friendSuggestions);
    }

    @Override
    public Page<SearchFriendResponse> searchUser(int page, int pageSize, String keyWord) {
        long userId = SecurityUtil.getCurrentUserId();
        List<SuggestionDomain> suggestionDomains = relationshipDatabasePort.searchUserByKeyWord(userId, keyWord);
        List<SearchFriendResponse> searchFriendResponses = customSuggestionMapper.toSearchFriendResponses(suggestionDomains);
        return getPage(page, pageSize, searchFriendResponses);
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

    private <T> PageImpl<T> getPage(int page, int pageSize, List<T> list) {
        var pageable = PageRequest.of(page - 1, pageSize);
        int start = Math.min((int) pageable.getOffset(), list.size());
        int end = Math.min((start + pageable.getPageSize()), list.size());
        List<T> paged = list.subList(start, end);
        return new PageImpl<>(paged, pageable, list.size());
    }

    private <T> PageImpl<T> getPage(int page, int pageSize, List<T> list, Sort sort) {
        var pageable = PageRequest.of(page - 1, pageSize, sort);
        int start = Math.min((int) pageable.getOffset(), list.size());
        int end = Math.min((start + pageable.getPageSize()), list.size());
        List<T> paged = list.subList(start, end);
        return new PageImpl<>(paged, pageable, list.size());
    }
}
