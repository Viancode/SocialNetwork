package com.example.socialnetwork.application.controller;

import com.example.socialnetwork.application.request.PostRequest;
import com.example.socialnetwork.application.response.PostResponse;
import com.example.socialnetwork.application.response.ResultResponse;
import com.example.socialnetwork.common.mapper.PostMapper;
import com.example.socialnetwork.common.util.SecurityUtil;
import com.example.socialnetwork.domain.model.PostDomain;
import com.example.socialnetwork.domain.port.api.PostServicePort;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/post")
@RequiredArgsConstructor
public class PostController extends BaseController {
    private final PostServicePort postServicePort;
    private final PostMapper postMapper;

    @GetMapping("")
    public ResponseEntity<ResultResponse> getPosts(@RequestParam(defaultValue = "1") int page,
                                                   @RequestParam(value = "page_size",defaultValue = "5") int pageSize,
                                                   @RequestParam(value = "sort_by", defaultValue = "createdAt") String sortBy,
                                                   @RequestParam(value = "sort_direction", defaultValue = "desc") String sortDirection,
                                                   @RequestParam(required = false, value = "target_user_id") Long targetUserId,
                                                   Authentication authentication) {

        User user = (User) authentication.getPrincipal();
        Page<PostResponse> posts = postServicePort.getAllPosts(page, pageSize, sortBy, sortDirection, Long.valueOf(user.getUsername()), targetUserId);
        return buildResponse("Get post successfully", posts);
    }

    @PostMapping("")
    public ResponseEntity<?> createPost(
            @RequestBody PostRequest postRequest
    ) {
        PostDomain postDomain = postServicePort.createPost(postMapper.requestToDomain(postRequest));
        return buildResponse("Create post successfully", postMapper.domainToResponse(postDomain));
    }

    @DeleteMapping("")
    public ResponseEntity<?> deletePost(@RequestParam(value = "post_id") Long postId){
        postServicePort.deletePost(postId);
        return buildResponse("Delete post successfully", HttpStatus.ACCEPTED);
    }

    @PutMapping("")
    public ResponseEntity<?> updatePost(
            @RequestBody PostRequest postRequest
    ){
        PostDomain postDomain = postServicePort.updatePost(postMapper.requestToDomain(postRequest));
        return buildResponse("Update post successfully", postMapper.domainToResponse(postDomain));
    }

    @GetMapping("/number_post")
    public ResponseEntity<?> getNumberPost(){
        Long numberPost = postServicePort.countPostByUserId();
        return buildResponse("Get number post successfully", numberPost);
    }
}
