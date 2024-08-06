package com.example.socialnetwork.infrastructure.adapter;

import com.example.socialnetwork.common.mapper.CloseRelationshipMapper;
import com.example.socialnetwork.domain.model.CloseRelationshipDomain;
import com.example.socialnetwork.domain.port.spi.CloseRelationshipDatabasePort;
import com.example.socialnetwork.infrastructure.entity.CloseRelationship;
import com.example.socialnetwork.infrastructure.repository.CloseRelationshipRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CloseRelationshipDatabaseAdapter implements CloseRelationshipDatabasePort {

    private final CloseRelationshipRepository closeRelationshipRepository;
    @Override
    public CloseRelationshipDomain createRelationship(CloseRelationshipDomain closeRelationshipDomain) {
        CloseRelationship closeRelationship = CloseRelationshipMapper.INSTANCE.domainToEntity(closeRelationshipDomain);
        return CloseRelationshipMapper.INSTANCE.entityToDomain(closeRelationshipRepository.save(closeRelationship));
    }
}
