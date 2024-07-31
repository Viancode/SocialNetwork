package com.example.socialnetwork.common.mapper;

import com.example.socialnetwork.domain.model.RelationshipDomain;
import com.example.socialnetwork.infrastructure.entity.Relationship;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RelationshipMapper {
    RelationshipDomain toRelationshipDomain(Relationship relationship);

    List<RelationshipDomain> toRelationshipDomain(List<Relationship> relationships);
}
