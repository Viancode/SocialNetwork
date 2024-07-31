package com.example.socialnetwork.domain.port.spi;

import com.example.socialnetwork.common.constant.Visibility;
import com.example.socialnetwork.domain.model.PostDomain;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

import java.util.List;

public interface PostDatabasePort {
    PostDomain createPost(PostDomain postDomain);
    PostDomain updatePost(PostDomain postDomain);
    void deletePost(Long postId);
    PostDomain findById(Long id);
    Page<PostDomain> getAllPosts(int page, int pageSize, Sort sort, Long targetUserId, Visibility visibility);
    List<PostDomain> getAllPosts(long targetUserId, boolean checkCurrentUser);
}
