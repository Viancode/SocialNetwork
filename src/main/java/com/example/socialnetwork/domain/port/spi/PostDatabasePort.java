package com.example.socialnetwork.domain.port.spi;

import com.example.socialnetwork.domain.model.PostDomain;

import java.util.List;

public interface PostDatabasePort {
    PostDomain createPost(PostDomain postDomain);
    PostDomain updatePost(PostDomain postDomain);
    void deletePost(Long postId);
    List<PostDomain> getAllPosts(Long userId);
}
