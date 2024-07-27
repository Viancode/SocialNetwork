package com.example.socialnetwork.domain.port.spi;

import com.example.socialnetwork.common.constant.Visibility;
import com.example.socialnetwork.domain.model.CommentDomain;
import com.example.socialnetwork.domain.model.PostDomain;
import com.example.socialnetwork.infrastructure.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

public interface CommentDatabasePort {
    CommentDomain createComment(CommentDomain commentDomain);
    CommentDomain updateComment(Comment comment);
    void deleteComment(Long commentId);
    Comment findById(Long id);
    Page<CommentDomain> getAllComments(int page, int pageSize, Sort sort, Long targetUserId, Visibility visibility);
}
