package com.example.socialnetwork.domain.service;

import com.example.socialnetwork.application.request.PostRequest;
import com.example.socialnetwork.application.response.PostResponse;
import com.example.socialnetwork.common.mapper.PostMapper;
import com.example.socialnetwork.domain.model.PostDomain;
import com.example.socialnetwork.domain.port.api.PostServicePort;
import com.example.socialnetwork.domain.port.spi.PostDatabasePort;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class PortServiceImpl implements PostServicePort {

    private final PostDatabasePort postDatabasePort;

    @Override
    public PostResponse createPost(PostRequest postRequest) {
        PostDomain postDomain = PostMapper.INSTANCE.postRequestToPostDomain(postRequest);
        return PostMapper.INSTANCE.postDomainToPostResponse(postDatabasePort.createPost(postDomain));
    }

    @Override
    public PostDomain updatePost(PostDomain postDomain) {
        return postDatabasePort.updatePost(postDomain);
    }

    @Override
    public void deletePost(Long postId) {
        postDatabasePort.deletePost(postId);
    }

    @Override
    public List<PostResponse> getAllPosts(Long userId) {
        return postDatabasePort.getAllPosts(userId).stream().map(PostMapper.INSTANCE::postDomainToPostResponse).collect(Collectors.toList());
    }
}
