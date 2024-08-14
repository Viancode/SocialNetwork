package com.example.socialnetwork.application.controller;

import com.example.socialnetwork.application.request.CloseRelationshipRequest;
import com.example.socialnetwork.application.request.CommentReactionRequest;
import com.example.socialnetwork.application.response.CloseRelationshipResponse;
import com.example.socialnetwork.application.response.CommentReactionResponse;
import com.example.socialnetwork.application.response.ResultResponse;
import com.example.socialnetwork.common.mapper.CloseRelationshipMapper;
import com.example.socialnetwork.common.mapper.CommentReactionMapper;
import com.example.socialnetwork.domain.model.CloseRelationshipDomain;
import com.example.socialnetwork.domain.model.CommentReactionDomain;
import com.example.socialnetwork.domain.model.UserDomain;
import com.example.socialnetwork.domain.port.api.CloseRelationshipServicePort;
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
@RequestMapping("/api/v1/close_relationship")
@SecurityRequirement(name = "bearerAuth")
@RequiredArgsConstructor
@Tag(name = "Close relationship")
public class CloseRelationshipController extends BaseController {
    private final CloseRelationshipServicePort closeRelationshipServicePort;
    private final UserServicePort userServicePort;

    @Operation(
            summary = "Create close relationship with other user",
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
    public ResponseEntity<?> createCloseRelationship(
            @RequestBody CloseRelationshipRequest closeRelationshipRequest
    ) {
        UserDomain userDomain = userServicePort.findUserById(closeRelationshipRequest.getTargetUserId());
        CloseRelationshipDomain closeRelationshipDomain = closeRelationshipServicePort.createCloseRelationship(CloseRelationshipMapper.INSTANCE.requestToDomain(closeRelationshipRequest, userDomain));
        return buildResponse("Create close relationship successfully", CloseRelationshipMapper.INSTANCE.domainToResponse(closeRelationshipDomain));
    }

    @Operation(
            summary = "Get all close relationship of user",
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
    public ResponseEntity<ResultResponse> getCloseRelationships(@RequestParam(defaultValue = "1") int page,
                                                              @RequestParam(value = "page_size", defaultValue = "5") int pageSize,
                                                              @RequestParam(value = "sort_by",defaultValue = "createdAt") String sortBy,
                                                              @RequestParam(value = "sort_direction", defaultValue = "desc") String sortDirection,
                                                              @RequestParam(value = "user_id") Long userId
    ) {
        Page<CloseRelationshipDomain> closeRelationshipDomains = closeRelationshipServicePort.getAllCloseRelationship(page, pageSize, sortBy, sortDirection, userId);

        Page<CloseRelationshipResponse> closeRelationshipResponses = closeRelationshipDomains.map(CloseRelationshipMapper.INSTANCE::domainToResponse);

        return buildResponse("Get close relationship successfully", closeRelationshipResponses);
    }

    @Operation(
            summary = "Delete close relationship with an user",
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
    public ResponseEntity<?> deleteCloseRelationship(
            @RequestParam(value = "target_user_id") Long targetUserId
    ){
        closeRelationshipServicePort.deleteCloseRelationship(targetUserId);
        return buildResponse("Delete close relationship successfully");
    }
}
