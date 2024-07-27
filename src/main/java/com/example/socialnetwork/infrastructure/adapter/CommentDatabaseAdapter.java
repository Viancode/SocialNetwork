package com.example.socialnetwork.infrastructure.adapter;

import com.example.socialnetwork.common.constant.Visibility;
import com.example.socialnetwork.common.mapper.CommentMapper;
import com.example.socialnetwork.common.mapper.PostMapper;
import com.example.socialnetwork.domain.model.CommentDomain;
import com.example.socialnetwork.domain.port.spi.CommentDatabasePort;
import com.example.socialnetwork.infrastructure.entity.Comment;
import com.example.socialnetwork.infrastructure.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

@RequiredArgsConstructor
public class CommentDatabaseAdapter implements CommentDatabasePort {
    private final CommentRepository commentRepository;
    @Override
    public CommentDomain createComment(CommentDomain commentDomain) {
        Comment comment = commentRepository.save(CommentMapper.INSTANCE.commentDomainToComment(commentDomain));
        return CommentMapper.INSTANCE.commentToCommentDomain(comment);
    }

    @Override
    public CommentDomain updateComment(Comment comment) {
        Comment updateComment = commentRepository.save(comment);
        return CommentMapper.INSTANCE.commentToCommentDomain(updateComment);
    }

    @Override
    public void deleteComment(Long commentId) {
        commentRepository.deleteById(commentId);
    }

    @Override
    public Comment findById(Long id) {
        return commentRepository.findById(id).orElse(null);
    }

    @Override
    public Page<CommentDomain> getAllComments(int page, int pageSize, Sort sort, Long targetUserId, Visibility visibility) {
        return null;
    }
}
