package com.example.socialnetwork.infrastructure.repository;

import com.example.socialnetwork.infrastructure.entity.CloseRelationship;
import com.example.socialnetwork.infrastructure.entity.CommentReaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CloseRelationshipRepository extends JpaRepository<CloseRelationship, Long> {
    Optional<CloseRelationship> findCloseRelationshipByUserIdAndTargetUserId(Long userId, Long targetUserId);
    Page<CloseRelationship> findAll(Specification<CloseRelationship> spec, Pageable pageable);
    void deleteByTargetUserId(Long targetUserId);
}
