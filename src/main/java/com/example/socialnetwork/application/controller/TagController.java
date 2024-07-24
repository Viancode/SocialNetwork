package com.example.socialnetwork.application.controller;

import com.example.socialnetwork.domain.model.TagDomain;
import com.example.socialnetwork.domain.port.api.TagServicePort;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/tag")
@RequiredArgsConstructor
public class TagController extends BaseController{
    private final TagServicePort tagServicePort;

    @PostMapping("/create")
    public ResponseEntity<?> createTag(@RequestBody TagDomain tagDomain) {
        return buildResponse("Create tag successfully", tagServicePort.createTag(tagDomain));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteTag(@RequestParam Long tagId) {
        tagServicePort.deleteTag(tagId);
        return buildResponse("Delete tag successfully", HttpStatus.ACCEPTED);
    }
}
