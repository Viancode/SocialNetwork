package com.example.socialnetwork.application.request;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostRequest {
    private Long id = null;

    private Long userId;

    private String content;

    private String visibility;

    private MultipartFile[] photoLists;

}
