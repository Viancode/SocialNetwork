package com.example.socialnetwork.application.controller;

import com.example.socialnetwork.application.request.TagRequest;
import com.example.socialnetwork.application.response.PostReactionResponse;
import com.example.socialnetwork.application.response.ResultResponse;
import com.example.socialnetwork.application.response.TagResponse;
import com.example.socialnetwork.common.mapper.PostReactionMapper;
import com.example.socialnetwork.common.mapper.TagMapper;
import com.example.socialnetwork.domain.model.PostReactionDomain;
import com.example.socialnetwork.domain.model.TagDomain;
import com.example.socialnetwork.domain.model.UserDomain;
import com.example.socialnetwork.domain.port.api.TagServicePort;
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
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/tag")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Tag")
public class TagController extends BaseController{
    private final TagServicePort tagServicePort;
    private final TagMapper tagMapper;

//    @Operation(
//            summary = "Create tag user into post",
//            responses = {
//                    @ApiResponse(
//                            description = "Success",
//                            responseCode = "200",
//                            content = @Content(
//                                    mediaType = "application/json",
//                                    schema = @Schema(implementation = ResultResponse.class),
//                                    examples = @ExampleObject(
//                                            value = """
//                                                        {
//                                                            "status": 200,
//                                                            "message": "",
//                                                            "result": {
//
//                                                            },
//                                                            "timestamp": "2024-08-13T00:41:22.073430290Z"
//                                                        }
//                                                    """
//                                    )
//                            )
//                    ),
//                    @ApiResponse(description = "The access token provided is expired, revoked, malformed, or invalid for other reasons.",
//                            responseCode = "401", content = @Content()),
//                    @ApiResponse(description = "", responseCode = "404", content = @Content()),
//                    @ApiResponse(description = "", responseCode = "400", content = @Content())
//            }
//    )
//    @PostMapping
//    public ResponseEntity<?> createTag(@RequestBody TagRequest tagRequest) {
//        TagDomain tagDomain = tagServicePort.createTag(tagMapper.requestToDomain(tagRequest, null));
//        return buildResponse("Create tag successfully", tagMapper.domainToResponse(tagDomain));
//    }

    @Operation(
            summary = "Delete tag from post",
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
                                                            "message": "Delete tag successfully",
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
    @DeleteMapping
    public ResponseEntity<?> deleteTag(@RequestParam(value = "tag_id") Long tagId) {
        tagServicePort.deleteTag(tagId);
        return buildResponse("Delete tag successfully", HttpStatus.ACCEPTED);
    }

    @Operation(
            summary = "Get all tag of post",
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
                                                            "message": "Get all tags",
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
                                                                        "id": 1,
                                                                        "userId": 3,
                                                                        "username": "user3",
                                                                        "createdAt": "2024-08-26T06:31:10"
                                                                    },
                                                                    {
                                                                        "id": 2,
                                                                        "userId": 47,
                                                                        "username": "Hà Lý",
                                                                        "createdAt": "2024-08-26T06:31:10"
                                                                    },
                                                                    {
                                                                        "id": 3,
                                                                        "userId": 20,
                                                                        "username": "Chi Phan",
                                                                        "createdAt": "2024-08-26T06:31:10"
                                                                    }
                                                                ]
                                                            },
                                                            "timestamp": "2024-08-25T23:31:28.038791600Z"
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
    @GetMapping
    public ResponseEntity<?> getAllTags(@RequestParam(defaultValue = "1") int page,
                                        @RequestParam(value = "page_size", defaultValue = "5") int pageSize,
                                        @RequestParam(value = "sort_by", defaultValue = "createdAt") String sortBy,
                                        @RequestParam(value = "sort_direction", defaultValue = "desc") String sortDirection,
                                        @RequestParam(required = false) Long postId) {
        Page<TagDomain> tagDomainPage = tagServicePort.getAllTags(page,pageSize,sortBy,sortDirection,postId);
        Page<TagResponse> tagResponsePage = tagDomainPage.map(tagMapper::domainToResponse);

        return buildResponse("Get all tags", tagResponsePage);
    }
}
