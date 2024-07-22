package com.example.socialnetwork.domain.port.spi;

import com.example.socialnetwork.common.constant.ERelationship;
import com.example.socialnetwork.domain.model.RelationshipDomain;

import java.util.List;

public interface RelationshipDatabasePort {

    RelationshipDomain find(long senderId, long receiverId);

    List<RelationshipDomain> getListRequest(long userId);

    List<RelationshipDomain> getListFriend(long userId);

    void deleteFriend(long userId, long friendId);

    void deleteRequest(long senderId, long receiverId);

    void updateRelation(long senderId, long receiverId, ERelationship relationship);

    void createRelationship(long senderId, long receiverId, ERelationship relation);
}
