package com.example.socialnetwork.domain.port.spi;

import com.example.socialnetwork.domain.model.TagDomain;

public interface TagDatabasePort {
    TagDomain createTag(TagDomain tagDomain);

    void deleteTag(Long tagId);
}
