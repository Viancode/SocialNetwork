package com.example.socialnetwork.application.controller;

import com.example.socialnetwork.application.request.PostReactionRequest;
import com.example.socialnetwork.application.response.PostReactionResponse;
import com.example.socialnetwork.application.response.ResultResponse;
import com.example.socialnetwork.common.mapper.PostReactionMapper;
import com.example.socialnetwork.domain.model.PostReactionDomain;
import com.example.socialnetwork.domain.port.api.PostReactionServicePort;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/post_reaction")
@RequiredArgsConstructor
public class PostReactionController extends BaseController {
    private final PostReactionServicePort postReactionService;

    @PostMapping("")
    public ResponseEntity<?> createPostReaction(
            @RequestBody PostReactionRequest postReactionRequest
    ) {
        PostReactionDomain postReactionDomain = postReactionService.createPostReaction(PostReactionMapper.INSTANCE.requestToDomain(postReactionRequest));
        return buildResponse("Create post reaction successfully", PostReactionMapper.INSTANCE.domainToResponse(postReactionDomain));
    }

    @DeleteMapping("")
    public ResponseEntity<?> deletePostReaction(
            @RequestParam Long postReactionId
    ){
        postReactionService.deletePostReaction(postReactionId);
        return buildResponse("Delete post reaction successfully");
    }


    @GetMapping("")
    public ResponseEntity<ResultResponse> getPosts(@RequestParam(defaultValue = "1") int page,
                                                   @RequestParam(defaultValue = "5") int pageSize,
                                                   @RequestParam(defaultValue = "createdAt") String sortBy,
                                                   @RequestParam(defaultValue = "desc") String sortDirection,
                                                   @RequestParam Long postId,
                                                   @RequestParam String postReactionType
                                                    ) {

        Page<PostReactionDomain> postReactionDomainPage = postReactionService.getAllPostReactions(page,pageSize,sortBy,sortDirection,postId,postReactionType);
        Page<PostReactionResponse> postReactionResponsePage = postReactionDomainPage.map(PostReactionMapper.INSTANCE::domainToResponse);
        return buildResponse("Get post successfully", postReactionResponsePage);
    }

}
