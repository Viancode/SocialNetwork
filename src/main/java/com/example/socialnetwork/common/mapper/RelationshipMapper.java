package com.example.socialnetwork.common.mapper;

import com.example.socialnetwork.application.response.MakeFriendResponse;
import com.example.socialnetwork.common.constant.ERelationship;
import com.example.socialnetwork.domain.model.RelationshipDomain;
import com.example.socialnetwork.infrastructure.entity.Relationship;
import org.mapstruct.Mapper;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Mapper(componentModel = "spring")
public interface RelationshipMapper {
    RelationshipDomain toRelationshipDomain(Relationship relationship);

    List<RelationshipDomain> toRelationshipDomain(List<Relationship> relationships);

    default List<MakeFriendResponse> toResponse(List<RelationshipDomain> relationshipDomains) {
        if (relationshipDomains == null) {
            return null;
        } else {
            List<MakeFriendResponse> list = new ArrayList<>(relationshipDomains.size());

            for (RelationshipDomain relationshipDomain : relationshipDomains) {
                MakeFriendResponse makeFriendResponse = new MakeFriendResponse();
                makeFriendResponse.setId(relationshipDomain.getId());
                makeFriendResponse.setEmail(relationshipDomain.getUser().getEmail());
                makeFriendResponse.setUsername(relationshipDomain.getUser().getUsername());
                makeFriendResponse.setLastName(relationshipDomain.getUser().getLastName());
                makeFriendResponse.setFirstName(relationshipDomain.getUser().getFirstName());
                list.add(makeFriendResponse);
            }
            return list;
        }
    }
}
