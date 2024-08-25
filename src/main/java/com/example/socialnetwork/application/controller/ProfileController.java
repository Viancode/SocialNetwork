package com.example.socialnetwork.application.controller;

import com.example.socialnetwork.application.request.ProfileRequest;
import com.example.socialnetwork.application.response.ProfileResponse;
import com.example.socialnetwork.application.response.ResultResponse;
import com.example.socialnetwork.common.constant.FileType;
import com.example.socialnetwork.common.mapper.UserMapper;
import com.example.socialnetwork.domain.port.api.StorageServicePort;
import com.example.socialnetwork.domain.port.api.UserServicePort;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/profile")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Profile")
public class ProfileController extends BaseController {
    private final UserServicePort userService;
    private final UserMapper userMapper;

    @Operation(
            summary = "Get profile of an user",
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
                                                            "message": "Get profile successfully",
                                                            "result": {
                                                                "data": {
                                                                    "username": "user1",
                                                                    "email": "user1@gmail.com",
                                                                    "firstName": "First1",
                                                                    "lastName": "Last1",
                                                                    "gender": "MALE",
                                                                    "visibility": "PUBLIC",
                                                                    "bio": null,
                                                                    "location": "Ha Noi",
                                                                    "work": "Ha Noi",
                                                                    "education": "HUST",
                                                                    "createdAt": "2024-08-12T08:56:49Z",
                                                                    "updatedAt": "2024-08-12T08:56:49Z",
                                                                    "avatar": null,
                                                                    "backgroundImage": null,
                                                                    "dateOfBirth": "1990-01-01"
                                                                }
                                                            },
                                                            "timestamp": "2024-08-13T00:41:22.073430290Z"
                                                        }
                                                    """
                                    )
                            )
                    ),
                    @ApiResponse(description = "The access token provided is expired, revoked, malformed, or invalid for other reasons.",
                            responseCode = "401", content = @Content()),
                    @ApiResponse(description = "User not found", responseCode = "404", content = @Content()),
                    @ApiResponse(description = "Required request parameter 'target_user_id' for method parameter type Long is not present", responseCode = "400", content = @Content())
            }
    )
    @GetMapping("")
    public ResponseEntity<ResultResponse> getProfile(@RequestParam(value = "target_user_id") Long targetUserID,
                                                     Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        Long sourceUserId = Long.parseLong(user.getUsername());
        ProfileResponse profile = userMapper.toProfileResponse(userService.getProfile(sourceUserId, targetUserID));
        return buildResponse("Get profile successfully", profile);
    }

    @Operation(
            summary = "Update profile of an user",
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
                                                            "message": "Update profile successfully",
                                                            "result": {},
                                                            "timestamp": "2024-08-13T00:41:22.073430290Z"
                                                        }
                                                    
                                                    """
                                    )
                            )
                    ),
                    @ApiResponse(description = "The access token provided is expired, revoked, malformed, or invalid for other reasons.",
                            responseCode = "401", content = @Content()),
            }
    )
    @PutMapping("")
    public ResponseEntity<ResultResponse> updateProfile(@ModelAttribute ProfileRequest profileRequest,
                                                        @RequestParam(value = "is_delete_avt", defaultValue = "false") Boolean isDeleteAvt,
                                                        @RequestParam(value = "is_delete_background", defaultValue = "false") Boolean isDeleteBackground,
                                                        Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        Long userId = Long.parseLong(user.getUsername());
        userService.updateProfile(userId, profileRequest, isDeleteAvt, isDeleteBackground);
        return buildResponse("Update profile successfully");
    }

    @Operation(
            summary = "Delete profile of an user",
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
                                                            "message": "Delete profile successfully",
                                                            "result": {},
                                                            "timestamp": "2024-08-13T00:41:22.073430290Z"
                                                        }
                                                    
                                                    """
                                    )
                            )
                    ),
                    @ApiResponse(description = "The access token provided is expired, revoked, malformed, or invalid for other reasons.",
                            responseCode = "401", content = @Content()),
            }
    )
    @DeleteMapping("")
    public ResponseEntity<ResultResponse> deleteProfile(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        Long userId = Long.parseLong(user.getUsername());
        userService.deleteProfile(userId);
        return buildResponse("Delete profile successfully");
    }
}
