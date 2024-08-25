package com.example.socialnetwork.application.controller;

import com.example.socialnetwork.application.request.PostReactionRequest;
import com.example.socialnetwork.application.response.PostReactionResponse;
import com.example.socialnetwork.application.response.ResultResponse;
import com.example.socialnetwork.common.mapper.PostReactionMapper;
import com.example.socialnetwork.domain.model.PostReactionDomain;
import com.example.socialnetwork.domain.model.UserDomain;
import com.example.socialnetwork.domain.port.api.PostReactionServicePort;
import com.example.socialnetwork.domain.port.api.UserServicePort;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/post_reaction")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Reaction of post")
public class PostReactionController extends BaseController {
    private final PostReactionServicePort postReactionService;
    private final UserServicePort userServicePort;

    @Operation(
            summary = "Create new reaction for post",
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
                                                            "message": "Create post reaction successfully",
                                                            "result": {
                                                                "data": {
                                                                    "id": 3416,
                                                                    "userId": 1,
                                                                    "username": "user1",
                                                                    "avatar": null,
                                                                    "postId": 177,
                                                                    "reactionType": "LIKE",
                                                                    "createdAt": "2024-08-25T21:55:17.186958500Z"
                                                                }
                                                            },
                                                            "timestamp": "2024-08-25T21:55:17.222126Z"
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
    @PostMapping("")
    public ResponseEntity<?> createPostReaction(
            @RequestBody PostReactionRequest postReactionRequest
    ) {
        PostReactionDomain postReactionDomain = postReactionService.createPostReaction(PostReactionMapper.INSTANCE.requestToDomain(postReactionRequest));
        if(postReactionDomain == null) {
            return buildResponse("unreactive post successfully");
        }
        UserDomain userDomain = userServicePort.findUserById(postReactionDomain.getUserId());
        return buildResponse("Create post reaction successfully", PostReactionMapper.INSTANCE.domainToResponseWithUser(postReactionDomain, userDomain));
    }

    @Operation(
            summary = "Delete reaction from post",
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
                                                            "message": "Delete post reaction successfully",
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
    @DeleteMapping("")
    public ResponseEntity<?> deletePostReaction(
            @RequestParam(value = "post_reaction_id") Long postReactionId
    ){
        postReactionService.deletePostReaction(postReactionId);
        return buildResponse("Delete post reaction successfully");
    }

    @Operation(
            summary = "Get all reaction of post",
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
                                                            "message": "Get post successfully",
                                                            "result": {
                                                                "pageMeta": {
                                                                    "page": 1,
                                                                    "pageSize": 5,
                                                                    "totalElements": 3,
                                                                    "totalPages": 1,
                                                                    "hasPrev": false,
                                                                    "hasNext": false
                                                                },
                                                                "data": [
                                                                    {
                                                                        "id": 3416,
                                                                        "userId": 1,
                                                                        "username": "user1",
                                                                        "avatar": null,
                                                                        "postId": 177,
                                                                        "reactionType": "LIKE",
                                                                        "createdAt": "2024-08-25T21:55:17Z"
                                                                    },
                                                                    {
                                                                        "id": 475,
                                                                        "userId": 7,
                                                                        "username": "Hằng Lý",
                                                                        "avatar": "https://ghtk-socialnetwork.s3.ap-southeast-2.amazonaws.com/images/02282ff2-7a79-4e60-92c4-38fd148c711a.png",
                                                                        "postId": 177,
                                                                        "reactionType": "LIKE",
                                                                        "createdAt": "2023-11-07T18:02:22Z"
                                                                    },
                                                                    {
                                                                        "id": 474,
                                                                        "userId": 37,
                                                                        "username": "Châu Vũ",
                                                                        "avatar": "https://ghtk-socialnetwork.s3.ap-southeast-2.amazonaws.com/images/02282ff2-7a79-4e60-92c4-38fd148c711a.png",
                                                                        "postId": 177,
                                                                        "reactionType": "LIKE",
                                                                        "createdAt": "2023-11-07T14:51:22Z"
                                                                    }
                                                                ]
                                                            },
                                                            "timestamp": "2024-08-25T21:55:52.471185300Z"
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
    @GetMapping("")
    public ResponseEntity<ResultResponse> getPostReactions(@RequestParam(defaultValue = "1") int page,
                                                           @RequestParam(value = "page_size",defaultValue = "5") int pageSize,
                                                           @RequestParam(value = "sort_by", defaultValue = "createdAt") String sortBy,
                                                           @RequestParam(value = "sort_direction", defaultValue = "desc") String sortDirection,
                                                           @RequestParam(value = "post_id", required = false) Long postId,
                                                           @RequestParam(value = "post_reaction_type",required = false) String postReactionType
                                                    ) {

        Page<PostReactionDomain> postReactionDomainPage = postReactionService.getAllPostReactions(page,pageSize,sortBy,sortDirection,postId,postReactionType);
        Page<PostReactionResponse> postReactionResponsePage = postReactionDomainPage.map(postReactionDomain -> {
            UserDomain userDomain = userServicePort.findUserById(postReactionDomain.getUserId());
            return PostReactionMapper.INSTANCE.domainToResponseWithUser(postReactionDomain, userDomain);
        });
        return buildResponse("Get post successfully", postReactionResponsePage);
    }

}
