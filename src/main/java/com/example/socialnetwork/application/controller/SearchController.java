package com.example.socialnetwork.application.controller;

import com.example.socialnetwork.application.response.ResultResponse;
import com.example.socialnetwork.common.mapper.UserMapper;
import com.example.socialnetwork.domain.port.api.RelationshipServicePort;
import com.example.socialnetwork.domain.port.api.UserServicePort;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/search")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Search")
public class SearchController extends BaseController{
    private final RelationshipServicePort relationshipServicePort;
    private final UserMapper userMapper;

    @Operation(
            summary = "Search user in social network",
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
                                                            "message": "Search user successfully",
                                                            "result": {
                                                                "pageMeta": {
                                                                    "page": 1,
                                                                    "pageSize": 3,
                                                                    "totalElements": 1,
                                                                    "totalPages": 1,
                                                                    "hasPrev": false,
                                                                    "hasNext": false
                                                                },
                                                                "data": [
                                                                    {
                                                                        "id": 39,
                                                                        "avatar": null,
                                                                        "username": "Bảo Hoàng",
                                                                        "email": "bảo.hoàng1745@example.com",
                                                                        "mutualFriends": 0,
                                                                        "status": "FRIEND",
                                                                        "closeRelationship": "FATHER"
                                                                    }
                                                                ]
                                                            },
                                                            "timestamp": "2024-08-25T21:56:54.268759400Z"
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
    public ResponseEntity<?> search(@RequestParam(value = "page", defaultValue = "1") int page,
                                    @RequestParam(value = "page_size", defaultValue = "10") int pageSize,
                                    @RequestParam(value = "keyword") String keyWord) {
        return buildResponse("Search user successfully", relationshipServicePort.searchUser(page, pageSize, keyWord));
    }
}
