package com.example.socialnetwork.infrastructure.adapter;

import com.example.socialnetwork.common.constant.Visibility;
import com.example.socialnetwork.common.mapper.PostMapper;
import com.example.socialnetwork.common.mapper.UserMapper;
import com.example.socialnetwork.common.util.SecurityUtil;
import com.example.socialnetwork.domain.model.PostDomain;
import com.example.socialnetwork.domain.model.UserDomain;
import com.example.socialnetwork.domain.port.spi.PostDatabasePort;
import com.example.socialnetwork.exception.custom.ClientErrorException;
import com.example.socialnetwork.exception.custom.NotFoundException;
import com.example.socialnetwork.infrastructure.entity.Post;
import com.example.socialnetwork.infrastructure.entity.User;
import com.example.socialnetwork.infrastructure.repository.PostRepository;
import com.example.socialnetwork.infrastructure.repository.RelationshipRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

import static com.example.socialnetwork.infrastructure.specification.PostSpecification.*;

@RequiredArgsConstructor
public class PostDatabaseAdapter implements PostDatabasePort {
    private final PostRepository postRepository;
    private final RelationshipRepository relationshipRepository;
    private final PostMapper postMapper;
    private final UserMapper userMapper;

    @Override
    public PostDomain createPost(PostDomain postDomain) {
        Post post = postRepository.save(postMapper.postDomainToPost(postDomain));
        return postMapper.postToPostDomain(post);
    }

    @Override
    public PostDomain updatePost(PostDomain postDomain) {
        Post post  = postRepository.findById(postDomain.getId()).orElse(null);
        if (post == null) {
            throw new NotFoundException("Post not found");
        }else{
            post = postRepository.save(postMapper.postDomainToPost(postDomain));
            return postMapper.postToPostDomain(post);

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
        return postMapper.postToPostDomain(postRepository.findById(id).isPresent()? postRepository.findById(id).get():null);
    }

    @Override
    public Page<PostDomain> getAllPosts(int page, int pageSize, Sort sort, Long targetUserId, List<Visibility> visibilities) {
        var pageable = PageRequest.of(page - 1, pageSize, sort);
        var spec = getSpec(targetUserId, visibilities);
        return postRepository.findAll(spec, pageable).map(postMapper::postToPostDomain);
    }

    @Override
    public List<PostDomain> getAllPostByFriends(List<UserDomain> userDomains){
        List<User> users = userDomains.stream().map(userMapper::toUser).toList();
        return postMapper.toPostDomains(postRepository.findByListUser(users));
    }

    private Specification<Post> getSpec(Long targetUserId, List<Visibility> visibilities) {
        Specification<Post> spec = Specification.where(null);
        if (visibilities != null && !visibilities.isEmpty()) {
            spec = spec.and(withUserIdAndVisibility(targetUserId, visibilities));
        }
        return spec;
    }
}
