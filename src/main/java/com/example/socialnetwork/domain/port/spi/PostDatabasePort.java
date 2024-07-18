package com.example.socialnetwork.domain.port.spi;

import com.example.socialnetwork.domain.model.PostDomain;
import com.example.socialnetwork.infrastructure.entity.Post;
import org.springframework.data.domain.Page;

import java.util.List;

public interface PostDatabasePort {
    PostDomain createPost(PostDomain postDomain);
    PostDomain updatePost(PostDomain postDomain);
    void deletePost(Long postId);
    List<PostDomain> getAllPosts(Long userId);

    PostDomain findById(Long id);

    Page<PostDomain> getAllPosts(Long userId, int offset, int pageSize);
}
