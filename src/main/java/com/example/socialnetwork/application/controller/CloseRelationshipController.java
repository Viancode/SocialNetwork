package com.example.socialnetwork.application.controller;

import com.example.socialnetwork.application.request.CloseRelationshipRequest;
import com.example.socialnetwork.application.request.CommentReactionRequest;
import com.example.socialnetwork.application.response.CloseRelationshipResponse;
import com.example.socialnetwork.application.response.CommentReactionResponse;
import com.example.socialnetwork.application.response.ResultResponse;
import com.example.socialnetwork.common.mapper.CloseRelationshipMapper;
import com.example.socialnetwork.common.mapper.CommentReactionMapper;
import com.example.socialnetwork.domain.model.CloseRelationshipDomain;
import com.example.socialnetwork.domain.model.CommentReactionDomain;
import com.example.socialnetwork.domain.model.UserDomain;
import com.example.socialnetwork.domain.port.api.CloseRelationshipServicePort;
import com.example.socialnetwork.domain.port.api.UserServicePort;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/close_relationship")
@RequiredArgsConstructor
public class CloseRelationshipController extends BaseController {
    private final CloseRelationshipServicePort closeRelationshipServicePort;
    private final UserServicePort userServicePort;

    @PostMapping("")
    public ResponseEntity<?> createCommentReaction(
            @RequestBody CloseRelationshipRequest closeRelationshipRequest
    ) {
        UserDomain userDomain = userServicePort.findUserById(closeRelationshipRequest.getTargetUserId());
        CloseRelationshipDomain closeRelationshipDomain = closeRelationshipServicePort.createCloseRelationship(CloseRelationshipMapper.INSTANCE.requestToDomain(closeRelationshipRequest, userDomain));
        return buildResponse("Create close relationship successfully", CloseRelationshipMapper.INSTANCE.domainToResponse(closeRelationshipDomain));
    }

    @GetMapping("")
    public ResponseEntity<ResultResponse> getCloseRelationships(@RequestParam(defaultValue = "1") int page,
                                                              @RequestParam(defaultValue = "5") int pageSize,
                                                              @RequestParam(defaultValue = "createdAt") String sortBy,
                                                              @RequestParam(defaultValue = "desc") String sortDirection,
                                                              @RequestParam Long userId
    ) {
        Page<CloseRelationshipDomain> closeRelationshipDomains = closeRelationshipServicePort.getAllCloseRelationship(page, pageSize, sortBy, sortDirection, userId);

        Page<CloseRelationshipResponse> closeRelationshipResponses = closeRelationshipDomains.map(CloseRelationshipMapper.INSTANCE::domainToResponse);

        return buildResponse("Get close relationship successfully", closeRelationshipResponses);
    }

    @DeleteMapping("")
    public ResponseEntity<?> deleteCloseRelationship(
            @RequestParam Long targetUserId
    ){
        closeRelationshipServicePort.deleteCloseRelationship(targetUserId);
        return buildResponse("Delete close relationship successfully");
    }
}
