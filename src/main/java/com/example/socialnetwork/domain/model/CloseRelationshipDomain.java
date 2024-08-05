package com.example.socialnetwork.domain.model;

import com.example.socialnetwork.common.constant.ECloseRelationship;
import com.example.socialnetwork.infrastructure.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CloseRelationshipDomain {
    private Long id;

    private UserDomain user;

    private UserDomain targetUser;

    private ECloseRelationship closeRelationshipName;
}
