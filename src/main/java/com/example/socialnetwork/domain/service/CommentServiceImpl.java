package com.example.socialnetwork.domain.service;

import com.example.socialnetwork.application.request.CommentRequest;
import com.example.socialnetwork.application.response.CommentResponse;
import com.example.socialnetwork.common.constant.ERelationship;
import com.example.socialnetwork.common.constant.Visibility;
import com.example.socialnetwork.common.mapper.CommentMapper;
import com.example.socialnetwork.common.util.SecurityUtil;
import com.example.socialnetwork.domain.model.CommentDomain;
import com.example.socialnetwork.domain.model.PostDomain;
import com.example.socialnetwork.domain.model.RelationshipDomain;
import com.example.socialnetwork.domain.model.UserDomain;
import com.example.socialnetwork.domain.port.api.CommentServicePort;
import com.example.socialnetwork.domain.port.spi.CommentDatabasePort;
import com.example.socialnetwork.domain.port.spi.PostDatabasePort;
import com.example.socialnetwork.domain.port.spi.RelationshipDatabasePort;
import com.example.socialnetwork.domain.port.spi.UserDatabasePort;
import com.example.socialnetwork.exception.custom.NotAllowException;
import com.example.socialnetwork.exception.custom.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@RequiredArgsConstructor
public class CommentServiceImpl implements CommentServicePort {
    private final CommentDatabasePort commentDatabasePort;
    private final UserDatabasePort userDatabase;
    private final PostDatabasePort postDatabasePort;
    private final RelationshipDatabasePort relationshipDatabasePort;
    private final CommentMapper commentMapper;

    private void checkUserCommentAndUserPost(Long userId, Long postId) {
        // check post exists
        PostDomain post = postDatabasePort.findById(postId);
        if (post == null) {
            throw new NotFoundException("Post not found");
        }

        // Check if the user is the post owner
        if (!Objects.equals(post.getUserId(), userId)) {
            ERelationship relationship = relationshipDatabasePort.find(userId, post.getUserId())
                    .map(RelationshipDomain::getRelation)
                    .orElse(null);

            if (relationship == ERelationship.BLOCK ||
                    post.getVisibility() == Visibility.FRIEND && relationship != ERelationship.FRIEND ||
                    post.getVisibility() == Visibility.PRIVATE) {
                throw new NotAllowException("You are not allowed to interact with this post");
            }
        }
    }

    private void checkParentComment(Long userId, Long parentCommentId, Long postId) {
        if (parentCommentId != null) {
            // check parent comment exists
            CommentDomain parentComment = commentDatabasePort.findById(parentCommentId);
            if (parentComment == null) {
                throw new NotFoundException("Parent comment not found");
            }

            // check current comment is not top level comment
            if (parentComment.getParentCommentId() != null) {
                throw new NotAllowException("You are not allowed to reply to this comment");
            }

            // Check user is blocked by comment owner
            if (userId != parentComment.getUser().getId()) {
                ERelationship relationship = relationshipDatabasePort.find(userId, parentComment.getUser().getId())
                        .map(RelationshipDomain::getRelation)
                        .orElse(null);

                if (relationship == ERelationship.BLOCK) {
                    throw new NotAllowException("You are not allowed to interact with this comment");
                }
            }

            // check parent comment is belonged to the post
            if (!Objects.equals(parentComment.getPost().getId(), postId)) {
                throw new NotAllowException("You are not allowed to reply to this comment");
            }
        }
    }

    @Override
    public CommentDomain createComment(CommentRequest commentRequest) {
        Long userId = SecurityUtil.getCurrentUserId();
        checkUserCommentAndUserPost(userId, commentRequest.getPostId());
        checkParentComment(userId, commentRequest.getParentCommentId(), commentRequest.getPostId());
        return commentDatabasePort.createComment(commentMapper.commentRequestToCommentDomain(commentRequest));
    }

    @Override
    @Transactional
    public CommentDomain updateComment(Long commentId, String content, String image, Long postId) {
        Long userId = SecurityUtil.getCurrentUserId();

        checkUserCommentAndUserPost(userId, postId);

        CommentDomain currentComment = commentDatabasePort.findById(commentId);
        if (currentComment.getUser().getId() != userId) {
            throw new NotAllowException("You are not allowed to update this comment");
        }

        checkParentComment(userId, currentComment.getParentCommentId(), postId);

        currentComment.setContent(content);
        currentComment.setUpdatedAt(LocalDateTime.now());
        currentComment.setImage(image);
        return commentDatabasePort.updateComment(currentComment);
    }

    @Override
    @Transactional
    public void deleteComment(Long commentId) {
        Long userId = SecurityUtil.getCurrentUserId();
        CommentDomain comment = commentDatabasePort.findById(commentId);
        if (comment == null) {
            throw new NotFoundException("Comment not found");
        }

        if (comment.getUser().getId() != userId) {
            throw new NotAllowException("You are not allowed to delete this comment");
        }

        commentDatabasePort.deleteComment(commentId);
    }

    @Override
    public List<CommentDomain> findAllUpdateWithinLastDay(LocalDateTime yesterday) {
        return commentDatabasePort.findAllUpdateWithinLastDay(yesterday);
    }

    @Override
    public Page<CommentResponse> getAllComments(Long postId, int page, int pageSize, String sortBy, String sortDirection) {
        Long userId = SecurityUtil.getCurrentUserId();
        checkUserCommentAndUserPost(userId, postId);

        Sort.Direction direction = sortDirection.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Sort sort = Sort.by(direction, sortBy);

        // Get the list of blocked friends
        List<UserDomain> listBlockFriend = relationshipDatabasePort.getListBlock(userId);
        List<Long> blockedUserIds = listBlockFriend.stream()
                .map(UserDomain::getId)
                .toList();

        Page<CommentDomain> comments = commentDatabasePort.getAllComments(page, pageSize, sort, userId, postId, blockedUserIds);
        if (comments == null || comments.isEmpty()) {
            throw new NotFoundException("This post has no comment");
        }


        return comments.map(commentMapper::commentDomainToCommentResponse);
    }

    @Override
    public Page<CommentResponse> getChildComments(Long postId, Long commentId, int page, int pageSize, String sortBy, String sortDirection) {
        Long userId = SecurityUtil.getCurrentUserId();
        checkParentComment(userId, commentId, postId);

        // Get the list of blocked friends
        List<UserDomain> listBlockFriend = relationshipDatabasePort.getListBlock(userId);
        List<Long> blockedUserIds = listBlockFriend.stream()
                .map(UserDomain::getId)
                .toList();

        Sort.Direction direction = sortDirection.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Sort sort = Sort.by(direction, sortBy);

        Page<CommentDomain> childComments = commentDatabasePort.getChildComments(page, pageSize, sort, userId, commentId, blockedUserIds);
//        if (childComments == null || childComments.isEmpty()) {
//            throw new NotFoundException("This comment has no child comment");
//        }
        return childComments.map(commentMapper::commentDomainToCommentResponse);
    }
}