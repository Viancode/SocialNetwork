package com.example.socialnetwork.infrastructure.adapter;

import com.example.socialnetwork.common.mapper.TagMapper;
import com.example.socialnetwork.domain.model.TagDomain;
import com.example.socialnetwork.domain.port.spi.TagDatabasePort;
import com.example.socialnetwork.exception.custom.NotFoundException;
import com.example.socialnetwork.infrastructure.entity.Tag;
import com.example.socialnetwork.infrastructure.repository.TagRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class TagDatabaseAdapter implements TagDatabasePort {
    private final TagRepository tagRepository;
    private final TagMapper tagMapper;

    @Override
    public TagDomain createTag(TagDomain tagDomain) {
        Tag tag = tagRepository.save(tagMapper.tagDomainToTag(tagDomain));
        return tagMapper.tagToTagDomain(tag);
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
