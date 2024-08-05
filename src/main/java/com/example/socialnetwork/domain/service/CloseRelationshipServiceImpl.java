package com.example.socialnetwork.domain.service;

import com.example.socialnetwork.common.util.SecurityUtil;
import com.example.socialnetwork.domain.model.CloseRelationshipDomain;
import com.example.socialnetwork.domain.model.UserDomain;
import com.example.socialnetwork.domain.port.api.CloseRelationshipServicePort;
import com.example.socialnetwork.domain.port.spi.CloseRelationshipDatabasePort;
import com.example.socialnetwork.domain.port.spi.RelationshipDatabasePort;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class CloseRelationshipServiceImpl implements CloseRelationshipServicePort {
    private final CloseRelationshipDatabasePort closeRelationshipDatabasePort;

    private final RelationshipDatabasePort relationshipDatabasePort;

    @Override
    public CloseRelationshipDomain createCloseRelationship(CloseRelationshipDomain closeRelationshipDomain) {
//        Long currentUserId = SecurityUtil.getCurrentUserId();
//        List<UserDomain> userDomainList = relationshipDatabasePort.getListBlock(currentUserId);
//        if(!userDomainList.isEmpty()) {
//            if(userDomainList.contains(closeRelationshipDomain.getUser())) {}
//        }
        return closeRelationshipDatabasePort.createRelationship(closeRelationshipDomain);
    }
}
