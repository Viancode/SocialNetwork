package com.example.socialnetwork.domain.service;

import com.example.socialnetwork.domain.model.TagDomain;
import com.example.socialnetwork.domain.port.api.TagServicePort;
import com.example.socialnetwork.domain.port.spi.TagDatabasePort;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class TagServiceImpl implements TagServicePort {
    private final TagDatabasePort tagDatabasePort;

    @Override
    public TagDomain createTag(TagDomain tagDomain) {
        return tagDatabasePort.createTag(tagDomain);
    }

    @Override
    public void deleteTag(Long tagId) {
        tagDatabasePort.deleteTag(tagId);
    }
}
