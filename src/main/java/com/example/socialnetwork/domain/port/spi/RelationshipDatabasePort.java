package com.example.socialnetwork.domain.port.spi;

import com.example.socialnetwork.common.constant.ERelationship;
import com.example.socialnetwork.domain.model.RelationshipDomain;
import com.example.socialnetwork.domain.model.SuggestionDomain;
import com.example.socialnetwork.domain.model.UserDomain;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;

public interface RelationshipDatabasePort {

    Optional<RelationshipDomain> find(long senderId, long receiverId);

    ERelationship getRelationship(long senderId, long receiverId);

    Page<UserDomain> getListSendRequest(int page, int pageSize, long userId);

    Page<UserDomain> getListReceiveRequest(int page, int pageSize, long userId);

    Page<UserDomain> getListFriend(int page, int pageSize, long userId, Sort sort);

    List<UserDomain> getListFriend(long userId);

    Page<UserDomain> getListBlock(int page, int pageSize, long userId, Sort sort);

    List<UserDomain> getListBlock(long userId);

    Page<UserDomain> findFriendByKeyWord(int page, int pageSize, long userId, String keyWord);

    List<UserDomain> findFriendByKeyWord(long userId, String keyWord);

    List<SuggestionDomain> getListSuggestionUser(long userId);

    List<SuggestionDomain> searchUserByKeyWord(long userId, String keyWord);

    void deleteFriend(long userId, long friendId);

    void deleteRequest(long senderId, long receiverId);

    void unblock(long userId, long blockId);

    void updateRelation(long senderId, long receiverId, ERelationship relationship);

    void createRelationship(long senderId, long receiverId, ERelationship relation);
}
