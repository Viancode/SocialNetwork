package com.example.socialnetwork.infrastructure.repository;


import com.example.socialnetwork.common.constant.ERelationship;
import com.example.socialnetwork.domain.model.RelationshipDomain;
import com.example.socialnetwork.infrastructure.entity.Relationship;
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

    @Query("SELECT r FROM Relationship r " +
            "INNER JOIN User u1 ON u1.id = r.user.id OR u1.id = r.friend.id " +
            "WHERE r.relation = :relation " +
            "AND (r.friend.id = :userId OR r.user.id = :userId) ")
    List<Relationship> getListFriend(@Param("userId") long userId, ERelationship relation);
}
