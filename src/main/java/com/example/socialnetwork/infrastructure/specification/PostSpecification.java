package com.example.socialnetwork.infrastructure.specification;

import com.example.socialnetwork.common.constant.Visibility;
import com.example.socialnetwork.infrastructure.entity.Post;
import com.example.socialnetwork.infrastructure.entity.User;
import org.springframework.data.jpa.domain.Specification;

public class PostSpecification {
    public static Specification<Post> withVisibility(Visibility visibility) {
        return (root, query, cb) -> cb.equal(root.get(Post.Fields.visibility), visibility);
    }

    public static Specification<Post> withUserId(Long userId) {
        return (root, query, cb) -> cb.equal(root.get(Post.Fields.user).get(User.Fields.id), userId);
    }

    public static Specification<Post> withUserIdAndVisibility(Long userId, Visibility visibility) {
        return Specification.where(withUserId(userId)).and(withVisibility(visibility));
    }


}
