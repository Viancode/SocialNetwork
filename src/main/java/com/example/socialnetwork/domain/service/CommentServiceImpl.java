package com.example.socialnetwork.domain.service;

import com.example.socialnetwork.application.request.CommentRequest;
import com.example.socialnetwork.application.response.CommentResponse;
import com.example.socialnetwork.common.constant.ERelationship;
import com.example.socialnetwork.common.constant.Visibility;
import com.example.socialnetwork.common.mapper.CommentMapper;
import com.example.socialnetwork.domain.model.CommentDomain;
import com.example.socialnetwork.domain.model.PostDomain;
import com.example.socialnetwork.domain.model.UserDomain;
import com.example.socialnetwork.domain.port.api.CommentServicePort;
import com.example.socialnetwork.domain.port.api.RelationshipServicePort;
import com.example.socialnetwork.domain.port.spi.CommentDatabasePort;
import com.example.socialnetwork.domain.port.spi.PostDatabasePort;
import com.example.socialnetwork.domain.port.spi.UserDatabasePort;
import com.example.socialnetwork.exception.custom.NotAllowException;
import com.example.socialnetwork.exception.custom.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Objects;

@RequiredArgsConstructor
public class CommentServiceImpl implements CommentServicePort {
    private final CommentDatabasePort commentDatabasePort;
    private final UserDatabasePort userDatabase;
    private final PostDatabasePort postDatabasePort;
    private final RelationshipServicePort relationshipServicePort;
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
        ERelationship relationship = relationshipServicePort.getRelationship(userId, postOwnerId);
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
        ERelationship relationship = relationshipServicePort.getRelationship(userId, commentOwnerId);
        if (relationship == ERelationship.BLOCK) {
            throw new NotAllowException("You are not allowed to interact with this comment");
        }
    }

    // Validates a comment operation
    private void validateCommentOperation(Long userId, Long postId, Long commentId) {
        checkPostExists(postId);
        PostDomain post = postDatabasePort.findById(postId);

        // Check if the user is the post owner
        if (!Objects.equals(post.getUserId(), userId)) {
            checkUserBlocked(userId, post.getUserId());
            ERelationship relationship = relationshipServicePort.getRelationship(userId, post.getUserId());
            checkPostVisibility(post, userId, relationship);
        }

        if (commentId != null) {
            CommentDomain comment = commentDatabasePort.findById(commentId);
            if (comment == null || !Objects.equals(comment.getPost().getId(), postId)) {
                throw new NotFoundException("Comment not found");
            }
            checkUserBlockedByCommentOwner(userId, comment.getUser().getId());
        }
    }

    @Override
    public CommentDomain createComment(Long userId, CommentRequest commentRequest) {
        validateCommentOperation(userId, commentRequest.getPostId(), null);
        checkParentCommentExists(commentRequest.getParentComment());

        return commentDatabasePort.createComment(commentMapper.commentRequestToCommentDomain(commentRequest));
    }

    @Override
    @Transactional
    public CommentDomain updateComment(Long userId, Long commentId, String content, String image, Long postId) {
        validateCommentOperation(userId, postId, commentId);

        CommentDomain currentComment = commentDatabasePort.findById(commentId);
        if (currentComment.getUser().getId() != userId) {
            throw new NotAllowException("You are not allowed to update this comment");
        }

        currentComment.setContent(content);
        currentComment.setUpdatedAt(LocalDateTime.now());
        currentComment.setImage(image);
        return commentDatabasePort.updateComment(currentComment);
    }

    @Override
    public void deleteComment(Long userId, Long commentId) {
        CommentDomain comment = commentDatabasePort.findById(commentId);
        if (comment == null) {
            throw new NotFoundException("Comment not found");
        }

        validateCommentOperation(userId, comment.getPost().getId(), commentId);

        if (comment.getUser().getId() != userId) {
            throw new NotAllowException("You are not allowed to delete this comment");
        }

        commentDatabasePort.deleteComment(commentId);
    }

    @Override
    public Page<CommentResponse> getAllComments(Long userId, Long postId, int page, int pageSize, String sortBy, String sortDirection) {
        validateCommentOperation(userId, postId, null);

        Sort.Direction direction = sortDirection.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Sort sort = Sort.by(direction, sortBy);

        Page<CommentDomain> comments = commentDatabasePort.getAllComments(page, pageSize, sort, userId, postId);
        if (comments == null || comments.isEmpty()) {
            throw new NotFoundException("This post has no comment");
        }
        return comments.map(commentMapper::commentDomainToCommentResponse);
    }

    @Override
    public Page<CommentResponse> getChildComments(Long userId, Long commentId, int page, int pageSize, String sortBy, String sortDirection) {
        CommentDomain parentComment = commentDatabasePort.findById(commentId);
        if (parentComment == null) {
            throw new NotFoundException("Parent comment not found");
        }

        validateCommentOperation(userId, parentComment.getPost().getId(), commentId);

        Sort.Direction direction = sortDirection.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Sort sort = Sort.by(direction, sortBy);

        Page<CommentDomain> childComments = commentDatabasePort.getChildComments(page, pageSize, sort, userId, commentId);
        if (childComments == null || childComments.isEmpty()) {
            throw new NotFoundException("This comment has no child comment");
        }
        return childComments.map(commentMapper::commentDomainToCommentResponse);
    }
}