package com.example.socialnetwork.domain.port.api;

import com.example.socialnetwork.domain.model.CloseRelationshipDomain;

public interface CloseRelationshipServicePort {
    CloseRelationshipDomain createCloseRelationship(CloseRelationshipDomain closeRelationshipDomain);
}
