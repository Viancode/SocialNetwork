package com.example.socialnetwork.common.mapper;

import com.example.socialnetwork.domain.model.TagDomain;
import com.example.socialnetwork.infrastructure.entity.Tag;
import com.example.socialnetwork.infrastructure.repository.PostRepository;
import com.example.socialnetwork.infrastructure.repository.TagRepository;
import com.example.socialnetwork.infrastructure.repository.UserRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class TagMapper {
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    public TagDomain tagToTagDomain(Tag tag){
        TagDomain tagDomain = new TagDomain();
        tagDomain.setId(tag.getId());
        tagDomain.setPostId(tag.getPost().getId());
        tagDomain.setTaggedUserId(tag.getTaggedUser().getId());
        tagDomain.setTaggedByUserId(tag.getTaggedByUser().getId());
        return tagDomain;
    }

    public Tag tagDomainToTag(TagDomain tagDomain){
        Tag tag = new Tag();
        tag.setPost(postRepository.findById(tagDomain.getPostId()).orElse(null));
        tag.setTaggedUser(userRepository.findUserById(tagDomain.getTaggedUserId()).orElse(null));
        tag.setTaggedByUser(userRepository.findUserById(tagDomain.getTaggedByUserId()).orElse(null));
        return tag;
    }
}
