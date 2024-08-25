package com.example.socialnetwork.application.controller;

import com.example.socialnetwork.application.request.PostRequest;
import com.example.socialnetwork.application.request.PostUpdate;
import com.example.socialnetwork.application.request.TagRequest;
import com.example.socialnetwork.application.response.PostResponse;
import com.example.socialnetwork.application.response.ResultResponse;
import com.example.socialnetwork.common.mapper.PostMapper;
import com.example.socialnetwork.common.util.SecurityUtil;
import com.example.socialnetwork.domain.model.PostDomain;
import com.example.socialnetwork.domain.port.api.PostServicePort;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;


@RestController
@RequestMapping("/api/v1/post")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Post")
public class PostController extends BaseController {
    private final PostServicePort postServicePort;
    private final PostMapper postMapper;

    @Operation(
            summary = "Get all post of an user",
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
                                                                "data": [
                                                                    {
                                                                        "id": 9,
                                                                        "userId": 1,
                                                                        "username": "Minh Viet",
                                                                        "avatar": "https://ghtk-socialnetwork.s3.ap-southeast-2.amazonaws.com/images/7fbcaedf-9ba7-4e32-ae6c-ced3e38cb5e0.png",
                                                                        "content": "Post content 8",
                                                                        "visibility": "PUBLIC",
                                                                        "createdAt": "2024-08-07T00:50:58Z",
                                                                        "updatedAt": "2024-08-07T00:50:58Z",
                                                                        "photoResponses": [
                                                                            {
                                                                                "id": "9b227680-ff92-4bbf-a237-3001cd7f98c1",
                                                                                "url": "https://ghtk-socialnetwork.s3.ap-southeast-2.amazonaws.com/images/9b227680-ff92-4bbf-a237-3001cd7f98c1.png"
                                                                            }
                                                                        ],
                                                                        "numberOfComments": 10,
                                                                        "numberOfReacts": 3,
                                                                        "tagUsers": []
                                                                    },
                                                                    {
                                                                        "id": 20,
                                                                        "userId": 1,
                                                                        "username": "Minh Viet",
                                                                        "avatar": "https://ghtk-socialnetwork.s3.ap-southeast-2.amazonaws.com/images/7fbcaedf-9ba7-4e32-ae6c-ced3e38cb5e0.png",
                                                                        "content": "Post content 19",
                                                                        "visibility": "FRIEND",
                                                                        "createdAt": "2024-08-01T12:11:16Z",
                                                                        "updatedAt": "2024-08-01T12:11:16Z",
                                                                        "photoResponses": [
                                                                            {
                                                                                "id": "9b227680-ff92-4bbf-a237-3001cd7f98c1",
                                                                                "url": "https://ghtk-socialnetwork.s3.ap-southeast-2.amazonaws.com/images/9b227680-ff92-4bbf-a237-3001cd7f98c1.png"
                                                                            }
                                                                        ],
                                                                        "numberOfComments": 10,
                                                                        "numberOfReacts": 3,
                                                                        "tagUsers": []
                                                                    }
                                                                ],
                                                                "pageMeta": {
                                                                    "page": 1,
                                                                    "pageSize": 2,
                                                                    "totalElements": 20,
                                                                    "totalPages": 10,
                                                                    "hasPrev": false,
                                                                    "hasNext": true
                                                                }
                                                            },
                                                            "timestamp": "2024-08-14T19:22:05.563894846Z"
                                                        }
                                                    """
                                    )
                            )
                    ),
                    @ApiResponse(description = "The access token provided is expired, revoked, malformed, or invalid for other reasons.",
                            responseCode = "401", content = @Content()),
                    @ApiResponse(description = "User not found", responseCode = "404", content = @Content()),
                    @ApiResponse(description = "You don't have permission to view this user's posts or user doesn't have any posts", responseCode = "400", content = @Content())
            }
    )
    @GetMapping("")
    public ResponseEntity<ResultResponse> getPosts(@RequestParam(defaultValue = "1") int page,
                                                   @RequestParam(value = "page_size",defaultValue = "5") int pageSize,
                                                   @RequestParam(value = "sort_by", defaultValue = "createdAt") String sortBy,
                                                   @RequestParam(value = "sort_direction", defaultValue = "desc") String sortDirection,
                                                   @RequestParam(value = "target_user_id") Long targetUserId,
                                                   Authentication authentication) {

        User user = (User) authentication.getPrincipal();
        Page<PostResponse> posts = postServicePort.getAllPosts(page, pageSize, sortBy, sortDirection, Long.valueOf(user.getUsername()), targetUserId);
        return buildResponse("Get post successfully", posts);
    }


    @Operation(
            summary = "Create a post",
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
                                                             "message": "Create post successfully",
                                                             "result": {
                                                                 "data": {
                                                                     "id": 63,
                                                                     "userId": 1,
                                                                     "username": "Minh Viet",
                                                                     "avatar": "https://ghtk-socialnetwork.s3.ap-southeast-2.amazonaws.com/images/7fbcaedf-9ba7-4e32-ae6c-ced3e38cb5e0.png",
                                                                     "content": "Hello",
                                                                     "visibility": "PUBLIC",
                                                                     "createdAt": "2024-08-14T19:26:04.052613165Z",
                                                                     "updatedAt": "2024-08-14T19:26:04.052613783Z",
                                                                     "photoResponses": [
                                                                         {
                                                                             "id": "6a283bd1-46ff-4f87-abda-6f121687ffcf",
                                                                             "url": "https://ghtk-socialnetwork.s3.ap-southeast-2.amazonaws.com/images/6a283bd1-46ff-4f87-abda-6f121687ffcf.png"
                                                                         },
                                                                         {
                                                                             "id": "02282ff2-7a79-4e60-92c4-38fd148c711a",
                                                                             "url": "https://ghtk-socialnetwork.s3.ap-southeast-2.amazonaws.com/images/02282ff2-7a79-4e60-92c4-38fd148c711a.png"
                                                                         }
                                                                     ],
                                                                     "numberOfComments": 0,
                                                                     "numberOfReacts": 0,
                                                                     "tagUsers": []
                                                                 }
                                                             },
                                                             "timestamp": "2024-08-14T19:26:04.078384091Z"
                                                         }
                                                    """
                                    )
                            )
                    ),
                    @ApiResponse(description = "The access token provided is expired, revoked, malformed, or invalid for other reasons.",
                            responseCode = "401", content = @Content()),
                    @ApiResponse(description = "User not found", responseCode = "404", content = @Content()),
                    @ApiResponse(description = "User with id 1 is not friend.", responseCode = "400", content = @Content())
            }
    )
    @PostMapping("")
    public ResponseEntity<?> createPost(
            @ModelAttribute PostRequest postRequest){
        PostDomain postDomain = postServicePort.createPost(postMapper.requestToDomain(postRequest));
        return buildResponse("Create post successfully", postMapper.domainToResponse(postDomain));
    }

    @Operation(
            summary = "Delete a post",
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
                                                            "status": 202,
                                                            "message": "Delete post successfully",
                                                            "result": {},
                                                            "timestamp": "2024-08-14T19:28:41.178009966Z"
                                                        }
                                                    """
                                    )
                            )
                    ),
                    @ApiResponse(description = "The access token provided is expired, revoked, malformed, or invalid for other reasons.",
                            responseCode = "401", content = @Content()),
                    @ApiResponse(description = "Post not found", responseCode = "404", content = @Content()),
            }
    )
    @DeleteMapping("")
    public ResponseEntity<?> deletePost(@RequestParam(value = "post_id") Long postId){
        postServicePort.deletePost(postId);
        return buildResponse("Delete post successfully", HttpStatus.ACCEPTED);
    }

    @Operation(
            summary = "Update a post",
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
                                                             "message": "Update post successfully",
                                                             "result": {
                                                                 "data": {
                                                                     "id": 2,
                                                                     "userId": 1,
                                                                     "username": "Minh Viet",
                                                                     "avatar": "https://ghtk-socialnetwork.s3.ap-southeast-2.amazonaws.com/images/7fbcaedf-9ba7-4e32-ae6c-ced3e38cb5e0.png",
                                                                     "content": "Hello",
                                                                     "visibility": "PUBLIC",
                                                                     "createdAt": "2024-06-21T03:40:48Z",
                                                                     "updatedAt": "2024-08-14T19:33:22.357765965Z",
                                                                     "photoResponses": [
                                                                         {
                                                                             "id": "6a283bd1-46ff-4f87-abda-6f121687ffcf",
                                                                             "url": "https://ghtk-socialnetwork.s3.ap-southeast-2.amazonaws.com/images/6a283bd1-46ff-4f87-abda-6f121687ffcf.png"
                                                                         },
                                                                         {
                                                                             "id": "02282ff2-7a79-4e60-92c4-38fd148c711a",
                                                                             "url": "https://ghtk-socialnetwork.s3.ap-southeast-2.amazonaws.com/images/02282ff2-7a79-4e60-92c4-38fd148c711a.png"
                                                                         }
                                                                     ],
                                                                     "numberOfComments": 10,
                                                                     "numberOfReacts": 3,
                                                                     "tagUsers": [
                                                                         {
                                                                             "id": 2,
                                                                             "username": "user2"
                                                                         }
                                                                     ]
                                                                 }
                                                             },
                                                             "timestamp": "2024-08-14T19:33:22.601632499Z"
                                                         }
                                                    """
                                    )
                            )
                    ),
                    @ApiResponse(description = "The access token provided is expired, revoked, malformed, or invalid for other reasons.",
                            responseCode = "401", content = @Content()),
                    @ApiResponse(description = "Post not found", responseCode = "404", content = @Content()),
            }
    )
    @PutMapping("")
    public ResponseEntity<?> updatePost(
            @ModelAttribute PostUpdate postUpdate
    ){
        PostDomain postDomain = postServicePort.updatePost(postUpdate);
        return buildResponse("Update post successfully", postMapper.domainToResponse(postDomain));
    }

    @Operation(
            summary = "Count number of posts of current user",
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
                                                            "message": "Get number post successfully",
                                                            "result": {
                                                                "data": 20
                                                            },
                                                            "timestamp": "2024-08-14T19:34:20.890521130Z"
                                                        }
                                                    """
                                    )
                            )
                    ),
                    @ApiResponse(description = "The access token provided is expired, revoked, malformed, or invalid for other reasons.",
                            responseCode = "401", content = @Content()),
            }
    )
    @GetMapping("/number_post")
    public ResponseEntity<?> getNumberPost(){
        Long numberPost = postServicePort.countPostByUserId();
        return buildResponse("Get number post successfully", numberPost);
    }
}
