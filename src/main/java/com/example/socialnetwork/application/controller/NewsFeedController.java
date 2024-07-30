package com.example.socialnetwork.application.controller;

import com.example.socialnetwork.domain.port.api.PostServicePort;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class NewsFeedController extends BaseController{
    private final PostServicePort postServicePort;

    @GetMapping
    private ResponseEntity<?> getNewsFeedController(@RequestParam(defaultValue = "1") int page,
                                                    @RequestParam(defaultValue = "5") int pageSize,
                                                    @RequestParam(defaultValue = "createdAt") String sortBy){
        return buildResponse("Get News Feed successfully", postServicePort.getNewsFeed(page, pageSize, sortBy));
    }
}
