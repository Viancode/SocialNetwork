package com.example.socialnetwork.application.controller;

import com.example.socialnetwork.application.response.ResultResponse;
import com.example.socialnetwork.common.mapper.RelationshipMapper;
import com.example.socialnetwork.common.mapper.SuggestionMapper;
import com.example.socialnetwork.common.mapper.UserMapper;
import com.example.socialnetwork.domain.port.api.RelationshipServicePort;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
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

    @Operation(
            summary = "Send making friend request to user",
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ResultResponse.class),
                                    examples = @ExampleObject(
                                            value = """
                                                        {
                                                            "status": 200,
                                                            "message": "Sent friend request successfully",
                                                            "result": {},
                                                            "timestamp": "2024-08-13T00:41:22.073430290Z"
                                                        }
                                                    """
                                    )
                            )
                    ),
                    @ApiResponse(description = "The access token provided is expired, revoked, malformed, or invalid for other reasons.",
                            responseCode = "401", content = @Content()),
                    @ApiResponse(description = "", responseCode = "404", content = @Content()),
                    @ApiResponse(description = "", responseCode = "400", content = @Content())
            }
    )
    @PostMapping("/send_request")
    public ResponseEntity<?> createRequest(@RequestParam(value = "user_id") long userId){
        relationshipService.sendRequestMakeFriendship(userId);
        return buildResponse("Sent friend request successfully");
    }

    @Operation(
            summary = "Delete making friend request",
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ResultResponse.class),
                                    examples = @ExampleObject(
                                            value = """
                                                        {
                                                            "status": 200,
                                                            "message": "Delete request successfully",
                                                            "result": {},
                                                            "timestamp": "2024-08-13T00:41:22.073430290Z"
                                                        }
                                                    """
                                    )
                            )
                    ),
                    @ApiResponse(description = "The access token provided is expired, revoked, malformed, or invalid for other reasons.",
                            responseCode = "401", content = @Content()),
                    @ApiResponse(description = "", responseCode = "404", content = @Content()),
                    @ApiResponse(description = "", responseCode = "400", content = @Content())
            }
    )
    @DeleteMapping("/delete_request")
    public ResponseEntity<?> deleteRequest(@RequestParam(value = "user_id") long userId){
        relationshipService.deleteRequestMakeFriendship(userId);
        return buildResponse("Delete request successfully");
    }

    @Operation(
            summary = "Accept a making friend request",
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ResultResponse.class),
                                    examples = @ExampleObject(
                                            value = """
                                                        {
                                                            "status": 200,
                                                            "message": "Accept the friend request successfully",
                                                            "result": {},
                                                            "timestamp": "2024-08-13T00:41:22.073430290Z"
                                                        }
                                                    """
                                    )
                            )
                    ),
                    @ApiResponse(description = "The access token provided is expired, revoked, malformed, or invalid for other reasons.",
                            responseCode = "401", content = @Content()),
                    @ApiResponse(description = "", responseCode = "404", content = @Content()),
                    @ApiResponse(description = "", responseCode = "400", content = @Content())
            }
    )
    @PostMapping("/accept_request")
    public ResponseEntity<?> acceptRequest(@RequestParam(value = "user_id") long userId){
        relationshipService.acceptRequestMakeFriendship(userId);
        return buildResponse("Accept the friend request successfully");
    }

    @Operation(
            summary = "Refuse a making friend request",
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ResultResponse.class),
                                    examples = @ExampleObject(
                                            value = """
                                                        {
                                                            "status": 200,
                                                            "message": "Refuse the friend request successfully",
                                                            "result": {},
                                                            "timestamp": "2024-08-13T00:41:22.073430290Z"
                                                        }
                                                    """
                                    )
                            )
                    ),
                    @ApiResponse(description = "The access token provided is expired, revoked, malformed, or invalid for other reasons.",
                            responseCode = "401", content = @Content()),
                    @ApiResponse(description = "", responseCode = "404", content = @Content()),
                    @ApiResponse(description = "", responseCode = "400", content = @Content())
            }
    )
    @PostMapping("/refuse_request")
    public ResponseEntity<?> refuseRequest(@RequestParam(value = "user_id") long userId){
        relationshipService.refuseRequestMakeFriendship(userId);
        return buildResponse("Refuse the friend request successfully");
    }

    @Operation(
            summary = "Get list received making friend requests",
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ResultResponse.class),
                                    examples = @ExampleObject(
                                            value = """
                                                        {
                                                            "status": 200,
                                                            "message": "Get list receive requests successfully",
                                                            "result": {
                                                                "pageMeta": {
                                                                    "page": 1,
                                                                    "pageSize": 5,
                                                                    "totalElements": 0,
                                                                    "totalPages": 0,
                                                                    "hasPrev": false,
                                                                    "hasNext": false
                                                                },
                                                                "data": []
                                                            },
                                                            "timestamp": "2024-08-25T23:36:00.546278800Z"
                                                        }
                                                    """
                                    )
                            )
                    ),
                    @ApiResponse(description = "The access token provided is expired, revoked, malformed, or invalid for other reasons.",
                            responseCode = "401", content = @Content()),
                    @ApiResponse(description = "", responseCode = "404", content = @Content()),
                    @ApiResponse(description = "", responseCode = "400", content = @Content())
            }
    )
    @GetMapping("/get_list_receive_requests")
    public ResponseEntity<?> getListReceiveRequest(@RequestParam(value = "page", defaultValue = "1") int page,
                                                   @RequestParam(value = "page_size", defaultValue = "10") int pageSize){
        return buildResponse("Get list receive requests successfully", relationshipService.getListReceiveRequest(page, pageSize));
    }

    @Operation(
            summary = "Get list accepted making friend requests",
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ResultResponse.class),
                                    examples = @ExampleObject(
                                            value = """
                                                        {
                                                            "status": 200,
                                                            "message": "Get list receive requests successfully",
                                                            "result": {
                                                                "pageMeta": {
                                                                    "page": 1,
                                                                    "pageSize": 10,
                                                                    "totalElements": 3,
                                                                    "totalPages": 1,
                                                                    "hasPrev": false,
                                                                    "hasNext": false
                                                                },
                                                                "data": [
                                                                    {
                                                                        "id": 10,
                                                                        "avatar": null,
                                                                        "username": "Duyên Dương",
                                                                        "email": "duyên.dương95@example.com",
                                                                        "mutualFriends": 2,
                                                                        "status": "REQUESTING",
                                                                        "closeRelationship": null
                                                                    },
                                                                    {
                                                                        "id": 16,
                                                                        "avatar": null,
                                                                        "username": "Hiếu Võ",
                                                                        "email": "hiếu.võ3135@example.com",
                                                                        "mutualFriends": 4,
                                                                        "status": "REQUESTING",
                                                                        "closeRelationship": null
                                                                    },
                                                                    {
                                                                        "id": 45,
                                                                        "avatar": null,
                                                                        "username": "Chi Huỳnh",
                                                                        "email": "chi.huỳnh9723@example.com",
                                                                        "mutualFriends": 2,
                                                                        "status": "REQUESTING",
                                                                        "closeRelationship": null
                                                                    }
                                                                ]
                                                            },
                                                            "timestamp": "2024-08-25T23:35:54.287422Z"
                                                        }
                                                    """
                                    )
                            )
                    ),
                    @ApiResponse(description = "The access token provided is expired, revoked, malformed, or invalid for other reasons.",
                            responseCode = "401", content = @Content()),
                    @ApiResponse(description = "", responseCode = "404", content = @Content()),
                    @ApiResponse(description = "", responseCode = "400", content = @Content())
            }
    )
    @GetMapping("/get_list_send_requests")
    public ResponseEntity<?> getListSendRequest(@RequestParam(value = "page", defaultValue = "1") int page,
                                                @RequestParam(value = "page_size", defaultValue = "10") int pageSize){
        return buildResponse("Get list receive requests successfully", relationshipService.getListSendRequest(page, pageSize));
    }

    @Operation(
            summary = "Get list friends of user",
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ResultResponse.class),
                                    examples = @ExampleObject(
                                            value = """
                                                        {
                                                            "status": 200,
                                                            "message": "Get list friends successfully",
                                                            "result": {
                                                                "pageMeta": {
                                                                    "page": 1,
                                                                    "pageSize": 15,
                                                                    "totalElements": 19,
                                                                    "totalPages": 2,
                                                                    "hasPrev": false,
                                                                    "hasNext": true
                                                                },
                                                                "data": [
                                                                    {
                                                                        "id": 28,
                                                                        "avatar": null,
                                                                        "username": "Hằng Bùi",
                                                                        "email": "hằng.bùi1426@example.com",
                                                                        "mutualFriends": 1,
                                                                        "status": "FRIEND",
                                                                        "closeRelationship": null
                                                                    },
                                                                    {
                                                                        "id": 53,
                                                                        "avatar": "https://ghtk-socialnetwork.s3.ap-southeast-2.amazonaws.com/images/02282ff2-7a79-4e60-92c4-38fd148c711a.png",
                                                                        "username": "Duyên Ngô",
                                                                        "email": "duyên.ngô6794@example.com",
                                                                        "mutualFriends": 0,
                                                                        "status": "FRIEND",
                                                                        "closeRelationship": "FATHER"
                                                                    },
                                                                    {
                                                                        "id": 52,
                                                                        "avatar": "https://ghtk-socialnetwork.s3.ap-southeast-2.amazonaws.com/images/02282ff2-7a79-4e60-92c4-38fd148c711a.png",
                                                                        "username": "Châu Lê",
                                                                        "email": "châu.lê8598@example.com",
                                                                        "mutualFriends": 1,
                                                                        "status": "FRIEND",
                                                                        "closeRelationship": null
                                                                    },
                                                                    {
                                                                        "id": 50,
                                                                        "avatar": null,
                                                                        "username": "Chi Võ",
                                                                        "email": "chi.võ8129@example.com",
                                                                        "mutualFriends": 1,
                                                                        "status": "FRIEND",
                                                                        "closeRelationship": null
                                                                    },
                                                                    {
                                                                        "id": 48,
                                                                        "avatar": null,
                                                                        "username": "Dũng Phạm",
                                                                        "email": "dũng.phạm7183@example.com",
                                                                        "mutualFriends": 0,
                                                                        "status": "FRIEND",
                                                                        "closeRelationship": null
                                                                    },
                                                                    {
                                                                        "id": 40,
                                                                        "avatar": "https://ghtk-socialnetwork.s3.ap-southeast-2.amazonaws.com/images/02282ff2-7a79-4e60-92c4-38fd148c711a.png",
                                                                        "username": "Hiếu Ngô",
                                                                        "email": "hiếu.ngô8814@example.com",
                                                                        "mutualFriends": 1,
                                                                        "status": "FRIEND",
                                                                        "closeRelationship": "BROTHER"
                                                                    },
                                                                    {
                                                                        "id": 39,
                                                                        "avatar": null,
                                                                        "username": "Bảo Hoàng",
                                                                        "email": "bảo.hoàng1745@example.com",
                                                                        "mutualFriends": 0,
                                                                        "status": "FRIEND",
                                                                        "closeRelationship": "FATHER"
                                                                    },
                                                                    {
                                                                        "id": 38,
                                                                        "avatar": null,
                                                                        "username": "Hân Đặng",
                                                                        "email": "hân.đặng429@example.com",
                                                                        "mutualFriends": 3,
                                                                        "status": "FRIEND",
                                                                        "closeRelationship": "BROTHER"
                                                                    },
                                                                    {
                                                                        "id": 32,
                                                                        "avatar": "https://ghtk-socialnetwork.s3.ap-southeast-2.amazonaws.com/images/02282ff2-7a79-4e60-92c4-38fd148c711a.png",
                                                                        "username": "Hoài Đỗ",
                                                                        "email": "hoài.đỗ8019@example.com",
                                                                        "mutualFriends": 1,
                                                                        "status": "FRIEND",
                                                                        "closeRelationship": null
                                                                    },
                                                                    {
                                                                        "id": 31,
                                                                        "avatar": null,
                                                                        "username": "Hoài Phan",
                                                                        "email": "hoài.phan2795@example.com",
                                                                        "mutualFriends": 2,
                                                                        "status": "FRIEND",
                                                                        "closeRelationship": "BROTHER"
                                                                    },
                                                                    {
                                                                        "id": 2,
                                                                        "avatar": null,
                                                                        "username": "user2",
                                                                        "email": "user2@gmail.com",
                                                                        "mutualFriends": 7,
                                                                        "status": "FRIEND",
                                                                        "closeRelationship": null
                                                                    },
                                                                    {
                                                                        "id": 26,
                                                                        "avatar": "https://ghtk-socialnetwork.s3.ap-southeast-2.amazonaws.com/images/02282ff2-7a79-4e60-92c4-38fd148c711a.png",
                                                                        "username": "Hiếu Đặng",
                                                                        "email": "hiếu.đặng6003@example.com",
                                                                        "mutualFriends": 4,
                                                                        "status": "FRIEND",
                                                                        "closeRelationship": null
                                                                    },
                                                                    {
                                                                        "id": 25,
                                                                        "avatar": "https://ghtk-socialnetwork.s3.ap-southeast-2.amazonaws.com/images/02282ff2-7a79-4e60-92c4-38fd148c711a.png",
                                                                        "username": "Công Vũ",
                                                                        "email": "công.vũ3412@example.com",
                                                                        "mutualFriends": 2,
                                                                        "status": "FRIEND",
                                                                        "closeRelationship": null
                                                                    },
                                                                    {
                                                                        "id": 21,
                                                                        "avatar": "https://ghtk-socialnetwork.s3.ap-southeast-2.amazonaws.com/images/02282ff2-7a79-4e60-92c4-38fd148c711a.png",
                                                                        "username": "Ánh Phạm",
                                                                        "email": "ánh.phạm9991@example.com",
                                                                        "mutualFriends": 3,
                                                                        "status": "FRIEND",
                                                                        "closeRelationship": "FATHER"
                                                                    },
                                                                    {
                                                                        "id": 20,
                                                                        "avatar": null,
                                                                        "username": "Chi Phan",
                                                                        "email": "chi.phan2308@example.com",
                                                                        "mutualFriends": 7,
                                                                        "status": "FRIEND",
                                                                        "closeRelationship": "FATHER"
                                                                    }
                                                                ]
                                                            },
                                                            "timestamp": "2024-08-25T23:36:05.368017400Z"
                                                        }
                                                    """
                                    )
                            )
                    ),
                    @ApiResponse(description = "The access token provided is expired, revoked, malformed, or invalid for other reasons.",
                            responseCode = "401", content = @Content()),
                    @ApiResponse(description = "", responseCode = "404", content = @Content()),
                    @ApiResponse(description = "", responseCode = "400", content = @Content())
            }
    )
    @GetMapping("/get_list_friends")
    public ResponseEntity<?> getListFriend(@RequestParam(value = "page", defaultValue = "1") int page,
                                           @RequestParam(value = "page_size", defaultValue = "10") int pageSize,
                                           @RequestParam(value = "sort_by", defaultValue = "createdAt") String sortBy,
                                           @RequestParam(value = "sort_direction", defaultValue = "desc") String sortDirection){
        return buildResponse("Get list friends successfully", relationshipService.getListFriend(page, pageSize, sortDirection, sortBy));
    }

    @Operation(
            summary = "Get list user blocked",
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ResultResponse.class),
                                    examples = @ExampleObject(
                                            value = """
                                                        {
                                                            "status": 200,
                                                            "message": "Get list users blocked successfully",
                                                            "result": {
                                                                "pageMeta": {
                                                                    "page": 1,
                                                                    "pageSize": 5,
                                                                    "totalElements": 24,
                                                                    "totalPages": 5,
                                                                    "hasPrev": false,
                                                                    "hasNext": true
                                                                },
                                                                "data": [
                                                                    {
                                                                        "id": 29,
                                                                        "avatar": null,
                                                                        "username": "Ánh Trần",
                                                                        "email": "ánh.trần4221@example.com",
                                                                        "mutualFriends": 3,
                                                                        "status": "BLOCK",
                                                                        "closeRelationship": null
                                                                    },
                                                                    {
                                                                        "id": 49,
                                                                        "avatar": null,
                                                                        "username": "Hiếu Đỗ",
                                                                        "email": "hiếu.đỗ3498@example.com",
                                                                        "mutualFriends": 1,
                                                                        "status": "BLOCK",
                                                                        "closeRelationship": null
                                                                    },
                                                                    {
                                                                        "id": 47,
                                                                        "avatar": null,
                                                                        "username": "Hà Lý",
                                                                        "email": "hà.lý1491@example.com",
                                                                        "mutualFriends": 1,
                                                                        "status": "BLOCK",
                                                                        "closeRelationship": null
                                                                    },
                                                                    {
                                                                        "id": 46,
                                                                        "avatar": null,
                                                                        "username": "Hoài Nguyễn",
                                                                        "email": "hoài.nguyễn3832@example.com",
                                                                        "mutualFriends": 3,
                                                                        "status": "BLOCK",
                                                                        "closeRelationship": null
                                                                    },
                                                                    {
                                                                        "id": 44,
                                                                        "avatar": null,
                                                                        "username": "Hằng Huỳnh",
                                                                        "email": "hằng.huỳnh7001@example.com",
                                                                        "mutualFriends": 0,
                                                                        "status": "BLOCK",
                                                                        "closeRelationship": null
                                                                    }
                                                                ]
                                                            },
                                                            "timestamp": "2024-08-25T23:36:09.805368500Z"
                                                        }
                                                    """
                                    )
                            )
                    ),
                    @ApiResponse(description = "The access token provided is expired, revoked, malformed, or invalid for other reasons.",
                            responseCode = "401", content = @Content()),
                    @ApiResponse(description = "", responseCode = "404", content = @Content()),
                    @ApiResponse(description = "", responseCode = "400", content = @Content())
            }
    )
    @GetMapping("/get_list_block")
    public ResponseEntity<?> getListBlock(@RequestParam(value = "page", defaultValue = "1") int page,
                                          @RequestParam(value = "page_size", defaultValue = "10") int pageSize,
                                          @RequestParam(value = "sort_direction", defaultValue = "desc") String sortDirection,
                                          @RequestParam(value = "sort_by", defaultValue = "createdAt") String sortBy){
        return buildResponse("Get list users blocked successfully", relationshipService.getListBlock(page, pageSize, sortDirection, sortBy));
    }

    @Operation(
            summary = "Remove friend",
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ResultResponse.class),
                                    examples = @ExampleObject(
                                            value = """
                                                        {
                                                            "status": 200,
                                                            "message": "Delete friend successfully",
                                                            "result": {},
                                                            "timestamp": "2024-08-13T00:41:22.073430290Z"
                                                        }
                                                    """
                                    )
                            )
                    ),
                    @ApiResponse(description = "The access token provided is expired, revoked, malformed, or invalid for other reasons.",
                            responseCode = "401", content = @Content()),
                    @ApiResponse(description = "", responseCode = "404", content = @Content()),
                    @ApiResponse(description = "", responseCode = "400", content = @Content())
            }
    )
    @DeleteMapping("/delete_friend")
    public ResponseEntity<?> removeFriend(@RequestParam(value = "user_id") long userId){
        relationshipService.deleteRelationship(userId);
        return buildResponse("Delete friend successfully");
    }

    @Operation(
            summary = "Block a user",
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ResultResponse.class),
                                    examples = @ExampleObject(
                                            value = """
                                                        {
                                                            "status": 200,
                                                            "message": "Block user successfully",
                                                            "result": {},
                                                            "timestamp": "2024-08-13T00:41:22.073430290Z"
                                                        }
                                                    """
                                    )
                            )
                    ),
                    @ApiResponse(description = "The access token provided is expired, revoked, malformed, or invalid for other reasons.",
                            responseCode = "401", content = @Content()),
                    @ApiResponse(description = "", responseCode = "404", content = @Content()),
                    @ApiResponse(description = "", responseCode = "400", content = @Content())
            }
    )
    @PostMapping("/block")
    public ResponseEntity<?> block(@RequestParam(value = "user_id") long userId){
        relationshipService.block(userId);
        return buildResponse("Block user successfully");
    }

    @Operation(
            summary = "Unblock a user",
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ResultResponse.class),
                                    examples = @ExampleObject(
                                            value = """
                                                        {
                                                            "status": 200,
                                                            "message": "UnBlock user successfully",
                                                            "result": {},
                                                            "timestamp": "2024-08-13T00:41:22.073430290Z"
                                                        }
                                                    """
                                    )
                            )
                    ),
                    @ApiResponse(description = "The access token provided is expired, revoked, malformed, or invalid for other reasons.",
                            responseCode = "401", content = @Content()),
                    @ApiResponse(description = "", responseCode = "404", content = @Content()),
                    @ApiResponse(description = "", responseCode = "400", content = @Content())
            }
    )
    @PostMapping("/unblock")
    public ResponseEntity<?> unblock(@RequestParam(value = "user_id") long userId){
        relationshipService.unblock(userId);
        return buildResponse("UnBlock user successfully");
    }

    @Operation(
            summary = "Get friend suggestions",
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ResultResponse.class),
                                    examples = @ExampleObject(
                                            value = """
                                                        {
                                                            "status": 200,
                                                            "message": "Get friend suggestions successfully",
                                                            "result": {
                                                                "pageMeta": {
                                                                    "page": 1,
                                                                    "pageSize": 10,
                                                                    "totalElements": 11,
                                                                    "totalPages": 2,
                                                                    "hasPrev": false,
                                                                    "hasNext": true
                                                                },
                                                                "data": [
                                                                    {
                                                                        "id": 57,
                                                                        "avatar": null,
                                                                        "username": "Viet Ngo",
                                                                        "email": "vietdiez@gmail.com",
                                                                        "mutualFriends": 0,
                                                                        "status": null,
                                                                        "closeRelationship": null
                                                                    },
                                                                    {
                                                                        "id": 4,
                                                                        "avatar": null,
                                                                        "username": "Hân Ngô",
                                                                        "email": "hân.ngô1494@example.com",
                                                                        "mutualFriends": 6,
                                                                        "status": null,
                                                                        "closeRelationship": null
                                                                    },
                                                                    {
                                                                        "id": 6,
                                                                        "avatar": null,
                                                                        "username": "Dũng Vũ",
                                                                        "email": "dũng.vũ2792@example.com",
                                                                        "mutualFriends": 6,
                                                                        "status": null,
                                                                        "closeRelationship": null
                                                                    },
                                                                    {
                                                                        "id": 16,
                                                                        "avatar": null,
                                                                        "username": "Hiếu Võ",
                                                                        "email": "hiếu.võ3135@example.com",
                                                                        "mutualFriends": 4,
                                                                        "status": "REQUESTING",
                                                                        "closeRelationship": null
                                                                    },
                                                                    {
                                                                        "id": 17,
                                                                        "avatar": null,
                                                                        "username": "Dũng Dương",
                                                                        "email": "dũng.dương2618@example.com",
                                                                        "mutualFriends": 3,
                                                                        "status": null,
                                                                        "closeRelationship": null
                                                                    },
                                                                    {
                                                                        "id": 10,
                                                                        "avatar": null,
                                                                        "username": "Duyên Dương",
                                                                        "email": "duyên.dương95@example.com",
                                                                        "mutualFriends": 2,
                                                                        "status": "REQUESTING",
                                                                        "closeRelationship": null
                                                                    },
                                                                    {
                                                                        "id": 43,
                                                                        "avatar": null,
                                                                        "username": "Chi Lê",
                                                                        "email": "chi.lê1247@example.com",
                                                                        "mutualFriends": 2,
                                                                        "status": null,
                                                                        "closeRelationship": null
                                                                    },
                                                                    {
                                                                        "id": 45,
                                                                        "avatar": null,
                                                                        "username": "Chi Huỳnh",
                                                                        "email": "chi.huỳnh9723@example.com",
                                                                        "mutualFriends": 2,
                                                                        "status": "REQUESTING",
                                                                        "closeRelationship": null
                                                                    },
                                                                    {
                                                                        "id": 37,
                                                                        "avatar": "https://ghtk-socialnetwork.s3.ap-southeast-2.amazonaws.com/images/02282ff2-7a79-4e60-92c4-38fd148c711a.png",
                                                                        "username": "Châu Vũ",
                                                                        "email": "châu.vũ4971@example.com",
                                                                        "mutualFriends": 1,
                                                                        "status": null,
                                                                        "closeRelationship": null
                                                                    },
                                                                    {
                                                                        "id": 51,
                                                                        "avatar": null,
                                                                        "username": "Anh Lý",
                                                                        "email": "anh.lý6794@example.com",
                                                                        "mutualFriends": 0,
                                                                        "status": null,
                                                                        "closeRelationship": null
                                                                    }
                                                                ]
                                                            },
                                                            "timestamp": "2024-08-25T23:36:13.111724100Z"
                                                        }
                                                    """
                                    )
                            )
                    ),
                    @ApiResponse(description = "The access token provided is expired, revoked, malformed, or invalid for other reasons.",
                            responseCode = "401", content = @Content()),
                    @ApiResponse(description = "", responseCode = "404", content = @Content()),
                    @ApiResponse(description = "", responseCode = "400", content = @Content())
            }
    )
    @GetMapping("/view_suggest")
    public ResponseEntity<?> viewSuggest(@RequestParam(value = "page", defaultValue = "1") int page,
                                         @RequestParam(value = "page_size", defaultValue = "10") int pageSize
    ){
        return buildResponse("Get friend suggestions successfully", relationshipService.getFriendSuggestions(page, pageSize));
    }

    @Operation(
            summary = "Find friend by key word",
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ResultResponse.class),
                                    examples = @ExampleObject(
                                            value = """
                                                        {
                                                            "status": 200,
                                                            "message": "Find friend successfully",
                                                            "result": {
                                                                "pageMeta": {
                                                                    "page": 1,
                                                                    "pageSize": 8,
                                                                    "totalElements": 2,
                                                                    "totalPages": 1,
                                                                    "hasPrev": false,
                                                                    "hasNext": false
                                                                },
                                                                "data": [
                                                                    {
                                                                        "id": 40,
                                                                        "avatar": "https://ghtk-socialnetwork.s3.ap-southeast-2.amazonaws.com/images/02282ff2-7a79-4e60-92c4-38fd148c711a.png",
                                                                        "username": "Hiếu Ngô",
                                                                        "email": "hiếu.ngô8814@example.com",
                                                                        "mutualFriends": 1,
                                                                        "status": "FRIEND",
                                                                        "closeRelationship": "BROTHER"
                                                                    },
                                                                    {
                                                                        "id": 53,
                                                                        "avatar": "https://ghtk-socialnetwork.s3.ap-southeast-2.amazonaws.com/images/02282ff2-7a79-4e60-92c4-38fd148c711a.png",
                                                                        "username": "Duyên Ngô",
                                                                        "email": "duyên.ngô6794@example.com",
                                                                        "mutualFriends": 0,
                                                                        "status": "FRIEND",
                                                                        "closeRelationship": "FATHER"
                                                                    }
                                                                ]
                                                            },
                                                            "timestamp": "2024-08-25T23:38:35.227414300Z"
                                                        }
                                                    """
                                    )
                            )
                    ),
                    @ApiResponse(description = "The access token provided is expired, revoked, malformed, or invalid for other reasons.",
                            responseCode = "401", content = @Content()),
                    @ApiResponse(description = "", responseCode = "404", content = @Content()),
                    @ApiResponse(description = "", responseCode = "400", content = @Content())
            }
    )
    @GetMapping("/find_friend")
    public ResponseEntity<?> findFriend(@RequestParam(value = "page", defaultValue = "1") int page,
                                        @RequestParam(value = "page_size", defaultValue = "10") int pageSize,
                                        @RequestParam("keyword") String keyWord){
        return buildResponse("Find friend successfully", relationshipService.findFriend(page, pageSize, keyWord));
    }

    @Operation(
            summary = "Get numbers of friends of user",
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ResultResponse.class),
                                    examples = @ExampleObject(
                                            value = """
                                                        {
                                                            "status": 200,
                                                            "message": "Get number of friends successfully",
                                                            "result": {
                                                                "data": 19
                                                            },
                                                            "timestamp": "2024-08-25T23:38:30.804197400Z"
                                                        }
                                                    """
                                    )
                            )
                    ),
                    @ApiResponse(description = "The access token provided is expired, revoked, malformed, or invalid for other reasons.",
                            responseCode = "401", content = @Content()),
                    @ApiResponse(description = "", responseCode = "404", content = @Content()),
                    @ApiResponse(description = "", responseCode = "400", content = @Content())
            }
    )
    @GetMapping("/number_of_friends")
    public ResponseEntity<?> getNumberOfFriends(){
        return buildResponse("Get number of friends successfully", relationshipService.getNumberOfFriend());
    }
}
