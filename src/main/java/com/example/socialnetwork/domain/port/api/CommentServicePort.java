package com.example.socialnetwork.domain.port.api;

import com.example.socialnetwork.application.request.CommentRequest;
import com.example.socialnetwork.application.response.CommentResponse;
import com.example.socialnetwork.domain.model.CommentDomain;
import org.springframework.data.domain.Page;

public interface CommentServicePort {
    CommentDomain createComment(CommentRequest commentRequest);
    CommentDomain updateComment(Long commentId, String content, String image, Long postId);
    void deleteComment(Long commentId);
    Page<CommentResponse> getAllComments(Long postId, int page, int pageSize, String sortBy, String sortDirection);
    Page<CommentResponse> getChildComments(Long postId, Long commentId, int page, int pageSize, String sortBy, String sortDirection);
}
