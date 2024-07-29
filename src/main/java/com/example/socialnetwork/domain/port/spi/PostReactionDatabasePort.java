package com.example.socialnetwork.domain.port.spi;

import com.example.socialnetwork.domain.model.PostDomain;
import com.example.socialnetwork.domain.model.PostReactionDomain;
import com.example.socialnetwork.infrastructure.entity.PostReaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

public interface PostReactionDatabasePort {
    PostReactionDomain createPostReaction(PostReactionDomain postReactionDomain);
    Boolean deletePostReaction(Long postReactionId);
    PostReactionDomain getPostReaction(Long postReactionId);
    Page<PostReactionDomain> getAllPostReactions(int page, int pageSize, Sort sort, Long postId, String postReactionType);

}
