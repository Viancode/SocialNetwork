package com.example.socialnetwork.domain.port.spi;

import com.example.socialnetwork.common.constant.Visibility;
import com.example.socialnetwork.domain.model.PostDomain;
import com.example.socialnetwork.infrastructure.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface PostDatabasePort {
    PostDomain createPost(PostDomain postDomain, Authentication authentication);
    PostDomain updatePost(PostDomain postDomain, Authentication authentication);
    void deletePost(Long userId, Long postId);
    PostDomain findById(Long id);
    Page<PostDomain> getAllPosts(Long otherUserId, Visibility visibility, int offset, int pageSize);
}
