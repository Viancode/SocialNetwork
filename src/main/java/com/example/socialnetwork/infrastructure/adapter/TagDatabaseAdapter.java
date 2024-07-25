package com.example.socialnetwork.infrastructure.adapter;

import com.example.socialnetwork.common.mapper.TagMapper;
import com.example.socialnetwork.domain.model.TagDomain;
import com.example.socialnetwork.domain.port.spi.TagDatabasePort;
import com.example.socialnetwork.exception.custom.ClientErrorException;
import com.example.socialnetwork.exception.custom.NotFoundException;
import com.example.socialnetwork.infrastructure.entity.Post;
import com.example.socialnetwork.infrastructure.entity.Relationship;
import com.example.socialnetwork.infrastructure.entity.Tag;
import com.example.socialnetwork.infrastructure.repository.PostRepository;
import com.example.socialnetwork.infrastructure.repository.RelationshipRepository;
import com.example.socialnetwork.infrastructure.repository.TagRepository;
import lombok.RequiredArgsConstructor;

import java.util.Objects;

@RequiredArgsConstructor
public class TagDatabaseAdapter implements TagDatabasePort {
    private final TagRepository tagRepository;
    private final TagMapper tagMapper;
    private final PostRepository postRepository;
    private final RelationshipRepository relationshipRepository;

    @Override
    public TagDomain createTag(TagDomain tagDomain) {
        Post post = postRepository.findById(tagDomain.getPostId()).orElseThrow(() -> new NotFoundException("Post not found"));
        Relationship relationship1 = relationshipRepository.findByUser_IdAndFriend_Id(tagDomain.getTaggedByUserId(), tagDomain.getTaggedUserId());
        Relationship relationship2 = relationshipRepository.findByUser_IdAndFriend_Id( tagDomain.getTaggedUserId(), tagDomain.getTaggedByUserId());

//        if ( (relationship1 != null && !relationship1.getRelation().equals("FRIEND")) || (relationship2 != null && !relationship2.getRelation().equals("FRIEND"))){
//            throw new ClientErrorException("User is not friend");
//        }

        if(Objects.equals(post.getUser().getId(), tagDomain.getTaggedByUserId())){
            Tag tag = tagRepository
                    .findByTaggedByUserIdAndTaggedUserIdAndPostId(tagDomain.getTaggedByUserId(), tagDomain.getTaggedUserId(), tagDomain.getPostId()).orElse(null);
            if(tag != null){
                throw new ClientErrorException("Tag already exists");
            }else{
                Tag tagEntity = tagRepository.save(tagMapper.tagDomainToTag(tagDomain));
                return tagMapper.tagToTagDomain(tagEntity);
            }
        }else {
            throw new ClientErrorException("User can not tagged by this post");
        }
    }

    @Override
    public void deleteTag(Long tagId) {
        if (tagRepository.existsById(tagId)) {
            tagRepository.deleteById(tagId);
        }else {
            throw new NotFoundException("Tag not found");
        }
    }
}
