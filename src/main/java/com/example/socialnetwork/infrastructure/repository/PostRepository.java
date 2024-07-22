package com.example.socialnetwork.infrastructure.repository;

import com.example.socialnetwork.infrastructure.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface PostRepository extends JpaRepository<Post, Long>, PagingAndSortingRepository<Post, Long> {
    List<Post> findAllByUserId(Long userId);

    Page<Post> findByUserIdAndIsDeletedFalse(Long userId, Pageable pageable);
    Page<Post> findByUserIdAndVisibilityAndIsDeletedFalse(Long userId,String visibility, Pageable pageable);

}
