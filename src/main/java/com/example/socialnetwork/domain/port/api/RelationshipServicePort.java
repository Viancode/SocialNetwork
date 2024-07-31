package com.example.socialnetwork.domain.port.api;

import com.example.socialnetwork.common.constant.ERelationship;
import com.example.socialnetwork.domain.model.RelationshipDomain;
import com.example.socialnetwork.domain.model.UserDomain;

import java.util.List;

public interface RelationshipServicePort {
    ERelationship getRelationship(long sourceUserID, long targetUserID);

    void deleteRelationship(long friendId);

    void sendRequestMakeFriendship(long userId);

    void deleteRequestMakeFriendship(long userId);

    void acceptRequestMakeFriendship(long userId);

    void refuseRequestMakeFriendship(long userId);

    void block(long friendId);

    List<UserDomain> findFriend(long userId, String keyWord);

    List<UserDomain> getListReceiveRequest();

    List<UserDomain> getListSendRequest();

    List<UserDomain> getListFriend(long userId);

    List<UserDomain> getListBlock();

    List<UserDomain> getFriendSuggestions(long userId);
}
