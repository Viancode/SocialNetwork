package com.example.socialnetwork.domain.port.spi;

import com.example.socialnetwork.domain.model.PostDomain;
import com.example.socialnetwork.infrastructure.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface PostDatabasePort {
    PostDomain createPost(PostDomain postDomain);
    PostDomain updatePost(PostDomain postDomain);
    void deletePost(Long postId);
//    List<PostDomain> getAllPostByUserId();

    PostDomain findById(Long id);

//    Page<PostDomain> getAllPostsOfOtherUser(Long otherUserId, int offset, int pageSize);
}
