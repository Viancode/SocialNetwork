package com.example.socialnetwork.domain.port.api;

import com.example.socialnetwork.application.response.FriendSuggestionsResponse;
import com.example.socialnetwork.application.response.ListFriendResponse;
import com.example.socialnetwork.domain.model.RelationshipDomain;

import java.util.List;

public interface RelationshipServicePort {
    void deleteRelationship(long userId, long friendId);

    void sendRequestMakeFriendship(long senderId, long receiverId);

    void deleteRequestMakeFriendship(long senderId, long receiverId);

    void acceptRequestMakeFriendship(long senderId, long receiverId);

    void refuseRequestMakeFriendship(long senderId, long receiverId);

    void block(long userId, long friendId);

    List<RelationshipDomain> getListRequest(long senderId);

    List<ListFriendResponse> getListFriend(long senderId);
}
