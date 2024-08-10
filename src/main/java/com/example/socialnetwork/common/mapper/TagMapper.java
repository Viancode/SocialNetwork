package com.example.socialnetwork.common.mapper;

import com.example.socialnetwork.application.request.TagRequest;
import com.example.socialnetwork.application.response.TagResponse;
import com.example.socialnetwork.common.util.SecurityUtil;
import com.example.socialnetwork.domain.model.TagDomain;
import com.example.socialnetwork.infrastructure.entity.Tag;
import com.example.socialnetwork.infrastructure.repository.PostRepository;
import com.example.socialnetwork.infrastructure.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class TagMapper {
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    public TagDomain entityToDomain(Tag tag){
        TagDomain tagDomain = new TagDomain();
        tagDomain.setId(tag.getId());
        tagDomain.setPostId(tag.getPost().getId());
        tagDomain.setUserIdTagged(tag.getTaggedUser().getId());
        tagDomain.setUserIdTag(tag.getTaggedByUser().getId());
        tagDomain.setCreatedAt(tag.getCreatedAt());
        return tagDomain;
    }

    public Tag domainToEntity(TagDomain tagDomain){
        Tag tag = new Tag();
        tag.setPost(postRepository.findById(tagDomain.getPostId()).orElse(null));
        tag.setTaggedUser(userRepository.findUserById(tagDomain.getUserIdTagged()).orElse(null));
        tag.setTaggedByUser(userRepository.findUserById(tagDomain.getUserIdTag()).orElse(null));
        tag.setCreatedAt(tagDomain.getCreatedAt());
        return tag;
    }

    public TagResponse domainToResponse(TagDomain tagDomain){
        TagResponse tagResponse = new TagResponse();
        tagResponse.setId(tagDomain.getId());
        tagResponse.setUserIdTag(tagDomain.getUserIdTag());
        tagResponse.setUserIdTagged(tagDomain.getUserIdTagged());
        tagResponse.setPostId(tagDomain.getPostId());
        tagResponse.setCreatedAt(tagDomain.getCreatedAt());
        tagResponse.setUsernameTag(userRepository.findUserById(tagDomain.getUserIdTag()).orElse(null).getUsername());
        tagResponse.setUsernameTagged(userRepository.findUserById(tagDomain.getUserIdTagged()).orElse(null).getUsername());
        return tagResponse;
    }

    public TagDomain requestToDomain(TagRequest tagRequest){
        TagDomain tagDomain = new TagDomain();
        tagDomain.setPostId(tagRequest.getPostId());
        tagDomain.setUserIdTagged(tagRequest.getUserIdTagged());
        tagDomain.setUserIdTag(SecurityUtil.getCurrentUserId());
        tagDomain.setCreatedAt(LocalDateTime.now());
        return tagDomain;
    }


}
