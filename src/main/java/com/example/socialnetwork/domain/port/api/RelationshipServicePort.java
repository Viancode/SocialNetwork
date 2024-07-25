package com.example.socialnetwork.domain.port.api;

import com.example.socialnetwork.application.response.ListFriendResponse;
import com.example.socialnetwork.common.constant.ERelationship;
import com.example.socialnetwork.domain.model.RelationshipDomain;

import java.util.List;

public interface RelationshipServicePort {
    ERelationship getRelationship(long sourceUserID, long targetUserID);
    void deleteRelationship(long friendId);

    void sendRequestMakeFriendship(long userId);

    void deleteRequestMakeFriendship(long userId);

    void acceptRequestMakeFriendship(long userId);

    void refuseRequestMakeFriendship(long userId);

    void block(long friendId);

    List<RelationshipDomain> getListReceiveRequest();

    List<RelationshipDomain> getListSendRequest();

    List<ListFriendResponse> getListFriend(long userId);
}
