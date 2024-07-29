package com.example.socialnetwork.domain.service;

import com.example.socialnetwork.application.request.CommentRequest;
import com.example.socialnetwork.application.response.CommentResponse;
import com.example.socialnetwork.common.mapper.CommentMapper;
import com.example.socialnetwork.domain.model.CommentDomain;
import com.example.socialnetwork.domain.model.UserDomain;
import com.example.socialnetwork.domain.port.api.CommentServicePort;
import com.example.socialnetwork.domain.port.spi.CommentDatabasePort;
import com.example.socialnetwork.domain.port.spi.PostDatabasePort;
import com.example.socialnetwork.domain.port.spi.UserDatabasePort;
import com.example.socialnetwork.exception.custom.NotAllowException;
import com.example.socialnetwork.exception.custom.NotFoundException;
import com.example.socialnetwork.infrastructure.entity.Comment;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

@RequiredArgsConstructor
public class CommentServiceImpl implements CommentServicePort {
    private final CommentDatabasePort commentDatabasePort;
    private final UserDatabasePort userDatabase;
    private final PostDatabasePort postDatabasePort;
    private final CommentMapper commentMapper;
    @Override
    public CommentDomain createComment(Long userId, CommentRequest commentRequest) {
        UserDomain userDomain = userDatabase.findById(userId);

        if (userDomain == null) {
            throw new NotFoundException("User not found");
        }

        if (postDatabasePort.findById(commentRequest.getPostId()) == null) {
            throw new NotFoundException("Post not found");
        }

        return commentDatabasePort.createComment(commentMapper.commentRequestToCommentDomain(commentRequest));
    }

    @Override
    @Transactional
    public CommentDomain updateComment(Long userId, Long commentId, String content, String image, Long postId) {
        UserDomain userDomain = userDatabase.findById(userId);

        if (userDomain == null) {
            throw new NotFoundException("User not found");
        }

        if (postDatabasePort.findById(postId) == null) {
            throw new NotFoundException("Post not found");
        }

        Comment currentComment = commentDatabasePort.findById(commentId);
        System.out.println(currentComment);

        if ( currentComment == null || !Objects.equals(currentComment.getPost().getId(), postId)) {
            throw new NotFoundException("Comment not found");
        }

        currentComment.setContent(content);
        currentComment.setUpdatedAt(LocalDateTime.now());
        currentComment.setImage(image);
        Comment updatedComment = commentDatabasePort.updateComment(currentComment);
        return commentMapper.commentEntityToCommentDomain(updatedComment);
    }

    @Override
    public void deleteComment(Long userId, Long commentId) {
        Comment comment = commentDatabasePort.findById(commentId);

        if (comment == null) {
            throw new NotFoundException("Comment not found");
        }

        if (!comment.getUser().getId().equals(userId)) {
            throw new NotAllowException("You are not allowed to delete this comment");
        }

        commentDatabasePort.deleteComment(commentId);
    }

    @Override
    public Page<CommentResponse> getAllComments(Long userId, Long postId, int page, int pageSize, String sortBy, String sortDirection) {
        Sort.Direction direction = sortDirection.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Sort sort = Sort.by(direction, sortBy);

        Page<CommentDomain> comments = null;
        comments = commentDatabasePort.getAllComments(page, pageSize, sort, userId, postId);

        if (comments != null) {
            return comments.map(commentMapper::commentDomainToCommentResponse);
        } else {
            throw new NotFoundException("This post has no comment");
        }
    }
}
