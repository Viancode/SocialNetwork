package com.example.socialnetwork.domain.port.api;

import com.example.socialnetwork.application.response.FriendSuggestionResponse;
import com.example.socialnetwork.application.response.SearchFriendResponse;
import com.example.socialnetwork.common.constant.ERelationship;
import com.example.socialnetwork.domain.model.SuggestionDomain;
import com.example.socialnetwork.domain.model.UserDomain;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface RelationshipServicePort {
    void deleteRelationship(long friendId);

    void sendRequestMakeFriendship(long userId);

    void deleteRequestMakeFriendship(long userId);

    void acceptRequestMakeFriendship(long userId);

    void refuseRequestMakeFriendship(long userId);

    void block(long friendId);

    void unblock(long friendId);

    Page<UserDomain> findFriend(int page, int pageSize, String keyWord);

    Page<UserDomain> getListReceiveRequest(int page, int pageSize);

    Page<UserDomain> getListSendRequest(int page, int pageSize);

    Page<UserDomain> getListFriend(int page, int pageSize, long userId, String sortDirection, String sortBy);

    Page<UserDomain> getListBlock(int page, int pageSize, String sortDirection, String sortBy);

    Page<FriendSuggestionResponse> getFriendSuggestions(int page, int pageSize);

    Page<SearchFriendResponse> searchUser(int page, int pageSize, String keyWord);
}
