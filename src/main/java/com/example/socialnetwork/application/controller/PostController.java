package com.example.socialnetwork.application.controller;

import com.example.socialnetwork.application.request.PostRequest;
import com.example.socialnetwork.application.response.PostResponse;
import com.example.socialnetwork.domain.model.PostDomain;
import com.example.socialnetwork.domain.port.api.PostServicePort;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1/post")
@RequiredArgsConstructor
public class PostController {
    private final PostServicePort postServicePort;

    @GetMapping("/getAll")
    public ResponseEntity<List<PostResponse>> getPosts(@RequestParam Long userId){
        return ResponseEntity.ok(postServicePort.getAllPosts(userId));
    }

    @PostMapping("/create")
    public ResponseEntity<PostDomain> createPost(
            @RequestParam("userId") Long userId,
            @RequestParam("content") String content,
            @RequestParam("visibility") String visibility,
            @RequestParam("photoLists") MultipartFile[] photoLists) {

        PostRequest postRequest = new PostRequest();
        postRequest.setUserId(userId);
        postRequest.setContent(content);
        postRequest.setVisibility(visibility);
        postRequest.setPhotoLists(photoLists);

        PostDomain postDomain = postServicePort.createPost(postRequest);
        return ResponseEntity.ok(postDomain);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Void> deletePost(@RequestParam Long postId){
        postServicePort.deletePost(postId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PutMapping("/update")
    public ResponseEntity<PostDomain> updatePost(
            @RequestParam("postId") Long postId,
            @RequestParam("userId") Long userId,
            @RequestParam("content") String content,
            @RequestParam("visibility") String visibility,
            @RequestParam("photoLists") MultipartFile[] photoLists
    ){
        PostRequest postRequest = new PostRequest();
        postRequest.setId(postId);
        postRequest.setUserId(userId);
        postRequest.setContent(content);
        postRequest.setVisibility(visibility);
        postRequest.setPhotoLists(photoLists);
        PostDomain postDomain = postServicePort.updatePost(postRequest);
        return ResponseEntity.ok(postDomain);
    }
}
