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
                                                            "message": "",
                                                            "result": {
                                                              
                                                            },
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
                                                            "message": "",
                                                            "result": {
                                                              
                                                            },
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
                                                            "message": "",
                                                            "result": {
                                                              
                                                            },
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
                                                            "message": "",
                                                            "result": {
                                                              
                                                            },
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
    @PutMapping("")
    public ResponseEntity<?> updateComment(
            @RequestParam(value = "comment_id") Long commentId,
            @RequestParam("content") String content,
            @RequestParam("image") String image
//            @RequestParam("postId") Long postId
//            @RequestParam(value = "parent_comment_id", required = false) Long parentComment,
            ) {

        CommentDomain commentDomain = commentServicePort.updateComment(commentId, content, image);
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
                                                            "message": "",
                                                            "result": {
                                                              
                                                            },
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
    public ResponseEntity<?> deleteComment(@RequestParam(value = "comment_id") Long commentId) {
        commentServicePort.deleteComment(commentId);
        return buildResponse("Delete comment successfully");
    }
}
