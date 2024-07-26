package com.example.socialnetwork.infrastructure.adapter;

import com.example.socialnetwork.common.constant.ERelationship;
import com.example.socialnetwork.common.constant.Visibility;
import com.example.socialnetwork.common.mapper.PostMapper;
import com.example.socialnetwork.common.util.SecurityUtil;
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
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.example.socialnetwork.infrastructure.specification.PostSpecification.*;

@RequiredArgsConstructor
public class PostDatabaseAdapter implements PostDatabasePort {
    private final PostRepository postRepository;
    private final RelationshipRepository relationshipRepository;

    @Override
    public PostDomain createPost(PostDomain postDomain) {
        Post post = postRepository.save(PostMapper.INSTANCE.postDomainToPost(postDomain));
        return PostMapper.INSTANCE.postToPostDomain(post);
    }

    @Override
    public PostDomain updatePost(PostDomain postDomain) {
        Post post  = postRepository.findById(postDomain.getId()).orElse(null);
        if (post == null) {
            throw new NotFoundException("Post not found");
        }else{
            post = postRepository.save(PostMapper.INSTANCE.postDomainToPost(postDomain));
            return PostMapper.INSTANCE.postToPostDomain(post);

        }
    }

    @Override
    public void deletePost(Long postId) {
        Long userId = SecurityUtil.getCurrentUserId();
        Post post = postRepository.findById(postId).orElse(null);
        if (post != null) {
            if(userId.equals(post.getUser().getId())){
                postRepository.delete(post);
            }else{
                throw new ClientErrorException("User not authorized to delete this post");
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
    public Page<PostDomain> getAllPosts(int page, int pageSize, Sort sort, Long targetUserId, Visibility visibility) {
        var pageable = PageRequest.of(page - 1, pageSize, sort);
        var spec = getSpec(targetUserId, visibility);
        return postRepository.findAll(spec, pageable).map(PostMapper.INSTANCE::postToPostDomain);
    }

    private Specification<Post> getSpec(Long targetUserId, Visibility visibility) {
        Specification<Post> spec = Specification.where(null);
        if (visibility == null) {
            spec = spec.and(withUserId(targetUserId));
        }
        if (targetUserId != null && visibility == Visibility.PUBLIC) {
            spec = spec.and(withUserIdAndVisibility(targetUserId, visibility));
        }
        if (targetUserId != null && visibility == Visibility.FRIEND) {
            spec = spec.and(withUserIdAndVisibility(targetUserId, visibility, Visibility.PUBLIC));
        }

        return spec;
    }


}
