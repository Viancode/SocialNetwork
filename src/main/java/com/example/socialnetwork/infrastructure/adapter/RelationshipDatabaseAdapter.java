package com.example.socialnetwork.infrastructure.adapter;

import com.example.socialnetwork.common.constant.ERelationship;
import com.example.socialnetwork.common.mapper.RelationshipMapper;
import com.example.socialnetwork.common.mapper.UserMapper;
import com.example.socialnetwork.domain.model.RelationshipDomain;
import com.example.socialnetwork.domain.model.UserDomain;
import com.example.socialnetwork.domain.port.spi.RelationshipDatabasePort;
import com.example.socialnetwork.infrastructure.entity.Relationship;
import com.example.socialnetwork.infrastructure.entity.User;
import com.example.socialnetwork.infrastructure.repository.RelationshipRepository;
import com.example.socialnetwork.infrastructure.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RelationshipDatabaseAdapter implements RelationshipDatabasePort {
    private final RelationshipRepository relationshipRepository;
    private final RelationshipMapper relationshipMapper;
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public RelationshipDomain changeStatusAndSave(long userId, long friendId, int status) {
        Relationship relationship = relationshipRepository.findByUser_IdAndFriend_Id(userId, friendId);
        if(relationship != null) {
            if(status == 1) {
                relationship.setCreatedAt(LocalDateTime.now());
                relationship.setRelation(ERelationship.FRIEND.name());
            }
            relationship.setStatus(status);
            return relationshipMapper.toRelationshipDomain(relationshipRepository.save(relationship));
        }
        relationship = new Relationship();
        relationship.setStatus(0);
        relationship.setUser(userRepository.findUserById(userId).get());
        relationship.setFriend(userRepository.findUserById(friendId).get());
        return relationshipMapper.toRelationshipDomain(relationshipRepository.save(relationship));
    }

    @Override
    public RelationshipDomain find(long userId, long friendId) {
        Relationship relationship = relationshipRepository.findByUser_IdAndFriend_Id(userId, friendId);
        return relationshipMapper.toRelationshipDomain(relationship);
    }

    @Override
    public List<RelationshipDomain> getListRequest(long userId) {
        return relationshipMapper.toRelationshipDomain(relationshipRepository.findByFriend_IdAndStatus(userId, 0));
    }

    @Override
    public boolean deleteRelationship(long userId, long friendId) {
        Relationship relationship = relationshipRepository.findByUser_IdAndFriend_Id(userId, friendId);
        relationship = relationship == null ? relationshipRepository.findByUser_IdAndFriend_Id(friendId, userId) : relationship;
        if(relationship == null) {
            return false;
        }
        relationshipRepository.delete(relationship);
        return true;
    }

    @Override
    public List<RelationshipDomain> search(long userId, String name) {
        return relationshipMapper.toRelationshipDomain(relationshipRepository.search(userId, name));
    }

    @Override
    public List<RelationshipDomain> getListFriend(long userId) {
        return relationshipMapper.toRelationshipDomain(relationshipRepository.getListFriend(userId));
    }

    @Override
    public boolean updateRelationship(long userId, long friendId, String eRelationship) {
        Relationship relationship = relationshipRepository.findByUser_IdAndFriend_Id(userId, friendId);
        relationship = (relationship == null) ? relationshipRepository.findByUser_IdAndFriend_Id(friendId, userId) : relationship;
        if(relationship == null) {
            return false;
        }
        relationship.setRelation(eRelationship);
        relationshipRepository.save(relationship);
        return true;
    }
}
