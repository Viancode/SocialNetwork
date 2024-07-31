package com.example.socialnetwork.common.mapper;

import com.example.socialnetwork.application.request.CommentRequest;
import com.example.socialnetwork.application.response.CommentResponse;
import com.example.socialnetwork.common.util.SecurityUtil;
import com.example.socialnetwork.domain.model.CommentDomain;
import com.example.socialnetwork.domain.model.PostDomain;
import com.example.socialnetwork.domain.model.UserDomain;
import com.example.socialnetwork.domain.port.spi.CommentDatabasePort;
import com.example.socialnetwork.infrastructure.entity.Comment;
import com.example.socialnetwork.infrastructure.entity.Post;
import com.example.socialnetwork.infrastructure.entity.User;
import com.example.socialnetwork.infrastructure.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class CommentMapper {
    private final CommentRepository commentRepository;
    public CommentDomain commentRequestToCommentDomain(CommentRequest request) {
        return CommentDomain.builder()
                .user(UserDomain.builder().id(SecurityUtil.getCurrentUserId()).build())
                .post(PostDomain.builder().id(request.getPostId()).build())
                .parentComment(request.getParentComment() != null ? CommentDomain.builder().commentId(request.getParentComment()).build() : null)
                .content(request.getContent())
                .image(request.getImage())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .isHidden(false)
                .build();
    }

    public Comment commentDomainToCommentEntity(CommentDomain domain) {
        return Comment.builder()
                .id(domain.getCommentId())
                .user(User.builder().id(domain.getUser().getId()).build())
                .post(Post.builder().id(domain.getPost().getId()).build())
                .parentComment(domain.getParentComment() != null ? Comment.builder().id(domain.getParentComment().getCommentId()).build() : null)
                .content(domain.getContent())
                .createdAt(domain.getCreatedAt())
                .updatedAt(domain.getUpdatedAt())
                .isHidden(domain.getIsHidden())
                .image(domain.getImage())
                .build();
    }

    public CommentDomain commentEntityToCommentDomain(Comment entity) {
        return CommentDomain.builder()
                .commentId(entity.getId())
                .user(UserDomain.builder()
                        .id(entity.getUser().getId())
                        .username(entity.getUser().getUsername())
                        .avatar(entity.getUser().getAvatar())
                        .build())
                .post(PostDomain.builder().id(entity.getPost().getId()).build())
                .parentComment(entity.getParentComment() != null ? CommentDomain.builder().commentId(entity.getParentComment().getId()).build() : null)
                .content(entity.getContent())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .isHidden(entity.getIsHidden())
                .image(entity.getImage())
                .reactionsId(entity.getCommentReactions())
                .build();
    }

    public CommentResponse commentDomainToCommentResponse(CommentDomain domain) {
        return CommentResponse.builder()
                .commentId(domain.getCommentId())
                .userId(domain.getUser().getId())
                .username(domain.getUser().getUsername())
                .avatar(domain.getUser().getAvatar())
                .postId(domain.getPost().getId())
                .parentComment(domain.getParentComment() != null ? domain.getParentComment().getCommentId() : null)
                .numberOfChild((long) commentRepository.findAllByParentComment(this.commentDomainToCommentEntity(domain)).size())
                .content(domain.getContent())
                .createdAt(domain.getCreatedAt())
                .updatedAt(domain.getUpdatedAt())
                .isHidden(domain.getIsHidden())
                .image(domain.getImage())
                .reactCount(domain.getReactionsId() != null ? (long) domain.getReactionsId().size() : 0)
                .build();
    }
}