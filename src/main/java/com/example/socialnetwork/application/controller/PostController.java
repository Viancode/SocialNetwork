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
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


@RestController
@RequestMapping("/api/v1/post")
@RequiredArgsConstructor
public class PostController extends BaseController {
    private final PostServicePort postServicePort;

    @GetMapping("")
    public ResponseEntity<ResultResponse> getPosts(@RequestParam(defaultValue = "1") int page,
                                                   @RequestParam(defaultValue = "5") int pageSize,
                                                   @RequestParam(defaultValue = "createdAt") String sortBy,
                                                   @RequestParam(defaultValue = "desc") String sortDirection,
                                                   @RequestParam Long targetUserId,
                                                   Authentication authentication) {

        User user = (User) authentication.getPrincipal();
        Page<PostDomain> posts = postServicePort.getAllPosts(page, pageSize, sortBy, sortDirection, Long.valueOf(user.getUsername()), targetUserId);
        Page<PostResponse> postResponses = posts.map(PostMapper.INSTANCE::postDomainToPostResponse);
        return buildResponse("Get post successfully", postResponses);
    }

    @PostMapping("/create")
    public ResponseEntity<?> createPost(
            @RequestParam("content") String content,
            @RequestParam(value = "visibility", defaultValue = "PUBLIC") String visibility,
            @RequestParam("photoLists") String photoLists
    ) {
        PostRequest postRequest = new PostRequest();
        postRequest.setUserId(SecurityUtil.getCurrentUserId());
        postRequest.setContent(content);
        postRequest.setVisibility(visibility);
        postRequest.setPhotoLists(photoLists);

        PostDomain postDomain = postServicePort.createPost(postRequest);
        return buildResponse("Create post successfully", postDomain);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deletePost(@RequestParam Long postId){
        postServicePort.deletePost(postId);
        return buildResponse("Delete post successfully", HttpStatus.ACCEPTED);
    }

    @PutMapping("/update")
    public ResponseEntity<?> updatePost(
            @RequestParam("postId") Long postId,
            @RequestParam("content") String content,
            @RequestParam("visibility") String visibility,
            @RequestParam("photoLists") String photoLists
    ){
        PostRequest postRequest = new PostRequest();
        postRequest.setId(postId);
        postRequest.setUserId(SecurityUtil.getCurrentUserId());
        postRequest.setContent(content);
        postRequest.setVisibility(visibility);
        postRequest.setPhotoLists(photoLists);
        PostDomain postDomain = postServicePort.updatePost(postRequest);
        return buildResponse("Update post successfully", postDomain);
    }
}
