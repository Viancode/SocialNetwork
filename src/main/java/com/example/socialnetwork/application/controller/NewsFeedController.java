package com.example.socialnetwork.application.controller;

import com.example.socialnetwork.application.response.ResultResponse;
import com.example.socialnetwork.common.mapper.PostMapper;
import com.example.socialnetwork.domain.port.api.PostServicePort;
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
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/newsfeed")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Newsfeed")
public class NewsFeedController extends BaseController{
    private final PostServicePort postServicePort;
    private final PostMapper postMapper;

    @Operation(
            summary = "Get newsfeed of current user",
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
                                                            "message": "Get News Feed successfully",
                                                            "result": {
                                                                "pageMeta": {
                                                                    "page": 1,
                                                                    "pageSize": 5,
                                                                    "totalElements": 100,
                                                                    "totalPages": 20,
                                                                    "hasPrev": false,
                                                                    "hasNext": true
                                                                },
                                                                "data": [
                                                                    {
                                                                        "id": 201,
                                                                        "userId": 21,
                                                                        "username": "Ánh Phạm",
                                                                        "avatar": "https://ghtk-socialnetwork.s3.ap-southeast-2.amazonaws.com/images/02282ff2-7a79-4e60-92c4-38fd148c711a.png",
                                                                        "content": "Post content 200",
                                                                        "visibility": "PUBLIC",
                                                                        "createdAt": "2024-03-06T14:53:59Z",
                                                                        "updatedAt": "2024-03-06T14:53:59Z",
                                                                        "photoResponses": null,
                                                                        "numberOfComments": 20,
                                                                        "numberOfReacts": 4,
                                                                        "tagUsers": [],
                                                                        "isReacted": false
                                                                    },
                                                                    {
                                                                        "id": 906,
                                                                        "userId": 2,
                                                                        "username": "user2",
                                                                        "avatar": null,
                                                                        "content": "Post content 905",
                                                                        "visibility": "PUBLIC",
                                                                        "createdAt": "2024-08-24T20:56:39Z",
                                                                        "updatedAt": "2024-08-24T20:56:39Z",
                                                                        "photoResponses": null,
                                                                        "numberOfComments": 20,
                                                                        "numberOfReacts": 5,
                                                                        "tagUsers": [],
                                                                        "isReacted": false
                                                                    },
                                                                    {
                                                                        "id": 729,
                                                                        "userId": 50,
                                                                        "username": "Chi Võ",
                                                                        "avatar": null,
                                                                        "content": "Post content 728",
                                                                        "visibility": "FRIEND",
                                                                        "createdAt": "2024-08-24T14:20:26Z",
                                                                        "updatedAt": "2024-08-24T14:20:26Z",
                                                                        "photoResponses": null,
                                                                        "numberOfComments": 20,
                                                                        "numberOfReacts": 3,
                                                                        "tagUsers": [],
                                                                        "isReacted": false
                                                                    },
                                                                    {
                                                                        "id": 142,
                                                                        "userId": 15,
                                                                        "username": "Hải Võ",
                                                                        "avatar": null,
                                                                        "content": "Post content 141",
                                                                        "visibility": "FRIEND",
                                                                        "createdAt": "2024-08-24T14:19:54Z",
                                                                        "updatedAt": "2024-08-24T14:19:54Z",
                                                                        "photoResponses": null,
                                                                        "numberOfComments": 20,
                                                                        "numberOfReacts": 5,
                                                                        "tagUsers": [],
                                                                        "isReacted": false
                                                                    },
                                                                    {
                                                                        "id": 751,
                                                                        "userId": 26,
                                                                        "username": "Hiếu Đặng",
                                                                        "avatar": "https://ghtk-socialnetwork.s3.ap-southeast-2.amazonaws.com/images/02282ff2-7a79-4e60-92c4-38fd148c711a.png",
                                                                        "content": "Post content 750",
                                                                        "visibility": "FRIEND",
                                                                        "createdAt": "2024-08-24T04:33:51Z",
                                                                        "updatedAt": "2024-08-24T04:33:51Z",
                                                                        "photoResponses": [
                                                                            {
                                                                                "id": "9b227680-ff92-4bbf-a237-3001cd7f98c1",
                                                                                "url": "https://ghtk-socialnetwork.s3.ap-southeast-2.amazonaws.com/images/9b227680-ff92-4bbf-a237-3001cd7f98c1.png"
                                                                            }
                                                                        ],
                                                                        "numberOfComments": 20,
                                                                        "numberOfReacts": 0,
                                                                        "tagUsers": [],
                                                                        "isReacted": false
                                                                    }
                                                                ]
                                                            },
                                                            "timestamp": "2024-08-25T21:52:58.638770700Z"
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
    private ResponseEntity<?> getNewsFeedController(@RequestParam(defaultValue = "1") int page,
                                                    @RequestParam(value = "page_size", defaultValue = "5") int pageSize,
                                                    Authentication authentication){
        User user = (User) authentication.getPrincipal();
        return buildResponse("Get News Feed successfully", postServicePort.getNewsFeed(page, pageSize, Long.parseLong(user.getUsername())));
    }
}
