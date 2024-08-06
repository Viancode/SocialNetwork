package com.example.socialnetwork.infrastructure.repository;

import com.example.socialnetwork.infrastructure.entity.Post;
import com.example.socialnetwork.infrastructure.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long>, PagingAndSortingRepository<Post, Long> {
    Page<Post> findAll(Specification<Post> spec, Pageable pageable);

    @Query("SELECT p FROM Post p " +
            "WHERE p.user IN :users " +
            "AND p.visibility <> 'PRIVATE' " +
            "ORDER BY p.lastComment DESC, p.createdAt DESC ")
    List<Post> findByListUser(@Param("users") List<User> users);
}
