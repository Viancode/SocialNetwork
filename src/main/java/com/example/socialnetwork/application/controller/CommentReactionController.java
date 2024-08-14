package com.example.socialnetwork.application.controller;

import com.example.socialnetwork.application.request.CommentReactionRequest;
import com.example.socialnetwork.application.request.PostReactionRequest;
import com.example.socialnetwork.application.response.CommentReactionResponse;
import com.example.socialnetwork.application.response.PostReactionResponse;
import com.example.socialnetwork.application.response.ResultResponse;
import com.example.socialnetwork.common.mapper.CommentReactionMapper;
import com.example.socialnetwork.common.mapper.PostReactionMapper;
import com.example.socialnetwork.domain.model.CommentReactionDomain;
import com.example.socialnetwork.domain.model.PostReactionDomain;
import com.example.socialnetwork.domain.model.UserDomain;
import com.example.socialnetwork.domain.port.api.CommentReactionServicePort;
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
@RequestMapping("/api/v1/comment_reaction")
@SecurityRequirement(name = "bearerAuth")
@RequiredArgsConstructor
@Tag(name = "Reaction of comment")
public class CommentReactionController extends BaseController {
    private final CommentReactionServicePort commentReactionServicePort;
    private final UserServicePort userServicePort;

    @Operation(
            summary = "Create new reaction for a comment",
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
    public ResponseEntity<?> createCommentReaction(
            @RequestBody CommentReactionRequest commentReactionRequest
    ) {
        CommentReactionDomain commentReactionDomain = commentReactionServicePort.createCommentReaction(CommentReactionMapper.INSTANCE.requestToDomain(commentReactionRequest));
        UserDomain userDomain = userServicePort.findUserById(commentReactionDomain.getUser().getId());
        return buildResponse("Create comment reaction successfully", CommentReactionMapper.INSTANCE.domainToResponse(commentReactionDomain, userDomain));
    }

    @Operation(
            summary = "Delete reaction from a comment",
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
    public ResponseEntity<?> deleteCommentReaction(
            @RequestParam(value = "comment_reaction_id") Long commentReactionId
    ){
        commentReactionServicePort.deleteCommentReaction(commentReactionId);
        return buildResponse("Delete comment reaction successfully");
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
    public ResponseEntity<ResultResponse> getCommentReactions(@RequestParam(defaultValue = "1") int page,
                                                   @RequestParam(value = "page_size", defaultValue = "5") int pageSize,
                                                   @RequestParam(value = "sort_by", defaultValue = "createdAt") String sortBy,
                                                   @RequestParam(value = "sort_direction", defaultValue = "desc") String sortDirection,
                                                   @RequestParam(value = "comment_id", required = false) Long commentId,
                                                   @RequestParam(value = "comment_reaction_type", required = false) String commentReactionType
    ) {

        Page<CommentReactionDomain> commentReactionDomainPage = commentReactionServicePort.getAllCommentReactions(page, pageSize, sortBy, sortDirection, commentId, commentReactionType);
        Page<CommentReactionResponse> commentReactionResponsePage = commentReactionDomainPage.map(commentReactionDomain -> {
            UserDomain userDomain = userServicePort.findUserById(commentReactionDomain.getUser().getId());
            return CommentReactionMapper.INSTANCE.domainToResponse(commentReactionDomain, userDomain);
        });
        return buildResponse("Get post successfully", commentReactionResponsePage);
    }
}
