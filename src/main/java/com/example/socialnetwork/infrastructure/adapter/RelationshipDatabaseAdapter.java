package com.example.socialnetwork.infrastructure.adapter;

import com.example.socialnetwork.common.constant.ERelationship;
import com.example.socialnetwork.common.mapper.RelationshipMapper;
import com.example.socialnetwork.common.mapper.SuggestionMapper;
import com.example.socialnetwork.common.mapper.UserMapper;
import com.example.socialnetwork.domain.model.RelationshipDomain;
import com.example.socialnetwork.domain.model.SuggestionDomain;
import com.example.socialnetwork.domain.model.UserDomain;
import com.example.socialnetwork.domain.port.spi.RelationshipDatabasePort;
import com.example.socialnetwork.exception.custom.NotFoundException;
import com.example.socialnetwork.infrastructure.entity.Relationship;
import com.example.socialnetwork.infrastructure.entity.Suggestion;
import com.example.socialnetwork.infrastructure.entity.User;
import com.example.socialnetwork.infrastructure.repository.RelationshipRepository;
import com.example.socialnetwork.infrastructure.repository.SuggestionRepository;
import com.example.socialnetwork.infrastructure.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class RelationshipDatabaseAdapter implements RelationshipDatabasePort {
    private final RelationshipRepository relationshipRepository;
    private final RelationshipMapper relationshipMapper;
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final SuggestionRepository suggestionRepository;
    private final SuggestionMapper suggestionMapper;

    @Override
    @Transactional
    public Optional<RelationshipDomain> find(long userId, long friendId) {
        Relationship relationship = relationshipRepository.findByUser_IdAndFriend_Id(userId, friendId);
        return Optional.ofNullable(relationshipMapper.toRelationshipDomain(relationship));
    }

    @Override
    @Transactional
    public Page<UserDomain> getListSendRequest(int page, int pageSize, long userId) {
        var pageable = PageRequest.of(page - 1, pageSize, Sort.by("friend.createdAt"));
        return relationshipRepository.findByUser_IdAndRelation(userId, ERelationship.PENDING, pageable).map(userMapper::toUserDomain);
    }

    @Override
    @Transactional
    public Page<UserDomain> getListReceiveRequest(int page, int pageSize, long userId) {
        var pageable = PageRequest.of(page - 1, pageSize, Sort.by("friend.createdAt"));
        return relationshipRepository.findByFriend_IdAndRelation(userId, ERelationship.PENDING, pageable).map(userMapper::toUserDomain);
    }

    @Override
    @Transactional
    public void deleteRequest(long senderId, long receiverId) {
        Relationship relationship = relationshipRepository.findByUser_IdAndFriend_Id(senderId, receiverId);
        relationshipRepository.delete(relationship);
    }

    @Override
    @Transactional
    public void unblock(long userId, long blockId) {
        Relationship relationship = relationshipRepository.getRelationship(userId, blockId);
        relationshipRepository.delete(relationship);
    }

    @Override
    @Transactional
    public Page<UserDomain> getListFriend(int page, int pageSize, long userId, Sort sort) {
        var pageable = PageRequest.of(page - 1, pageSize, sort);
        return relationshipRepository.getListUserWithRelation(userId, ERelationship.FRIEND, pageable).map(userMapper::toUserDomain);
    }

    @Override
    @Transactional
    public List<UserDomain> getListFriend(long userId) {
        return userMapper.toUserDomains(relationshipRepository.getListUserWithRelation(userId, ERelationship.FRIEND));
    }

    @Override
    @Transactional
    public Page<UserDomain> getListBlock(int page, int pageSize, long userId, Sort sort) {
        var pageable = PageRequest.of(page - 1, pageSize, sort);
        return relationshipRepository.getListBlock(userId, pageable).map(userMapper::toUserDomain);
    }

    @Override
    public List<UserDomain> getListBlock(long userId) {
        return userMapper.toUserDomains(relationshipRepository.getListUserWithRelation(userId, ERelationship.BLOCK));
    }

    @Override
    @Transactional
    public Page<UserDomain> findFriendByKeyWord(int page, int pageSize, long userId, String keyWord) {
        var pageable = PageRequest.of(page - 1, pageSize, Sort.by("username"));
        return relationshipRepository.getListFriendByKeyWord(userId, keyWord, pageable).map(userMapper::toUserDomain);
    }

    @Override
    @Transactional
    public List<UserDomain> findFriendByKeyWord(long userId, String keyWord) {
        return userMapper.toUserDomains(relationshipRepository.getListFriendByKeyWord(userId, keyWord));
    }

    @Override
    @Transactional
    public List<SuggestionDomain> getListSuggestionUser(long userId) {
        User user1 = userRepository.findById(userId).orElseThrow();
        List<Suggestion> suggestions = suggestionRepository.findByUserOrFriend(user1);
        return suggestions.stream().map(suggestionMapper::toSuggestionDomain).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteFriend(long userId, long friendId) {
        Relationship relationship = relationshipRepository.findByUser_IdAndFriend_Id(userId, friendId);
        if(relationship == null || relationship.getRelation() == ERelationship.PENDING) {
            throw new NotFoundException("you two are not friends");
        }else if(relationship.getRelation() == ERelationship.BLOCK) {
            throw new NotFoundException("You have been blocked by this person");
        }else {
            relationshipRepository.delete(relationship);
        }
    }

    @Override
    @Transactional
    public List<SuggestionDomain> searchUserByKeyWord(long userId, String keyWord) {
        User user1 = userRepository.findById(userId).orElseThrow();
        List<Suggestion> searchUsers = suggestionRepository.searchUser(user1);
        List<Suggestion> unsuitableSearchUsers = new ArrayList<>();
        for(Suggestion suggestion : searchUsers) {
            User user2 = suggestion.getUser();
            if(user1 == user2) user2 = suggestion.getFriend();
            if (!user2.getEmail().toLowerCase().contains(keyWord.toLowerCase()) || !user2.getUsername().toLowerCase().contains(keyWord.toLowerCase())) {
                unsuitableSearchUsers.add(suggestion);
            }
        }
        searchUsers.removeAll(unsuitableSearchUsers);
        return searchUsers.stream().map(suggestionMapper::toSuggestionDomain).collect(Collectors.toList());
    }


    @Override
    @Transactional
    public void updateRelation(long userId, long friendId, ERelationship eRelationship) {
        Relationship relationship = relationshipRepository.findByUser_IdAndFriend_Id(userId, friendId);
        if(relationship == null) {
            throw new NotFoundException("Not found relationship");
        }
        relationship.setRelation(eRelationship);
        relationshipRepository.save(relationship);
    }

    @Override
    @Transactional
    public void createRelationship(long userId, long friendId, ERelationship relation) {
        Relationship relationship = new Relationship();
        relationship.setUser(userRepository.findUserById(userId).get());
        relationship.setFriend(userRepository.findUserById(friendId).get());
        relationship.setRelation(relation);
        relationship.setCreatedAt(LocalDateTime.now());
        relationshipRepository.save(relationship);
    }
}
