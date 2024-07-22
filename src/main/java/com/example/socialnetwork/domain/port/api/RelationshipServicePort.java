package com.example.socialnetwork.domain.port.api;

import com.example.socialnetwork.application.response.FriendSuggestionsResponse;
import com.example.socialnetwork.application.response.ListFriendResponse;
import com.example.socialnetwork.application.response.MakeFriendResponse;
import com.example.socialnetwork.common.constant.ERelationship;
import com.example.socialnetwork.domain.model.RelationshipDomain;
import com.example.socialnetwork.domain.model.UserDomain;
import org.aspectj.asm.internal.Relationship;

import java.util.List;

public interface RelationshipServicePort {
    boolean deleteRelationship(long userId, long friendId);

    boolean updateRelationship(long userId, long friendId, String relationship);

    boolean sendRequestMakeFriendship(long userId, long friendId);

    boolean deleteRequestMakeFriendship(long userId, long friendId);

    boolean acceptRequestMakeFriendship(long userId, long friendId);

    boolean refuseRequestMakeFriendship(long userId, long friendId);

    List<RelationshipDomain> getListRequest(long userId);

    List<ListFriendResponse> getListFriend(long userId);

    List<ListFriendResponse> search(long userId, String name);

    List<FriendSuggestionsResponse> viewSuggest(long userId);
}
