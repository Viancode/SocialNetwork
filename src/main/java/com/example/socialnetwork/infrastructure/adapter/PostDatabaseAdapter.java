package com.example.socialnetwork.infrastructure.adapter;

import com.example.socialnetwork.common.mapper.PostMapper;
import com.example.socialnetwork.domain.model.PostDomain;
import com.example.socialnetwork.domain.port.spi.PostDatabasePort;
import com.example.socialnetwork.infrastructure.entity.Post;
import com.example.socialnetwork.infrastructure.repository.PostRepository;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class PostDatabaseAdapter implements PostDatabasePort {
    private final PostRepository postRepository;

    @Override
    public PostDomain createPost(PostDomain postDomain) {
        Post post = postRepository.save(PostMapper.INSTANCE.postDomainToPost(postDomain));
        return  PostMapper.INSTANCE.postToPostDomain(post);
    }

    @Override
    public PostDomain updatePost(PostDomain postDomain) {
        Post post = postRepository.save(PostMapper.INSTANCE.postDomainToPost(postDomain));
        return PostMapper.INSTANCE.postToPostDomain(post);
    }

    @Override
    public void deletePost(Long postId) {
        postRepository.deleteById(postId);
    }

    @Override
    public List<PostDomain> getAllPosts(Long userId) {
        List<Post> posts = postRepository.findAllByUserId(userId);
        return posts.stream().map(PostMapper.INSTANCE::postToPostDomain).collect(Collectors.toList());
    }
}
