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
        return PostMapper.INSTANCE.postToPostDomain(post);
    }

    @Override
    public PostDomain updatePost(PostDomain postDomain) {
        Post post = postRepository.save(PostMapper.INSTANCE.postDomainToPost(postDomain));
        return PostMapper.INSTANCE.postToPostDomain(post);
    }

    @Override
    public void deletePost(Long postId) {
        Post post = postRepository.findById(postId).isPresent() ? postRepository.findById(postId).get() : null;
        if (post != null) {
            post.setIsDeleted(true);
            postRepository.save(post);
        }
    }

    @Override
    public List<PostDomain> getAllPosts(Long userId) {
        List<Post> posts = postRepository.findAllByUserId(userId);
        return posts.stream().filter(x -> !x.getIsDeleted()).map(PostMapper.INSTANCE::postToPostDomain).collect(Collectors.toList());
    }

    @Override
    public PostDomain findById(Long id) {
        return PostMapper.INSTANCE.postToPostDomain(postRepository.findById(id).isPresent()? postRepository.findById(id).get():null);
    }
}
