package com.example.socialnetwork.infrastructure.repository;

import com.example.socialnetwork.infrastructure.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TagRepository extends JpaRepository<Tag, Long> {
    Optional<Tag> findByTaggedByUserIdAndTaggedUserIdAndPostId(Long userId, Long taggedByUserId, Long postId);
}
