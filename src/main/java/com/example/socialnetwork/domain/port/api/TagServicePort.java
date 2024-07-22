package com.example.socialnetwork.domain.port.api;

import com.example.socialnetwork.domain.model.TagDomain;

public interface TagServicePort {
    TagDomain createTag(TagDomain tag);

    void deleteTag(Long tagId);
}
