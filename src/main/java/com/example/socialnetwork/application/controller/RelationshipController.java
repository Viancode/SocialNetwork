package com.example.socialnetwork.application.controller;

import com.example.socialnetwork.application.request.MakeFriendRequest;
import com.example.socialnetwork.application.request.UpdateRelationshipRequest;
import com.example.socialnetwork.common.constant.ERelationship;
import com.example.socialnetwork.common.mapper.RelationshipMapper;
import com.example.socialnetwork.domain.port.api.RelationshipServicePort;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/friend")
@RequiredArgsConstructor
public class RelationshipController  extends BaseController{
    private final RelationshipServicePort relationshipService;
    private final RelationshipMapper relationshipMapper;

    @PostMapping("/send_request")
    public ResponseEntity<?> createRequest(@RequestBody MakeFriendRequest makeFriendRequest, Authentication authentication){
        User user = (User) authentication.getPrincipal();
        System.out.println(user.getUsername());
        System.out.println(makeFriendRequest.getFriendId());
        if(relationshipService.sendRequestMakeFriendship(Long.parseLong(user.getUsername()), makeFriendRequest.getFriendId()))
            return buildResponse("Sent friend request successfully");
        else return buildResponse("error");
    }

    @PostMapping("/delete_request")
    public ResponseEntity<?> deleteRequest(@RequestBody MakeFriendRequest makeFriendRequest, Authentication authentication){
        User user = (User) authentication.getPrincipal();
        if(relationshipService.deleteRequestMakeFriendship(Long.parseLong(user.getUsername()), makeFriendRequest.getFriendId()))
            return buildResponse();
        else return buildResponse("error");
    }

    @PostMapping("/accept_request")
    public ResponseEntity<?> acceptRequest(@RequestBody MakeFriendRequest makeFriendRequest, Authentication authentication){
        User user = (User) authentication.getPrincipal();
        if(relationshipService.acceptRequestMakeFriendship(Long.parseLong(user.getUsername()), makeFriendRequest.getFriendId()))
            return buildResponse("Accept the friend request successfully");
        else return buildResponse("error");
    }

    @PostMapping("/refuse_request")
    public ResponseEntity<?> refuseRequest(@RequestBody MakeFriendRequest makeFriendRequest, Authentication authentication){
        User user = (User) authentication.getPrincipal();
        if(relationshipService.refuseRequestMakeFriendship(Long.parseLong(user.getUsername()), makeFriendRequest.getFriendId()))
            return buildResponse("Refuse the friend request successfully");
        else return buildResponse("error");
    }

    @GetMapping("/get_list_requests")
    public ResponseEntity<?> getListRequest(Authentication authentication){
        User user = (User) authentication.getPrincipal();
        return ResponseEntity.ok().body(relationshipMapper.toResponse(relationshipService.getListRequest(Long.parseLong(user.getUsername()))));
    }

    @GetMapping("/get_list_friends")
    public ResponseEntity<?> getListFriend(Authentication authentication){
        User user = (User) authentication.getPrincipal();
        return ResponseEntity.ok().body(relationshipService.getListFriend(Long.parseLong(user.getUsername())));
    }


    @PostMapping("/delete_friend")
    public ResponseEntity<?> removeFriend(@RequestBody MakeFriendRequest makeFriendRequest, Authentication authentication){
        User user = (User) authentication.getPrincipal();
        if(relationshipService.deleteRelationship(Long.parseLong(user.getUsername()), makeFriendRequest.getFriendId()))
            return buildResponse("Delete friend successfully");
        else return buildResponse("error");
    }

    @PostMapping("/update_relationship")
    public ResponseEntity<?> updateRelationship(@RequestBody UpdateRelationshipRequest updateRelationshipRequest, Authentication authentication){
        User user = (User) authentication.getPrincipal();
        ERelationship relationship = ERelationship.valueOf(updateRelationshipRequest.getRelationship().toUpperCase());
        if(relationshipService.updateRelationship(Long.parseLong(user.getUsername()), updateRelationshipRequest.getFriendId(), relationship.name()))
            return buildResponse("Update relationship successfully");
        else return buildResponse("error");
    }

    @GetMapping("/search")
    public ResponseEntity<?> search(@RequestParam(value = "name") String name, Authentication authentication){
        User user = (User) authentication.getPrincipal();
        return ResponseEntity.ok().body(relationshipService.search(Long.parseLong(user.getUsername()), name));
    }

    @GetMapping("/view_friend_suggestions")
    public ResponseEntity<?> suggestFriend(Authentication authentication){
        User user = (User) authentication.getPrincipal();
        return ResponseEntity.ok().body(relationshipService.viewSuggest(Long.parseLong(user.getUsername())));
    }
}
