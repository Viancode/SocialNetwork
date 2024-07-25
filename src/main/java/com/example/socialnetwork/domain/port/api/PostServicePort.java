package com.example.socialnetwork.domain.port.api;

import com.example.socialnetwork.application.request.PostRequest;
import com.example.socialnetwork.domain.model.PostDomain;
import org.springframework.data.domain.Page;

public interface PostServicePort {
    PostDomain createPost(PostRequest postRequest);
    PostDomain updatePost(PostRequest postRequest);
    void deletePost(Long postId);
    Page<PostDomain> getAllPosts(int page, int pageSize, String sortBy, String sortDirection, Long userId, Long targetUserId);
}
