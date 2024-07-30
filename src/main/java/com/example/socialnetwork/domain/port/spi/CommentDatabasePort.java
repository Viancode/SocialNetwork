package com.example.socialnetwork.domain.port.spi;

import com.example.socialnetwork.domain.model.CommentDomain;
import com.example.socialnetwork.infrastructure.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

import java.util.List;

public interface CommentDatabasePort {
    CommentDomain createComment(CommentDomain comment);
    CommentDomain updateComment(CommentDomain comment);
    void deleteComment(Long commentId);
    CommentDomain findById(Long id);
    List<CommentDomain> findAllByParentComment(CommentDomain parentComment);
    Page<CommentDomain> getAllComments(int page, int pageSize, Sort sort, Long userId, Long postId);
    Page<CommentDomain> getChildComments(int page, int pageSize, Sort sort, Long userId, Long commentId);
}
