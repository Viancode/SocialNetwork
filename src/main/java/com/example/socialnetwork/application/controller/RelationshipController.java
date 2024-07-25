package com.example.socialnetwork.application.controller;

import com.example.socialnetwork.common.mapper.RelationshipMapper;
import com.example.socialnetwork.common.mapper.UserMapper;
import com.example.socialnetwork.domain.port.api.RelationshipServicePort;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/friend")
@RequiredArgsConstructor
public class RelationshipController  extends BaseController{
    private final RelationshipServicePort relationshipService;
    private final RelationshipMapper relationshipMapper;
    private final UserMapper userMapper;

    @PostMapping("/send_request")
    public ResponseEntity<?> createRequest(@RequestParam long userId){
        relationshipService.sendRequestMakeFriendship(userId);
        return buildResponse("Sent friend request successfully");
    }

    @DeleteMapping("/delete_request")
    public ResponseEntity<?> deleteRequest(@RequestParam long userId){
        relationshipService.deleteRequestMakeFriendship(userId);
        return buildResponse("Delete request successfully");
    }

    @PostMapping("/accept_request")
    public ResponseEntity<?> acceptRequest(@RequestParam long userId){
        relationshipService.acceptRequestMakeFriendship(userId);
        return buildResponse("Accept the friend request successfully");
    }

    @PostMapping("/refuse_request")
    public ResponseEntity<?> refuseRequest(@RequestParam long userId){
        relationshipService.refuseRequestMakeFriendship(userId);
        return buildResponse("Refuse the friend request successfully");
    }

    @GetMapping("/get_list_receive_requests")
    public ResponseEntity<?> getListReceiveRequest(){
        return buildResponse("Get list receive requests successfully", relationshipMapper.toResponse(relationshipService.getListReceiveRequest()));
    }

    @GetMapping("/get_list_send_requests")
    public ResponseEntity<?> getListSendRequest(){
        return buildResponse("Get list receive requests successfully", relationshipMapper.toResponse(relationshipService.getListSendRequest()));
    }

    @GetMapping("/get_list_friends")
    public ResponseEntity<?> getListFriend(@RequestParam long userId){
        return buildResponse("Get list friends successfully", userMapper.toFriendResponses(relationshipService.getListFriend(userId)));
    }


    @DeleteMapping("/delete_friend")
    public ResponseEntity<?> removeFriend(@RequestParam long userId){
        relationshipService.deleteRelationship(userId);
        return buildResponse("Delete friend successfully");
    }

    @PostMapping("/block")
    public ResponseEntity<?> block(@RequestParam long userId){
        relationshipService.block(userId);
        return buildResponse("Block user successfully");
    }

    @GetMapping("view_suggest")
    public ResponseEntity<?> viewSuggest(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        long userId = Long.parseLong(user.getUsername());
        return buildResponse("Get friend suggestions successfully", userMapper.toFriendResponses(relationshipService.getFriendSuggestions(userId)));
    }

    @GetMapping("find_friend")
    public ResponseEntity<?> findFriend(@RequestParam String keyWord){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        long userId = Long.parseLong(user.getUsername());
        return buildResponse("Find friend successfully", userMapper.toFriendResponses(relationshipService.findFriend(userId, keyWord)));
    }
}
