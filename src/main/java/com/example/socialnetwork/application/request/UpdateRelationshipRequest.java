package com.example.socialnetwork.application.request;

import com.example.socialnetwork.common.constant.ERelationship;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateRelationshipRequest {
    long friendId;
    String relationship;
}
