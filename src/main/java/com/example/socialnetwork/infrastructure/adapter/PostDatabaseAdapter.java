package com.example.socialnetwork.infrastructure.adapter;

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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
        Post post ;
        if(userId == postDomain.getUserId()){
            post = postRepository.save(PostMapper.INSTANCE.postDomainToPost(postDomain));
        }else{
            throw new NotFoundException("User can`t update post");
        }
        return PostMapper.INSTANCE.postToPostDomain(post);
    }
    
    public long getUserIdLogin(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return Long.parseLong(user.getUsername());
    }

    @Override
    public void deletePost(Long postId) {
        Post post = postRepository.findById(postId).orElse(null);
        if (post != null && !post.getIsDeleted()) {
            post.setIsDeleted(true);
            postRepository.save(post);
        }else{
            throw new NotFoundException("Post with id " + postId + " not found");
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

    @Override
    public Page<PostDomain> getAllPosts(Long userId, Long otherUserId, int offset, int pageSize) {
        Pageable pageable = PageRequest.of(offset, pageSize, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<Post> posts= null;
        if(userId == otherUserId || otherUserId == null){
            posts = postRepository.findByUserIdAndIsDeletedFalse(userId, pageable);
            return posts.map(PostMapper.INSTANCE::postToPostDomain);
        }else {
            Relationship relationship = relationshipRepository.findByUserIdAndFriendId(userId, otherUserId);
            if(relationship != null){
                if(relationship.getRelation().equals("FRIEND")){
                    List<Post> postsList = new ArrayList<>();
                    postsList.addAll(postRepository.findByUserIdAndVisibilityAndIsDeletedFalse(otherUserId,"FRIEND",pageable).getContent());
                    postsList.addAll(postRepository.findByUserIdAndVisibilityAndIsDeletedFalse(otherUserId,"PUBLIC",pageable).getContent());
                    return new PageImpl<>(postsList.stream().map(PostMapper.INSTANCE::postToPostDomain).collect(Collectors.toList()), pageable, postsList.size());
                }
            }else{
                posts = postRepository.findByUserIdAndVisibilityAndIsDeletedFalse(otherUserId,"PUBLIC",pageable);
                return posts.map(PostMapper.INSTANCE::postToPostDomain);
            }
        }
        return posts != null? posts.map(PostMapper.INSTANCE::postToPostDomain):null;
    }


}
