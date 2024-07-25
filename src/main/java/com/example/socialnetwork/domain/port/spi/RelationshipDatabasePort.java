package com.example.socialnetwork.domain.port.spi;

import com.example.socialnetwork.common.constant.ERelationship;
import com.example.socialnetwork.domain.model.RelationshipDomain;
import com.example.socialnetwork.domain.model.UserDomain;

import java.util.List;

public interface RelationshipDatabasePort {

    RelationshipDomain find(long senderId, long receiverId);

    List<RelationshipDomain> getListSendRequest(long userId);

    List<RelationshipDomain> getListReceiveRequest(long userId);

    List<UserDomain> getListFriend(long userId);

    List<UserDomain> findFriendByKeyWord(long userId, String keyWord);

    void deleteFriend(long userId, long friendId);

    void deleteRequest(long senderId, long receiverId);

    void updateRelation(long senderId, long receiverId, ERelationship relationship);

    void createRelationship(long senderId, long receiverId, ERelationship relation);
}
