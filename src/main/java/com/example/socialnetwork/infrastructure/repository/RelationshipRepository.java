package com.example.socialnetwork.infrastructure.repository;

import com.example.socialnetwork.infrastructure.entity.Relationship;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RelationshipRepository extends JpaRepository<Relationship, Long> {
    Relationship findByUserIdAndFriendId(Long userId, Long friendId);
}
