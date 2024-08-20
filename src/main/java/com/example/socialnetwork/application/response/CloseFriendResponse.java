package com.example.socialnetwork.application.response;

import com.example.socialnetwork.common.constant.ECloseRelationship;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CloseFriendResponse {
    Long id;
    String avatar;
    String username;
    String email;
    ECloseRelationship closeRelationship;
}
