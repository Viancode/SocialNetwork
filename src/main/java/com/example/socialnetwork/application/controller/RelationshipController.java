package com.example.socialnetwork.application.controller;

import com.example.socialnetwork.application.request.FriendRequest;
import com.example.socialnetwork.application.request.MakeFriendRequest;
import com.example.socialnetwork.common.mapper.RelationshipMapper;
import com.example.socialnetwork.domain.port.api.RelationshipServicePort;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/friend")
@RequiredArgsConstructor
public class RelationshipController  extends BaseController{
    private final RelationshipServicePort relationshipService;
    private final RelationshipMapper relationshipMapper;

    @PostMapping("/send_request")
    public ResponseEntity<?> createRequest(@RequestBody MakeFriendRequest makeFriendRequest){
        relationshipService.sendRequestMakeFriendship(makeFriendRequest.getSenderId(), makeFriendRequest.getReceiverId());
        return buildResponse("Sent friend request successfully");
    }

    @PostMapping("/delete_request")
    public ResponseEntity<?> deleteRequest(@RequestBody MakeFriendRequest makeFriendRequest){
        relationshipService.deleteRequestMakeFriendship(makeFriendRequest.getSenderId(), makeFriendRequest.getReceiverId());
        return buildResponse("Delete request successfully");
    }

    @PostMapping("/accept_request")
    public ResponseEntity<?> acceptRequest(@RequestBody MakeFriendRequest makeFriendRequest){
        relationshipService.acceptRequestMakeFriendship(makeFriendRequest.getSenderId(), makeFriendRequest.getReceiverId());
        return buildResponse("Accept the friend request successfully");
    }

    @PostMapping("/refuse_request")
    public ResponseEntity<?> refuseRequest(@RequestBody MakeFriendRequest makeFriendRequest){
        relationshipService.refuseRequestMakeFriendship(makeFriendRequest.getSenderId(), makeFriendRequest.getReceiverId());
        return buildResponse("Refuse the friend request successfully");
    }

    @GetMapping("/get_list_requests")
    public ResponseEntity<?> getListRequest(@RequestParam long userId){
        return buildResponse("Get list requests successfully", relationshipMapper.toResponse(relationshipService.getListRequest(userId)));
    }

    @GetMapping("/get_list_friends")
    public ResponseEntity<?> getListFriend(@RequestParam long userId){
        return buildResponse("Get list friends successfully", relationshipService.getListFriend(userId));
    }


    @PostMapping("/delete_friend")
    public ResponseEntity<?> removeFriend(@RequestBody FriendRequest friendRequest){
        relationshipService.deleteRelationship(friendRequest.getUserId(), friendRequest.getFriendId());
        return buildResponse("Delete friend successfully");
    }

    @PostMapping("/block")
    public ResponseEntity<?> block(@RequestBody FriendRequest friendRequest){
        relationshipService.block(friendRequest.getUserId(), friendRequest.getFriendId());
        return buildResponse("Block friend successfully");
    }
}
