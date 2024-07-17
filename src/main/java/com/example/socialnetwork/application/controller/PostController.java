package com.example.socialnetwork.application.controller;

import com.example.socialnetwork.application.response.PostResponse;
import com.example.socialnetwork.domain.port.api.PostServicePort;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/post")
@RequiredArgsConstructor
public class PostController {
    private final PostServicePort postServicePort;

    @GetMapping("/getAll")
    public List<PostResponse> getPosts(@RequestParam Long userId){
        return postServicePort.getAllPosts(userId);
    }
}
