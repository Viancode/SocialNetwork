package com.example.socialnetwork.application.controller;

import com.example.socialnetwork.application.request.CommentRequest;
import com.example.socialnetwork.application.response.CommentResponse;
import com.example.socialnetwork.application.response.ResultResponse;
import com.example.socialnetwork.common.mapper.CommentMapper;
import com.example.socialnetwork.common.util.SecurityUtil;
import com.example.socialnetwork.domain.model.CommentDomain;
import com.example.socialnetwork.domain.port.api.CommentServicePort;
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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/v1/comment")
@SecurityRequirement(name = "bearerAuth")
@RequiredArgsConstructor
@Tag(name = "Comment")
public class CommentController extends BaseController {
    private final CommentServicePort commentServicePort;
    private final CommentMapper commentMapper;

    @Operation(
            summary = "Get all comment of a post",
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
                                                            "message": "Get comments successfully",
                                                            "result": {
                                                                "pageMeta": {
                                                                    "page": 1,
                                                                    "pageSize": 5,
                                                                    "totalElements": 8,
                                                                    "totalPages": 2,
                                                                    "hasPrev": false,
                                                                    "hasNext": true
                                                                },
                                                                "data": [
                                                                    {
                                                                        "commentId": 459,
                                                                        "userId": 28,
                                                                        "username": "Hằng Bùi",
                                                                        "avatar": null,
                                                                        "postId": 23,
                                                                        "parentCommentId": null,
                                                                        "numberOfChild": 0,
                                                                        "content": "Comment 18 on post 22",
                                                                        "createdAt": "2024-06-28T00:09:08Z",
                                                                        "updatedAt": "2024-06-27T02:21:08Z",
                                                                        "image": null,
                                                                        "reactCount": 3,
                                                                        "isReacted": false
                                                                    },
                                                                    {
                                                                        "commentId": 454,
                                                                        "userId": 52,
                                                                        "username": "Châu Lê",
                                                                        "avatar": "https://ghtk-socialnetwork.s3.ap-southeast-2.amazonaws.com/images/02282ff2-7a79-4e60-92c4-38fd148c711a.png",
                                                                        "postId": 23,
                                                                        "parentCommentId": null,
                                                                        "numberOfChild": 0,
                                                                        "content": "Comment 13 on post 22",
                                                                        "createdAt": "2024-06-27T19:48:08Z",
                                                                        "updatedAt": "2024-06-27T20:02:08Z",
                                                                        "image": "https://ghtk-socialnetwork.s3.ap-southeast-2.amazonaws.com/images/9b227680-ff92-4bbf-a237-3001cd7f98c1.png",
                                                                        "reactCount": 6,
                                                                        "isReacted": false
                                                                    },
                                                                    {
                                                                        "commentId": 453,
                                                                        "userId": 20,
                                                                        "username": "Chi Phan",
                                                                        "avatar": null,
                                                                        "postId": 23,
                                                                        "parentCommentId": null,
                                                                        "numberOfChild": 1,
                                                                        "content": "Comment 12 on post 22",
                                                                        "createdAt": "2024-06-27T18:17:08Z",
                                                                        "updatedAt": "2024-06-27T07:05:08Z",
                                                                        "image": null,
                                                                        "reactCount": 0,
                                                                        "isReacted": false
                                                                    },
                                                                    {
                                                                        "commentId": 448,
                                                                        "userId": 40,
                                                                        "username": "Hiếu Ngô",
                                                                        "avatar": "https://ghtk-socialnetwork.s3.ap-southeast-2.amazonaws.com/images/02282ff2-7a79-4e60-92c4-38fd148c711a.png",
                                                                        "postId": 23,
                                                                        "parentCommentId": null,
                                                                        "numberOfChild": 0,
                                                                        "content": "Comment 7 on post 22",
                                                                        "createdAt": "2024-06-27T17:33:08Z",
                                                                        "updatedAt": "2024-06-28T00:38:08Z",
                                                                        "image": "https://ghtk-socialnetwork.s3.ap-southeast-2.amazonaws.com/images/9b227680-ff92-4bbf-a237-3001cd7f98c1.png",
                                                                        "reactCount": 4,
                                                                        "isReacted": false
                                                                    },
                                                                    {
                                                                        "commentId": 450,
                                                                        "userId": 38,
                                                                        "username": "Hân Đặng",
                                                                        "avatar": null,
                                                                        "postId": 23,
                                                                        "parentCommentId": null,
                                                                        "numberOfChild": 0,
                                                                        "content": "Comment 9 on post 22",
                                                                        "createdAt": "2024-06-27T16:34:08Z",
                                                                        "updatedAt": "2024-06-27T22:59:08Z",
                                                                        "image": "https://ghtk-socialnetwork.s3.ap-southeast-2.amazonaws.com/images/9b227680-ff92-4bbf-a237-3001cd7f98c1.png",
                                                                        "reactCount": 1,
                                                                        "isReacted": false
                                                                    }
                                                                ]
                                                            },
                                                            "timestamp": "2024-08-25T21:44:21.792790600Z"
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
    public ResponseEntity<ResultResponse> getComments(@RequestParam(value = "post_id") Long postId,
                                                      @RequestParam(defaultValue = "1") int page,
                                                      @RequestParam(value = "page_size", defaultValue = "5") int pageSize,
                                                      @RequestParam(value = "sort_by", defaultValue = "createdAt") String sortBy,
                                                      @RequestParam(value = "sort_direction", defaultValue = "desc") String sortDirection) {
        Page<CommentResponse> comments = commentServicePort.getAllComments(postId, page, pageSize, sortBy, sortDirection);
        return buildResponse("Get comments successfully", comments);
    }

    @Operation(
            summary = "Get all child comment of comment",
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
                                                            "message": "Get comments successfully",
                                                            "result": {
                                                                "pageMeta": {
                                                                    "page": 1,
                                                                    "pageSize": 5,
                                                                    "totalElements": 6,
                                                                    "totalPages": 2,
                                                                    "hasPrev": false,
                                                                    "hasNext": true
                                                                },
                                                                "data": [
                                                                    {
                                                                        "commentId": 15009,
                                                                        "userId": 17,
                                                                        "username": "Dũng Dương",
                                                                        "avatar": null,
                                                                        "postId": 751,
                                                                        "parentCommentId": null,
                                                                        "numberOfChild": 0,
                                                                        "content": "Comment 8 on post 750",
                                                                        "createdAt": "2024-08-25T01:18:51Z",
                                                                        "updatedAt": "2024-08-24T14:20:51Z",
                                                                        "image": "https://ghtk-socialnetwork.s3.ap-southeast-2.amazonaws.com/images/9b227680-ff92-4bbf-a237-3001cd7f98c1.png",
                                                                        "reactCount": 4,
                                                                        "isReacted": false
                                                                    },
                                                                    {
                                                                        "commentId": 15015,
                                                                        "userId": 31,
                                                                        "username": "Hoài Phan",
                                                                        "avatar": null,
                                                                        "postId": 751,
                                                                        "parentCommentId": null,
                                                                        "numberOfChild": 0,
                                                                        "content": "Comment 14 on post 750",
                                                                        "createdAt": "2024-08-25T00:00:51Z",
                                                                        "updatedAt": "2024-08-24T12:00:51Z",
                                                                        "image": "https://ghtk-socialnetwork.s3.ap-southeast-2.amazonaws.com/images/9b227680-ff92-4bbf-a237-3001cd7f98c1.png",
                                                                        "reactCount": 1,
                                                                        "isReacted": false
                                                                    },
                                                                    {
                                                                        "commentId": 15013,
                                                                        "userId": 48,
                                                                        "username": "Dũng Phạm",
                                                                        "avatar": null,
                                                                        "postId": 751,
                                                                        "parentCommentId": null,
                                                                        "numberOfChild": 0,
                                                                        "content": "Comment 12 on post 750",
                                                                        "createdAt": "2024-08-24T23:16:51Z",
                                                                        "updatedAt": "2024-08-24T09:14:51Z",
                                                                        "image": null,
                                                                        "reactCount": 4,
                                                                        "isReacted": false
                                                                    },
                                                                    {
                                                                        "commentId": 15011,
                                                                        "userId": 4,
                                                                        "username": "Hân Ngô",
                                                                        "avatar": null,
                                                                        "postId": 751,
                                                                        "parentCommentId": null,
                                                                        "numberOfChild": 0,
                                                                        "content": "Comment 10 on post 750",
                                                                        "createdAt": "2024-08-24T18:11:51Z",
                                                                        "updatedAt": "2024-08-24T19:56:51Z",
                                                                        "image": "https://ghtk-socialnetwork.s3.ap-southeast-2.amazonaws.com/images/9b227680-ff92-4bbf-a237-3001cd7f98c1.png",
                                                                        "reactCount": 2,
                                                                        "isReacted": false
                                                                    },
                                                                    {
                                                                        "commentId": 15004,
                                                                        "userId": 40,
                                                                        "username": "Hiếu Ngô",
                                                                        "avatar": "https://ghtk-socialnetwork.s3.ap-southeast-2.amazonaws.com/images/02282ff2-7a79-4e60-92c4-38fd148c711a.png",
                                                                        "postId": 751,
                                                                        "parentCommentId": null,
                                                                        "numberOfChild": 0,
                                                                        "content": "Comment 3 on post 750",
                                                                        "createdAt": "2024-08-24T17:07:51Z",
                                                                        "updatedAt": "2024-08-24T14:27:51Z",
                                                                        "image": "https://ghtk-socialnetwork.s3.ap-southeast-2.amazonaws.com/images/9b227680-ff92-4bbf-a237-3001cd7f98c1.png",
                                                                        "reactCount": 3,
                                                                        "isReacted": false
                                                                    }
                                                                ]
                                                            },
                                                            "timestamp": "2024-08-25T21:45:01.276201100Z"
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
    @GetMapping("/{comment_id}")
    public ResponseEntity<ResultResponse> getChildComment(@PathVariable(value = "comment_id") Long commentId,
                                                          @RequestParam(value = "post_id") Long postId,
                                                          @RequestParam(defaultValue = "1") int page,
                                                          @RequestParam(value = "page_size", defaultValue = "5") int pageSize,
                                                          @RequestParam(value = "sort_by", defaultValue = "createdAt") String sortBy,
                                                          @RequestParam(value = "sort_direction", defaultValue = "desc") String sortDirection) {
        Page<CommentResponse> childComments = commentServicePort.getChildComments(postId, commentId, page, pageSize, sortBy, sortDirection);
        return buildResponse("Get comment successfully", childComments);
    }

    @Operation(
            summary = "Create new comment",
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
                                                            "message": "Create comment successfully",
                                                            "result": {
                                                                "data": {
                                                                    "commentId": 24001,
                                                                    "userId": 1,
                                                                    "username": null,
                                                                    "avatar": null,
                                                                    "postId": 201,
                                                                    "parentCommentId": null,
                                                                    "numberOfChild": 0,
                                                                    "content": "hello",
                                                                    "createdAt": "2024-08-25T21:46:12.836585100Z",
                                                                    "updatedAt": "2024-08-25T21:46:12.836585100Z",
                                                                    "image": "",
                                                                    "reactCount": 0,
                                                                    "isReacted": false
                                                                }
                                                            },
                                                            "timestamp": "2024-08-25T21:46:12.856738400Z"
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
    public ResponseEntity<?> createComment(@ModelAttribute CommentRequest commentRequest) {
        CommentDomain newComment = commentServicePort.createComment(commentRequest);
        return buildResponse("Create comment successfully", commentMapper.commentDomainToCommentResponse(newComment));
    }

    @Operation(
            summary = "Update Comment",
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
                                                            "message": "Update comment successfully",
                                                            "result": {
                                                                "data": {
                                                                    "commentId": 24001,
                                                                    "userId": 1,
                                                                    "username": "user1",
                                                                    "avatar": null,
                                                                    "postId": 201,
                                                                    "parentCommentId": null,
                                                                    "numberOfChild": 0,
                                                                    "content": "manh",
                                                                    "createdAt": "2024-08-25T21:46:13Z",
                                                                    "updatedAt": "2024-08-25T21:47:07.029227Z",
                                                                    "image": "",
                                                                    "reactCount": 0,
                                                                    "isReacted": false
                                                                }
                                                            },
                                                            "timestamp": "2024-08-25T21:47:07.070601700Z"
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
    @PutMapping("")
    public ResponseEntity<?> updateComment(
            @RequestParam(value = "comment_id") Long commentId,
            @RequestParam("content") String content,
            @RequestParam(value = "image", required = false) MultipartFile[] image,
            @RequestParam(value = "is_delete", defaultValue = "false") Boolean isDelete
//            @RequestParam("postId") Long postId
//            @RequestParam(value = "parent_comment_id", required = false) Long parentComment,
            ) {

        CommentDomain commentDomain = commentServicePort.updateComment(commentId, content, image, isDelete);
        return buildResponse("Update comment successfully", commentMapper.commentDomainToCommentResponse(commentDomain));
    }

    @Operation(
            summary = "Delete comment from a post",
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
                                                            "message": "Delete comment successfully",
                                                            "result": {},
                                                            "timestamp": "2024-08-25T21:48:31.422203900Z"
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
    public ResponseEntity<?> deleteComment(@RequestParam(value = "comment_id") Long commentId) {
        commentServicePort.deleteComment(commentId);
        return buildResponse("Delete comment successfully");
    }
}
