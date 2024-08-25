package com.example.socialnetwork.application.controller;

import com.example.socialnetwork.application.response.ResultResponse;
import com.example.socialnetwork.common.constant.FileType;
import com.example.socialnetwork.domain.port.api.StorageServicePort;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1/images")
@SecurityRequirement(name = "bearerAuth")
@RequiredArgsConstructor
@Tag(name = "Image")
public class ImageController extends BaseController {
    private final StorageServicePort storageService;

    @Operation(
            summary = "Upload image",
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
                                                            "message": "Upload successfully",
                                                            "result": {
                                                                "data": [
                                                                    "https://ghtk-socialnetwork.s3.ap-southeast-2.amazonaws.com/images/dbd54df2-a3e5-4037-af75-76aae7fb37f8.png"
                                                                ]
                                                            },
                                                            "timestamp": "2024-08-25T21:52:07.399942100Z"
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
    @PostMapping("/upload")
    public ResponseEntity<ResultResponse> uploadProfileImage(@RequestParam MultipartFile[] file) {
        List<String> filePath = new ArrayList<>();
        for (MultipartFile f : file) {
            String fileName = storageService.store(FileType.IMAGE, f);
            filePath.add(storageService.getUrl(fileName));
        }
        return buildResponse("Upload successfully", filePath);
    }
}
