package com.example.socialnetwork.domain.service;

import com.example.socialnetwork.application.request.PostRequest;
import com.example.socialnetwork.common.constant.ERelationship;
import com.example.socialnetwork.common.constant.Visibility;
import com.example.socialnetwork.domain.model.PostDomain;
import com.example.socialnetwork.domain.port.api.PostServicePort;
import com.example.socialnetwork.domain.port.api.RelationshipServicePort;
import com.example.socialnetwork.domain.port.spi.PostDatabasePort;
import com.example.socialnetwork.exception.custom.ClientErrorException;
import com.example.socialnetwork.exception.custom.NotAllowException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

import java.time.LocalDateTime;

@RequiredArgsConstructor
public class PostServiceImpl implements PostServicePort {

    private final PostDatabasePort postDatabasePort;
    private final RelationshipServicePort relationshipService;

    @Override
    public PostDomain createPost(PostRequest postRequest) {
        PostDomain postDomain = new PostDomain();
        postDomain.setUserId(postRequest.getUserId());
        postDomain.setContent(postRequest.getContent());
        postDomain.setVisibility(Visibility.valueOf(postRequest.getVisibility()));
        postDomain.setPhotoLists(postRequest.getPhotoLists());
        postDomain.setCreatedAt(LocalDateTime.now());
        return postDatabasePort.createPost(postDomain);
    }

    @Override
    public PostDomain updatePost(PostRequest postRequest) {
        PostDomain postDomain = postDatabasePort.findById(postRequest.getId());
        if (postRequest.getContent().isEmpty()) {
            throw new ClientErrorException("Content is empty");
        } else {
            postDomain.setContent(postRequest.getContent());
        }
        postDomain.setVisibility(Visibility.valueOf(postRequest.getVisibility()));
        postDomain.setPhotoLists(postRequest.getPhotoLists());
        postDomain.setUpdatedAt(LocalDateTime.now());
        return postDatabasePort.updatePost(postDomain);
    }

    @Override
    public void deletePost( Long postId) {
        postDatabasePort.deletePost(postId);
    }

    @Override
    public Page<PostDomain> getAllPosts(int page, int pageSize, String sortBy, String sortDirection, Long userId, Long targetUserId) {
        Sort.Direction direction = sortDirection.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Sort sort = Sort.by(direction, sortBy);
        if (userId.equals(targetUserId)) {
            return postDatabasePort.getAllPosts(page, pageSize, sort, userId, null);
        }
        ERelationship relationship = relationshipService.getRelationship(userId, targetUserId);

        if (relationship == null || relationship == ERelationship.PENDING) {
            return postDatabasePort.getAllPosts(page, pageSize, sort, targetUserId, Visibility.PUBLIC);
        }

        if (relationship == ERelationship.FRIEND) {
            return postDatabasePort.getAllPosts(page, pageSize, sort, targetUserId, Visibility.FRIEND);
        }

        throw new NotAllowException("You don't have permission to view this user's posts");
    }
}
