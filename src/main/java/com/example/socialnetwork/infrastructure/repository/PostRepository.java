package com.example.socialnetwork.infrastructure.repository;

import com.example.socialnetwork.common.constant.Visibility;
import com.example.socialnetwork.infrastructure.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface PostRepository extends JpaRepository<Post, Long>, PagingAndSortingRepository<Post, Long> {
    Page<Post> findAll(Specification<Post> spec, Pageable pageable);

}
