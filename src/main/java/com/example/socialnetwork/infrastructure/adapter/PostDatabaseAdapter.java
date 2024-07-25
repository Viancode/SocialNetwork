package com.example.socialnetwork.infrastructure.adapter;

import com.example.socialnetwork.common.constant.ERelationship;
import com.example.socialnetwork.common.constant.Visibility;
import com.example.socialnetwork.common.mapper.PostMapper;
import com.example.socialnetwork.domain.model.PostDomain;
import com.example.socialnetwork.domain.port.spi.PostDatabasePort;
import com.example.socialnetwork.exception.custom.ClientErrorException;
import com.example.socialnetwork.exception.custom.NotFoundException;
import com.example.socialnetwork.infrastructure.entity.Post;
import com.example.socialnetwork.infrastructure.entity.Relationship;
import com.example.socialnetwork.infrastructure.repository.PostRepository;
import com.example.socialnetwork.infrastructure.repository.RelationshipRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.example.socialnetwork.infrastructure.specification.PostSpecification.withUserIdAndVisibility;

@RequiredArgsConstructor
public class PostDatabaseAdapter implements PostDatabasePort {
    private final PostRepository postRepository;
    private final RelationshipRepository relationshipRepository;

    @Override
    public PostDomain createPost(PostDomain postDomain, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        long userId = Long.parseLong(user.getUsername());
        Post post;
        if(userId == postDomain.getUserId()){
            post = postRepository.save(PostMapper.INSTANCE.postDomainToPost(postDomain));
        }else{
            throw new NotFoundException("User can`t create post");
        }
        return PostMapper.INSTANCE.postToPostDomain(post);
    }

    @Override
    public PostDomain updatePost(PostDomain postDomain, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        long userId = Long.parseLong(user.getUsername());
        Post post  = postRepository.findById(postDomain.getId()).orElseThrow(() -> new NotFoundException("Post not found"));
        if(post != null){
            if(post.getIsDeleted()){
                throw new ClientErrorException("Post is deleted");
            }else{
                post = postRepository.save(PostMapper.INSTANCE.postDomainToPost(postDomain));
            }
        }
        return PostMapper.INSTANCE.postToPostDomain(post);
    }
    
    public long getUserIdLogin(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return Long.parseLong(user.getUsername());
    }

    @Override
    public void deletePost(Long userId, Long postId) {
        Post post = postRepository.findById(postId).orElse(null);
        if (post != null && !post.getIsDeleted()) {
            if(Objects.equals(userId, post.getUser().getId())){
                post.setIsDeleted(true);
                postRepository.save(post);
            }else{
                throw new ClientErrorException("User can`t delete post");
            }
        }else{
            throw new NotFoundException("Post with id " + postId + " not found");
        }
    }

    @Override
    public PostDomain findById(Long id) {
        return PostMapper.INSTANCE.postToPostDomain(postRepository.findById(id).isPresent()? postRepository.findById(id).get():null);
    }

    @Override
    public Page<PostDomain> getAllPosts(Long otherUserId, Visibility visibility, int offset, int pageSize) {
        var pageable = PageRequest.of(page - 1, pageSize, Sort.by(sortBy));
        var spec = getSpec(otherUserId, Visibility.PUBLIC);
    }

    private Specification<Post> getSpec(Long targetUserId, Visibility visibility) {
        Specification<Post> spec = Specification.where(null);
        if (targetUserId != null && visibility != null) {
            spec = spec.and(withUserIdAndVisibility(targetUserId, visibility));
        }

        return spec;
    }


}
