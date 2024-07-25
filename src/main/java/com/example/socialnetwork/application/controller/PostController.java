package com.example.socialnetwork.application.controller;

import com.example.socialnetwork.application.request.PostRequest;
import com.example.socialnetwork.application.response.ResultResponse;
import com.example.socialnetwork.domain.model.PostDomain;
import com.example.socialnetwork.domain.port.api.PostServicePort;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@RestController
@RequestMapping("/api/v1/post")
@RequiredArgsConstructor
public class PostController extends BaseController {
    private final PostServicePort postServicePort;

//    @GetMapping("/")
//    public ResponseEntity<ResultResponse> getPosts(@RequestParam Long userId,
//                                                   @RequestParam Long otherUserId,
//                                                   @RequestParam(defaultValue = "0") int offset,
//                                                   @RequestParam(defaultValue = "10") int pageSize) {
//        Page<PostDomain> posts = postServicePort.getAllPosts(userId,otherUserId, offset-1, pageSize);
//        return buildResponse("Successfully get all post from userId", posts);
//    }

    @GetMapping("/")
    public ResponseEntity<ResultResponse> getPosts(@RequestParam(defaultValue = "1") int page,
                                                   @RequestParam(defaultValue = "10") int pageSize,
                                                   @RequestParam(defaultValue = "created_at") String sortBy,
                                                   @RequestParam Long targetUserId,
                                                   Authentication authentication) {

        User user = (User) authentication.getPrincipal();
        Page<PostDomain> posts = postServicePort.getAllPosts(page, pageSize, sortBy, Long.valueOf(user.getUsername()), targetUserId);
        return buildResponse("Successfully get all post from userId", posts);
    }

    @PostMapping("/create")
    public ResponseEntity<?> createPost(
            @RequestParam("userId") Long userId,
            @RequestParam("content") String content,
            @RequestParam(value = "visibility", defaultValue = "PUBLIC") String visibility,
            @RequestParam("photoLists") MultipartFile[] photoLists,
            Authentication authentication
    ) {

        PostRequest postRequest = new PostRequest();
        postRequest.setUserId(userId);
        postRequest.setContent(content);
        postRequest.setVisibility(visibility);
        postRequest.setPhotoLists(photoLists);

        PostDomain postDomain = postServicePort.createPost(postRequest,authentication);
        return buildResponse("Create post successfully", postDomain);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deletePost(@RequestParam Long userId,@RequestParam Long postId){
        postServicePort.deletePost(userId,postId);
        return buildResponse("Delete post successfully", HttpStatus.ACCEPTED);
    }

    @PutMapping("/update")
    public ResponseEntity<?> updatePost(
            @RequestParam("postId") Long postId,
            @RequestParam("userId") Long userId,
            @RequestParam("content") String content,
            @RequestParam("visibility") String visibility,
            @RequestParam("photoLists") MultipartFile[] photoLists,
            Authentication authentication
    ){
        PostRequest postRequest = new PostRequest();
        postRequest.setId(postId);
        postRequest.setUserId(userId);
        postRequest.setContent(content);
        postRequest.setVisibility(visibility);
        postRequest.setPhotoLists(photoLists);
        PostDomain postDomain = postServicePort.updatePost(postRequest,authentication);
        return buildResponse("Update post successfully", postDomain);
    }
}
