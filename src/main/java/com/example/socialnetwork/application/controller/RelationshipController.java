package com.example.socialnetwork.application.controller;

import com.example.socialnetwork.application.response.NumberOfFriendsResponse;
import com.example.socialnetwork.common.mapper.RelationshipMapper;
import com.example.socialnetwork.common.mapper.SuggestionMapper;
import com.example.socialnetwork.common.mapper.UserMapper;
import com.example.socialnetwork.domain.port.api.RelationshipServicePort;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/friend")
@RequiredArgsConstructor
public class RelationshipController  extends BaseController{
    private final RelationshipServicePort relationshipService;
    private final UserMapper userMapper;
    private final RelationshipMapper relationshipMapper;
    private final SuggestionMapper suggestionMapper;

    @PostMapping("/send_request")
    public ResponseEntity<?> createRequest(@RequestParam(value = "userId") long userId){
        relationshipService.sendRequestMakeFriendship(userId);
        return buildResponse("Sent friend request successfully");
    }

    @DeleteMapping("/delete_request")
    public ResponseEntity<?> deleteRequest(@RequestParam(value = "userId") long userId){
        relationshipService.deleteRequestMakeFriendship(userId);
        return buildResponse("Delete request successfully");
    }

    @PostMapping("/accept_request")
    public ResponseEntity<?> acceptRequest(@RequestParam(value = "userId") long userId){
        relationshipService.acceptRequestMakeFriendship(userId);
        return buildResponse("Accept the friend request successfully");
    }

    @PostMapping("/refuse_request")
    public ResponseEntity<?> refuseRequest(@RequestParam(value = "userId") long userId){
        relationshipService.refuseRequestMakeFriendship(userId);
        return buildResponse("Refuse the friend request successfully");
    }

    @GetMapping("/get_list_receive_requests")
    public ResponseEntity<?> getListReceiveRequest(@RequestParam(value = "page", defaultValue = "1") int page,
                                                   @RequestParam(value = "pageSize", defaultValue = "5") int pageSize){
        return buildResponse("Get list receive requests successfully", relationshipService.getListReceiveRequest(page, pageSize).map(userMapper::toFriendResponse));
    }

    @GetMapping("/get_list_send_requests")
    public ResponseEntity<?> getListSendRequest(@RequestParam(value = "page", defaultValue = "1") int page,
                                                @RequestParam(value = "pageSize", defaultValue = "5") int pageSize){
        return buildResponse("Get list receive requests successfully", relationshipService.getListSendRequest(page, pageSize).map(userMapper::toFriendResponse));
    }

    @GetMapping("/get_list_friends")
    public ResponseEntity<?> getListFriend(@RequestParam(value = "page", defaultValue = "1") int page,
                                           @RequestParam(value = "pageSize", defaultValue = "5") int pageSize,
                                           @RequestParam(value = "sortBy", defaultValue = "createdAt") String sortBy,
                                           @RequestParam(value = "sortDirection", defaultValue = "desc") String sortDirection,
                                           @RequestParam(value = "userId") long userId){
        return buildResponse("Get list friends successfully", relationshipService.getListFriend(page, pageSize, userId, sortDirection, sortBy).map(UserMapper.INSTANCE::toFriendResponse));
    }

    @GetMapping("/get_list_block")
    public ResponseEntity<?> getListBlock(@RequestParam(value = "page", defaultValue = "1") int page,
                                          @RequestParam(value = "pageSize", defaultValue = "5") int pageSize,
                                          @RequestParam(value = "sortDirection", defaultValue = "desc") String sortDirection,
                                          @RequestParam(value = "sortBy", defaultValue = "createdAt") String sortBy){
        return buildResponse("Get list users blocked successfully", relationshipService.getListBlock(page, pageSize, sortDirection, sortBy).map(UserMapper.INSTANCE::toFriendResponse));
    }

    @DeleteMapping("/delete_friend")
    public ResponseEntity<?> removeFriend(@RequestParam(value = "userId") long userId){
        relationshipService.deleteRelationship(userId);
        return buildResponse("Delete friend successfully");
    }

    @PostMapping("/block")
    public ResponseEntity<?> block(@RequestParam(value = "userId") long userId){
        relationshipService.block(userId);
        return buildResponse("Block user successfully");
    }

    @PostMapping("/unblock")
    public ResponseEntity<?> unblock(@RequestParam(value = "userId") long userId){
        relationshipService.unblock(userId);
        return buildResponse("UnBlock user successfully");
    }

    @GetMapping("/view_suggest")
    public ResponseEntity<?> viewSuggest(@RequestParam(value = "page", defaultValue = "1") int page,
                                         @RequestParam(value = "pageSize", defaultValue = "5") int pageSize
    ){
        return buildResponse("Get friend suggestions successfully", relationshipService.getFriendSuggestions(page, pageSize));
    }

    @GetMapping("/find_friend")
    public ResponseEntity<?> findFriend(@RequestParam(value = "page", defaultValue = "1") int page,
                                        @RequestParam(value = "pageSize", defaultValue = "5") int pageSize,
                                        @RequestParam("keyWord") String keyWord){
        return buildResponse("Find friend successfully", relationshipService.findFriend(page, pageSize, keyWord).map(userMapper::toFriendResponse));
    }

    @GetMapping("/number_of_friends")
    public ResponseEntity<?> getNumberOfFriends(){
        return buildResponse("Get number of friends successfully", relationshipService.getNumberOfFriend());
    }
}
