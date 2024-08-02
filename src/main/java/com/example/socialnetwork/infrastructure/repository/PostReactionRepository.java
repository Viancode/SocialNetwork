package com.example.socialnetwork.infrastructure.repository;

import com.example.socialnetwork.infrastructure.entity.PostReaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PostReactionRepository extends JpaRepository<PostReaction, Long> {
    Page<PostReaction> findAll(Specification<PostReaction> spec, Pageable pageable);
    Optional<PostReaction> findByUserIdAndPostIdAndReactionType(Long userId, Long postId, String reactionType);
    Optional<PostReaction> findByUserIdAndPostId(Long userId, Long postId);
}
