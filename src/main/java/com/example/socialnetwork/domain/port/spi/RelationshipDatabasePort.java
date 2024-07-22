package com.example.socialnetwork.domain.port.spi;

import com.example.socialnetwork.common.constant.ERelationship;
import com.example.socialnetwork.domain.model.RelationshipDomain;
import com.example.socialnetwork.domain.model.UserDomain;

import java.util.List;

public interface RelationshipDatabasePort {
    RelationshipDomain changeStatusAndSave(long userId, long friendId, int status);

    RelationshipDomain find(long userId, long friendId);

    List<RelationshipDomain> getListRequest(long userId);

    List<RelationshipDomain> getListFriend(long userId);

    boolean deleteRelationship(long userId, long friendId);

    boolean updateRelationship(long userId, long friendId, String relationship);

    List<RelationshipDomain> search(long userId, String name);
}
