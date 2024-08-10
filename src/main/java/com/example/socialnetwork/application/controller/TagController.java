package com.example.socialnetwork.application.controller;

import com.example.socialnetwork.application.request.TagRequest;
import com.example.socialnetwork.application.response.PostReactionResponse;
import com.example.socialnetwork.application.response.TagResponse;
import com.example.socialnetwork.common.mapper.PostReactionMapper;
import com.example.socialnetwork.common.mapper.TagMapper;
import com.example.socialnetwork.domain.model.PostReactionDomain;
import com.example.socialnetwork.domain.model.TagDomain;
import com.example.socialnetwork.domain.model.UserDomain;
import com.example.socialnetwork.domain.port.api.TagServicePort;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/tag")
@RequiredArgsConstructor
public class TagController extends BaseController{
    private final TagServicePort tagServicePort;
    private final TagMapper tagMapper;

    @PostMapping
    public ResponseEntity<?> createTag(@RequestBody TagRequest tagRequest) {
        TagDomain tagDomain = tagServicePort.createTag(tagMapper.requestToDomain(tagRequest));
        return buildResponse("Create tag successfully", tagMapper.domainToResponse(tagDomain));
    }

    @DeleteMapping
    public ResponseEntity<?> deleteTag(@RequestParam Long tagId) {
        tagServicePort.deleteTag(tagId);
        return buildResponse("Delete tag successfully", HttpStatus.ACCEPTED);
    }

    @GetMapping
    public ResponseEntity<?> getAllTags(@RequestParam(defaultValue = "1") int page,
                                        @RequestParam(defaultValue = "5") int pageSize,
                                        @RequestParam(defaultValue = "createdAt") String sortBy,
                                        @RequestParam(defaultValue = "desc") String sortDirection,
                                        @RequestParam(required = false) Long postId) {
        Page<TagDomain> tagDomainPage = tagServicePort.getAllTags(page,pageSize,sortBy,sortDirection,postId);
        Page<TagResponse> tagResponsePage = tagDomainPage.map(tagMapper::domainToResponse);

        return buildResponse("Get all tags", tagResponsePage);
    }
}
