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

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class CommentDatabaseAdapter implements CommentDatabasePort {
    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;
    @Override
    public CommentDomain createComment(CommentDomain comment) {
        Comment commentEntity = commentMapper.commentDomainToCommentEntity(comment);
        return  commentMapper.commentEntityToCommentDomain(commentRepository.save(commentEntity));
    }

    @Override
    public Comment updateComment(Comment comment) {
        return commentRepository.save(comment);
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
    public List<CommentDomain> findAllByParentComment(CommentDomain parentComment) {
        return commentRepository.findAllByParentComment(commentMapper.commentDomainToCommentEntity(parentComment))
                .stream()
                .map(commentMapper::commentEntityToCommentDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Page<CommentDomain> getAllComments(int page, int pageSize, Sort sort, Long targetUserId, Long postId) {
        return null;
    }
}
