package com.example.socialnetwork.application.controller;

import com.example.socialnetwork.application.request.PostRequest;
import com.example.socialnetwork.application.response.PostResponse;
import com.example.socialnetwork.application.response.ResultResponse;
import com.example.socialnetwork.common.util.SecurityUtil;
import com.example.socialnetwork.domain.model.PostDomain;
import com.example.socialnetwork.domain.port.api.PostServicePort;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/post")
@RequiredArgsConstructor
public class PostController extends BaseController {
    private final PostServicePort postServicePort;

//    @GetMapping("/getAllPostsOfOtherUser")
//    public ResponseEntity<ResultResponse> getAllPostsOfOtherUser(
//                                                   @RequestParam Long otherUserId,
//                                                   @RequestParam(defaultValue = "0") int offset,
//                                                   @RequestParam(defaultValue = "10") int pageSize) {
//        Page<PostDomain> posts = postServicePort.getAllPostsOfOtherUser(otherUserId, offset-1, pageSize);
//        return buildResponse("Successfully get all post from userId", posts);
//    }
//
//    @GetMapping("/getAllPost")
//    public ResponseEntity<ResultResponse> getAllPostByUserId(
//            @RequestParam(defaultValue = "0") int offset,
//            @RequestParam(defaultValue = "10") int pageSize) {
//        Page<PostResponse> posts = postServicePort.getAllPostByUserId();
//        return buildResponse("Successfully get all post from userId", posts);
//    }


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
