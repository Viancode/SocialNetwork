package com.example.socialnetwork.application.controller;

import com.example.socialnetwork.application.request.CloseRelationshipRequest;
import com.example.socialnetwork.application.request.CommentReactionRequest;
import com.example.socialnetwork.common.mapper.CloseRelationshipMapper;
import com.example.socialnetwork.common.mapper.CommentReactionMapper;
import com.example.socialnetwork.domain.model.CloseRelationshipDomain;
import com.example.socialnetwork.domain.model.CommentReactionDomain;
import com.example.socialnetwork.domain.model.UserDomain;
import com.example.socialnetwork.domain.port.api.CloseRelationshipServicePort;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/close_relationship")
@RequiredArgsConstructor
public class CloseRelationshipController extends BaseController {
    private final CloseRelationshipServicePort closeRelationshipServicePort;

    @PostMapping("")
    public ResponseEntity<?> createCommentReaction(
            @RequestBody CloseRelationshipRequest closeRelationshipRequest
    ) {
        CloseRelationshipDomain closeRelationshipDomain = closeRelationshipServicePort.createCloseRelationship(CloseRelationshipMapper.INSTANCE.requestToDomain(closeRelationshipRequest));
        return buildResponse("Create comment reaction successfully", CloseRelationshipMapper.INSTANCE.domainToResponse(closeRelationshipDomain));
    }
}
