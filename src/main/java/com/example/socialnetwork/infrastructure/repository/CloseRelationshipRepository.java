package com.example.socialnetwork.infrastructure.repository;

import com.example.socialnetwork.infrastructure.entity.CloseRelationship;
import com.example.socialnetwork.infrastructure.entity.CommentReaction;
import com.example.socialnetwork.infrastructure.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CloseRelationshipRepository extends JpaRepository<CloseRelationship, Long> {
    Optional<CloseRelationship> findCloseRelationshipByUserIdAndTargetUserId(Long userId, Long targetUserId);
    Page<CloseRelationship> findAll(Specification<CloseRelationship> spec, Pageable pageable);
    void deleteByTargetUserId(Long targetUserId);

    @Query("SELECT u FROM User u " +
            "INNER JOIN CloseRelationship r ON r.user = u OR r.targetUser = u " +
            "WHERE (r.user.id = :userId OR r.targetUser.id = :userId) " +
            "AND u.id <> :userId")
    List<User> findCloseRelationshipByUser(@Param("userId") long userId);
}
