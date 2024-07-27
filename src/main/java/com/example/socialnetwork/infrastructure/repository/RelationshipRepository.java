package com.example.socialnetwork.infrastructure.repository;


import com.example.socialnetwork.common.constant.ERelationship;
import com.example.socialnetwork.infrastructure.entity.Relationship;
import com.example.socialnetwork.infrastructure.entity.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@EnableJpaRepositories
public interface RelationshipRepository extends JpaRepository<Relationship, Long> {
    Relationship findByUser_IdAndFriend_Id(long userId, long friend_id);

    List<Relationship> findByFriend_IdAndRelation(long userId, ERelationship relation);

    List<Relationship> findByUser_IdAndRelation(long userId, ERelationship relation);

    @EntityGraph(attributePaths = {"user"})
    @Query("SELECT u FROM User u " +
            "INNER JOIN Relationship r ON r.user.id = u.id OR r.friend.id = u.id " +
            "WHERE r.relation = 'FRIEND' " +
            "AND (r.friend.id = :userId OR r.user.id = :userId) " +
            "AND u.id <> :userId")
    List<User> getListFriend(@Param("userId") long userId);

    @EntityGraph(attributePaths = {"user"})
    @Query("SELECT u FROM User u " +
            "INNER JOIN Relationship r ON r.user.id = u.id OR r.friend.id = u.id " +
            "WHERE r.relation = 'FRIEND' " +
            "AND (r.friend.id = :userId OR r.user.id = :userId) " +
            "AND u.id <> :userId " +
            "AND (u.email LIKE %:keyWord% OR u.username LIKE %:keyWord%)")
    List<User> getListFriendByKeyWord(@Param("userId") long userId,@Param("keyWord") String keyWord);
}
