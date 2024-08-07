package com.example.socialnetwork.domain.service;

import com.example.socialnetwork.application.request.PostRequest;
import com.example.socialnetwork.application.response.PostResponse;
import com.example.socialnetwork.common.constant.ERelationship;
import com.example.socialnetwork.common.constant.Visibility;
import com.example.socialnetwork.common.mapper.PostMapper;
import com.example.socialnetwork.common.util.SecurityUtil;
import com.example.socialnetwork.domain.model.PostDomain;
import com.example.socialnetwork.domain.model.UserDomain;
import com.example.socialnetwork.domain.port.api.PostServicePort;
import com.example.socialnetwork.domain.port.api.RelationshipServicePort;
import com.example.socialnetwork.domain.port.spi.CloseRelationshipDatabasePort;
import com.example.socialnetwork.domain.port.spi.PostDatabasePort;
import com.example.socialnetwork.domain.port.spi.RelationshipDatabasePort;
import com.example.socialnetwork.domain.port.spi.UserDatabasePort;
import com.example.socialnetwork.exception.custom.ClientErrorException;
import com.example.socialnetwork.exception.custom.NotAllowException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class PostServiceImpl implements PostServicePort {
    private final PostDatabasePort postDatabasePort;
    private final RelationshipDatabasePort relationshipDatabasePort;
    private final CloseRelationshipDatabasePort closeRelationshipDatabasePort;
    private final UserDatabasePort userDatabasePort;
    private final PostMapper postMapper;

    @Override
    public PostDomain createPost(PostRequest postRequest) {
        PostDomain postDomain = new PostDomain();
        postDomain.setUserId(postRequest.getUserId());
        postDomain.setContent(postRequest.getContent());
        postDomain.setVisibility(Visibility.valueOf(postRequest.getVisibility()));
        postDomain.setPhotoLists(postRequest.getPhotoLists());
        postDomain.setLastComment(LocalDateTime.now());
        postDomain.setCreatedAt(LocalDateTime.now());
        postDomain.setUpdatedAt(LocalDateTime.now());
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
        if (postRequest.getPhotoLists().isEmpty()) {
            postDomain.setPhotoLists(null);
        }else{
            postDomain.setPhotoLists(postRequest.getPhotoLists());
        }
        postDomain.setUpdatedAt(LocalDateTime.now());
        return postDatabasePort.updatePost(postDomain);
    }

    @Override
    public void deletePost( Long postId) {
        postDatabasePort.deletePost(postId);
    }

    @Override
    public Page<PostResponse> getAllPosts(int page, int pageSize, String sortBy, String sortDirection, Long userId, Long targetUserId) {
        Sort.Direction direction = sortDirection.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Sort sort = Sort.by(direction, sortBy);
        Page<PostDomain> posts = null;
        if (userId.equals(targetUserId)) {
            posts = postDatabasePort.getAllPosts(page, pageSize, sort, userId, List.of(Visibility.PUBLIC, Visibility.FRIEND, Visibility.PRIVATE));
        } else {
            ERelationship relationship = relationshipDatabasePort.getRelationship(userId, targetUserId);
            if (relationship == null || relationship == ERelationship.PENDING) {
                posts = postDatabasePort.getAllPosts(page, pageSize, sort, targetUserId, List.of(Visibility.PUBLIC));
            }
            if (relationship == ERelationship.FRIEND) {
                posts = postDatabasePort.getAllPosts(page, pageSize, sort, targetUserId, List.of(Visibility.PUBLIC, Visibility.FRIEND));
            }
        }
        if (posts != null) {
            return posts.map(postMapper::postDomainToPostResponse);
        } else {
            throw new NotAllowException("You don't have permission to view this user's posts or user doesn't have any posts");
        }
    }

    @Override
    public Page<PostResponse> getNewsFeed(int page, int pageSize, long userId) {
        long currentUserId = SecurityUtil.getCurrentUserId();
        UserDomain currentUser = userDatabasePort.findById(currentUserId);
        List<UserDomain> friends = relationshipDatabasePort.getListFriend(currentUserId);
        friends.add(currentUser);
        List<UserDomain> closedFriends = closeRelationshipDatabasePort.findUserHadClosedRelationshipWith(currentUserId);
        List<PostDomain> postOfClosedFriends = postDatabasePort.getAllPostByFriends(closedFriends);
        List<PostDomain> postOfClosedFriendsToday = new ArrayList<>();
        for (PostDomain postDomain : postOfClosedFriends) {
            if (postDomain.getCreatedAt().toLocalDate().equals(LocalDate.now())) {
                postOfClosedFriendsToday.add(postDomain);
            }
        }
        List<PostDomain> newsFeed = new ArrayList<>();
        List<PostDomain> postOfFriends = postDatabasePort.getAllPostByFriends(friends);
        postOfFriends.removeAll(postOfClosedFriendsToday);
        newsFeed.addAll(postOfClosedFriendsToday);
        newsFeed.addAll(postOfFriends);
        List<PostResponse> postResponses = postMapper.toPostResponses(newsFeed);
        var pageable = PageRequest.of(page - 1, pageSize);
        int start = Math.min((int) pageable.getOffset(), postResponses.size());
        int end = Math.min((start + pageable.getPageSize()), postResponses.size());
        List<PostResponse> pagedPostDomain = postResponses.subList(start, end);
        return new PageImpl<>(pagedPostDomain, pageable, postResponses.size());
    }
}
