package com.example.socialnetwork.infrastructure.adapter;

import com.example.socialnetwork.common.constant.ERelationship;
import com.example.socialnetwork.common.mapper.RelationshipMapper;
import com.example.socialnetwork.common.mapper.UserMapper;
import com.example.socialnetwork.domain.model.RelationshipDomain;
import com.example.socialnetwork.domain.model.UserDomain;
import com.example.socialnetwork.domain.port.spi.RelationshipDatabasePort;
import com.example.socialnetwork.exception.custom.NotFoundException;
import com.example.socialnetwork.infrastructure.entity.Relationship;
import com.example.socialnetwork.infrastructure.repository.RelationshipRepository;
import com.example.socialnetwork.infrastructure.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RelationshipDatabaseAdapter implements RelationshipDatabasePort {
    private final RelationshipRepository relationshipRepository;
    private final RelationshipMapper relationshipMapper;
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    @Transactional
    public RelationshipDomain find(long userId, long friendId) {
        Relationship relationship = relationshipRepository.findByUser_IdAndFriend_Id(userId, friendId);
        return relationshipMapper.toRelationshipDomain(relationship);
    }

    @Override
    @Transactional
    public List<RelationshipDomain> getListSendRequest(long userId) {
        return relationshipMapper.toRelationshipDomain(relationshipRepository.findByUser_IdAndRelation(userId, ERelationship.PENDING));
    }

    @Override
    @Transactional
    public List<RelationshipDomain> getListReceiveRequest(long userId) {
        return relationshipMapper.toRelationshipDomain(relationshipRepository.findByFriend_IdAndRelation(userId, ERelationship.PENDING));
    }

    @Override
    @Transactional
    public void deleteRequest(long senderId, long receiverId) {
        Relationship relationship = relationshipRepository.findByUser_IdAndFriend_Id(senderId, receiverId);
        relationshipRepository.delete(relationship);
    }

    @Override
    @Transactional
    public List<UserDomain> getListFriend(long userId) {
        return userMapper.toUserDomains(relationshipRepository.getListUserWithRelation(userId, ERelationship.FRIEND));
    }

    @Override
    @Transactional
    public List<UserDomain> getListBlock(long userId) {
        return userMapper.toUserDomains(relationshipRepository.getListUserWithRelation(userId, ERelationship.BLOCK));
    }

    @Override
    @Transactional
    public List<UserDomain> findFriendByKeyWord(long userId, String keyWord) {
        return userMapper.toUserDomains(relationshipRepository.getListFriendByKeyWord(userId, keyWord));
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
