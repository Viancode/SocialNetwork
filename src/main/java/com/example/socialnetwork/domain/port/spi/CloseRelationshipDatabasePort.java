package com.example.socialnetwork.domain.port.spi;

import com.example.socialnetwork.domain.model.CloseRelationshipDomain;

public interface CloseRelationshipDatabasePort {
    CloseRelationshipDomain createRelationship(CloseRelationshipDomain relationship);
}
