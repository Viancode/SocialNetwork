package com.example.socialnetwork.infrastructure.repository;

import com.example.socialnetwork.infrastructure.entity.PostReaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostReactionRepository extends JpaRepository<PostReaction, Long> {
    Page<PostReaction> findAll(Specification<PostReaction> spec, Pageable pageable);
}
