package com.example.socialnetwork.domain.service;

import com.example.socialnetwork.common.constant.ERelationship;
import com.example.socialnetwork.common.constant.Visibility;
import com.example.socialnetwork.common.util.SecurityUtil;
import com.example.socialnetwork.domain.model.PostDomain;
import com.example.socialnetwork.domain.model.PostReactionDomain;
import com.example.socialnetwork.domain.model.RelationshipDomain;
import com.example.socialnetwork.domain.port.api.PostReactionServicePort;
import com.example.socialnetwork.domain.port.spi.PostDatabasePort;
import com.example.socialnetwork.domain.port.spi.PostReactionDatabasePort;
import com.example.socialnetwork.domain.port.spi.RelationshipDatabasePort;
import com.example.socialnetwork.exception.custom.ClientErrorException;
import com.example.socialnetwork.exception.custom.NotAllowException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@RequiredArgsConstructor
public class PostReactionServiceImpl implements PostReactionServicePort {

    private final PostReactionDatabasePort postReactionDatabasePort;
    private final PostDatabasePort postDatabasePort;
    private final RelationshipDatabasePort relationshipDatabasePort;

    @Override
    public PostReactionDomain createPostReaction(PostReactionDomain postReactionDomain) {
        Long userIdCurrent = SecurityUtil.getCurrentUserId();
        PostDomain postDomain = postDatabasePort.findById(postReactionDomain.getPostId());

        RelationshipDomain relationshipDomain = relationshipDatabasePort.find(userIdCurrent, postDomain.getUserId());
        if (relationshipDomain == null) {
            if(postDomain.getVisibility().equals(Visibility.PUBLIC)){
                return postReactionDatabasePort.createPostReaction(postReactionDomain);
            }else{
                throw new ClientErrorException("user does not have permission to post reaction");
            }
        }else if (relationshipDomain.getRelation().equals(ERelationship.FRIEND)){
            if(!postDomain.getVisibility().equals(Visibility.PRIVATE)){
                return postReactionDatabasePort.createPostReaction(postReactionDomain);
            }else{
                throw new ClientErrorException("user does not have permission to post reaction");
            }
        }else{
            throw new ClientErrorException("user does not have permission to post reaction");
        }
    }

    @Override
    public Boolean deletePostReaction(Long postReactionId) {
        Long userIdCurrent = SecurityUtil.getCurrentUserId();
        PostReactionDomain postReactionDomain = getPostReaction(postReactionId);
        if(userIdCurrent.equals(postReactionDomain.getUserId())){
            if (postReactionDatabasePort.deletePostReaction(postReactionId)){
                return true;
            }else {
                throw new ClientErrorException("user does not have permission to delete post reaction");
            }
        }else{
            throw new ClientErrorException("user does not have permission to delete post reaction");
        }

    }

    @Override
    public PostReactionDomain getPostReaction(Long postReactionId) {
            return postReactionDatabasePort.getPostReaction(postReactionId);
    }

    @Override
    public Page<PostReactionDomain> getAllPostReactions(int page, int pageSize, String sortBy, String sortDirection, Long postId, String postReactionType) {
        Sort.Direction direction = sortDirection.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Sort sort = Sort.by(direction, sortBy);
        Pageable pageable = PageRequest.of(page, pageSize, sort);

        Page<PostReactionDomain> postReactionDomains = postReactionDatabasePort.getAllPostReactions(page, pageSize, sort, postId, postReactionType);

        if (postReactionDomains != null) {
            return postReactionDomains;
        } else {
            throw new NotAllowException("You don't have permission to view this post reactions");
        }
    }


}
