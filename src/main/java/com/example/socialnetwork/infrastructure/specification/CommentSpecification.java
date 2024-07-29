package com.example.socialnetwork.infrastructure.specification;

import com.example.socialnetwork.infrastructure.entity.Comment;
import org.springframework.data.jpa.domain.Specification;

public class CommentSpecification {
    public static Specification<Comment> withPostId(Long postId) {
        return (root, query, cb) -> cb.equal(root.get(Comment.Fields.post).get("id"), postId);
    }

    public static Specification<Comment> withParentCommentId(Long parentCommentId) {
        return (root, query, cb) -> cb.equal(root.get(Comment.Fields.parentComment).get("id"), parentCommentId);
    }

    public static Specification<Comment> withParentCommentIsNull() {
        return (root, query, cb) -> cb.isNull(root.get(Comment.Fields.parentComment));
    }

    public static Specification<Comment> withPostIdAndParentCommentIsNull(Long postId) {
        return Specification.where(withPostId(postId)).and(withParentCommentIsNull());
    }

}
