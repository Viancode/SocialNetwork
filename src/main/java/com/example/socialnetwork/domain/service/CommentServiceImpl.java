package com.example.socialnetwork.domain.service;

import com.example.socialnetwork.application.request.CommentRequest;
import com.example.socialnetwork.application.response.CommentResponse;
import com.example.socialnetwork.domain.model.CommentDomain;
import com.example.socialnetwork.domain.model.UserDomain;
import com.example.socialnetwork.domain.port.api.CommentServicePort;
import com.example.socialnetwork.domain.port.spi.CommentDatabasePort;
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

@RequiredArgsConstructor
public class CommentServiceImpl implements CommentServicePort {
    private final CommentDatabasePort commentDatabasePort;
    private final UserDatabasePort userDatabase;
    @Override
    public CommentDomain createComment(Long userId, CommentRequest commentRequest) {
        UserDomain userDomain = userDatabase.findById(userId);

        if (userDomain == null) {
            throw new NotFoundException("User not found");
        }

        CommentDomain commentDomain = new CommentDomain();
        commentDomain.setUserId(userId);
        commentDomain.setPostId(commentRequest.getPostId());
        commentDomain.setParentComment(commentRequest.getParentComment());
        commentDomain.setContent(commentRequest.getContent());
        commentDomain.setCreatedAt(LocalDateTime.now());
        commentDomain.setUpdatedAt(LocalDateTime.now());
        commentDomain.setIsHidden(false);
        commentDomain.setImage(commentRequest.getImage());
        return commentDatabasePort.createComment(commentDomain);
    }

    @Override
    @Transactional
    public CommentDomain updateComment(Long userId, CommentRequest commentRequest) {
        UserDomain userDomain = userDatabase.findById(userId);

        if (userDomain == null) {
            throw new NotFoundException("User not found");
        }

        Comment currentComment = commentDatabasePort.findById(commentRequest.getId());
        System.out.println(currentComment);

        if (currentComment == null) {
            throw new NotFoundException("Comment not found");
        }

        currentComment.setContent(commentRequest.getContent());
        currentComment.setUpdatedAt(LocalDateTime.now());
        currentComment.setImage(commentRequest.getImage());
        return commentDatabasePort.updateComment(currentComment);
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
            return comments.map()
        }
    }
}
