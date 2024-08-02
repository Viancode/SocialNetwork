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
import java.util.Optional;

@RequiredArgsConstructor
public class CommentServiceImpl implements CommentServicePort {
    private final CommentDatabasePort commentDatabasePort;
    private final UserDatabasePort userDatabase;
    private final PostDatabasePort postDatabasePort;
    private final RelationshipDatabasePort relationshipDatabasePort;
    private final CommentMapper commentMapper;

    // Checks if a post exists.
    private void checkPostExists(Long postId) {
        PostDomain post = postDatabasePort.findById(postId);
        if (post == null) {
            throw new NotFoundException("Post not found");
        }
    }

    // Checks if a user is blocked by the post owner.
    private void checkUserBlocked(Long userId, Long postOwnerId) {
        ERelationship relationship = relationshipDatabasePort.find(userId, postOwnerId).getRelation();
        if (relationship == ERelationship.BLOCK) {
            throw new NotAllowException("You are not allowed to interact with this post");
        }
    }

    // Checks if a user has permission to interact with a post based on its visibility.
    private void checkPostVisibility(PostDomain post, Long userId, ERelationship relationship) {
        if (post.getVisibility() == Visibility.FRIEND) {
            if (relationship == null || relationship != ERelationship.FRIEND) {
                throw new NotAllowException("You are not allowed to interact with this post");
            }
        }
        if (post.getVisibility() == Visibility.PRIVATE && !Objects.equals(post.getUserId(), userId)) {
            throw new NotAllowException("You are not allowed to interact with this post");
        }
    }

    // Checks if a parent comment exists when replying to a comment.
    private void checkParentCommentExists(Long parentCommentId) {
        if (parentCommentId != null) {
            CommentDomain parentComment = commentDatabasePort.findById(parentCommentId);
            if (parentComment == null) {
                throw new NotFoundException("Parent comment not found");
            }
        }
    }

    // Checks if a user is blocked by the comment owner.
    private void checkUserBlockedByCommentOwner(Long userId, Long commentOwnerId) {
        if (userId.equals(commentOwnerId)) {
            return;
        }
        ERelationship relationship = relationshipDatabasePort.find(userId, commentOwnerId).getRelation();
        if (relationship == ERelationship.BLOCK) {
            throw new NotAllowException("You are not allowed to interact with this comment");
        }
    }

    // Validates a comment operation
    // userId The ID of the user performing the operation.
    // postId The ID of the post associated with the comment.
    private void validateUserCommentAndUserPost(Long userId, Long postId) {
        checkPostExists(postId);
        PostDomain post = postDatabasePort.findById(postId);

        // Check if the user is the post owner
        if (!Objects.equals(post.getUserId(), userId)) {
            checkUserBlocked(userId, post.getUserId());
            ERelationship relationship = Optional.ofNullable(relationshipDatabasePort.find(userId, post.getUserId()))
                    .map(RelationshipDomain::getRelation)
                    .orElse(null);
            checkPostVisibility(post, userId, relationship);
        }
    }

    private void checkParentComment(Long userId, Long parentCommentId) {
        if (parentCommentId != null) {
            CommentDomain parentComment = commentDatabasePort.findById(parentCommentId);
            if (parentComment == null) {
                throw new NotFoundException("Parent comment not found");
            }

            if (parentComment.getParentCommentId() != null) {
                throw new NotAllowException("You are not allowed to reply to this comment");
            }
            checkUserBlockedByCommentOwner(userId, parentComment.getUser().getId());
        }
    }

    @Override
    public CommentDomain createComment(CommentRequest commentRequest) {
        Long userId = SecurityUtil.getCurrentUserId();
        validateUserCommentAndUserPost(userId, commentRequest.getPostId());
        checkParentComment(userId, commentRequest.getParentCommentId());
        return commentDatabasePort.createComment(commentMapper.commentRequestToCommentDomain(commentRequest));
    }

    @Override
    @Transactional
    public CommentDomain updateComment(Long commentId, String content, String image, Long postId) {
        Long userId = SecurityUtil.getCurrentUserId();

        validateUserCommentAndUserPost(userId, postId);

        CommentDomain currentComment = commentDatabasePort.findById(commentId);
        if (currentComment.getUser().getId() != userId) {
            throw new NotAllowException("You are not allowed to update this comment");
        }

        checkParentComment(userId, currentComment.getParentCommentId());

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
    public Page<CommentResponse> getAllComments(Long postId, int page, int pageSize, String sortBy, String sortDirection) {
        Long userId = SecurityUtil.getCurrentUserId();
        validateUserCommentAndUserPost(userId, postId);

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
    public Page<CommentResponse> getChildComments(Long commentId, int page, int pageSize, String sortBy, String sortDirection) {
        Long userId = SecurityUtil.getCurrentUserId();
        CommentDomain parentComment = commentDatabasePort.findById(commentId);
        if (parentComment == null) {
            throw new NotFoundException("Parent comment not found");
        }

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