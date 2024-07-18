package com.example.socialnetwork.domain.port.api;

import com.example.socialnetwork.application.request.PostRequest;
import com.example.socialnetwork.application.response.PostResponse;
import com.example.socialnetwork.domain.model.PostDomain;
import org.springframework.data.domain.Page;

import java.util.List;

public interface PostServicePort {
    PostDomain createPost(PostRequest postRequest);
    PostDomain updatePost(PostRequest postRequest);
    void deletePost(Long postId);
    List<PostResponse> getAllPosts(Long userId);

    Page<PostDomain> getAllPosts(Long userId, int offset, int pageSize);
}
