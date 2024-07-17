package com.example.socialnetwork.domain.port.api;

import com.example.socialnetwork.application.request.PostRequest;
import com.example.socialnetwork.application.response.PostResponse;
import com.example.socialnetwork.domain.model.PostDomain;

import java.util.List;

public interface PostServicePort {
    PostResponse createPost(PostRequest postRequest);
    PostDomain updatePost(PostDomain postDomain);
    void deletePost(Long postId);
    List<PostResponse> getAllPosts(Long userId);
}
