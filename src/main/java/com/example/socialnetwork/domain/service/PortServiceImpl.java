package com.example.socialnetwork.domain.service;

import com.example.socialnetwork.application.request.PostRequest;
import com.example.socialnetwork.application.response.PageInfo;
import com.example.socialnetwork.application.response.PostResponse;
import com.example.socialnetwork.common.mapper.PostMapper;
import com.example.socialnetwork.domain.model.PostDomain;
import com.example.socialnetwork.domain.port.api.PostServicePort;
import com.example.socialnetwork.domain.port.api.S3ServicePort;
import com.example.socialnetwork.domain.port.spi.PostDatabasePort;
import com.example.socialnetwork.infrastructure.entity.Post;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class PortServiceImpl implements PostServicePort {

    private final PostDatabasePort postDatabasePort;
    private final S3ServicePort s3Service;

    @Override
    public PostDomain createPost(PostRequest postRequest, Authentication authentication) {
        PostDomain postDomain = new PostDomain();
        postDomain.setUserId(postRequest.getUserId());
        postDomain.setContent(postRequest.getContent());
        postDomain.setVisibility(postRequest.getVisibility());

        StringBuilder photoPaths = new StringBuilder();
        for (MultipartFile photo : postRequest.getPhotoLists()) {
            String uniqueFilename = UUID.randomUUID().toString() + "-" + photo.getOriginalFilename();
            try {
                s3Service.putFile(uniqueFilename, photo.getContentType(), photo.getBytes());
                String photoUrl = s3Service.getFileUrl(uniqueFilename);
                photoPaths.append(photoUrl).append(",");
            } catch (Exception e) {
                throw new RuntimeException("Error while uploading file to S3", e);
            }
        }
        // Remove trailing comma
        if (!photoPaths.isEmpty()) {
            photoPaths.deleteCharAt(photoPaths.length() - 1);
        }
        postDomain.setPhotoLists(photoPaths.toString());
        postDomain.setCreatedAt(LocalDateTime.now());



        return postDatabasePort.createPost(postDomain, authentication);
    }

    @Override
    public PostDomain updatePost(PostRequest postRequest, Authentication authentication) {
        PostDomain postDomain = postDatabasePort.findById(postRequest.getId());
        postDomain.setContent(postRequest.getContent());
        postDomain.setVisibility(postRequest.getVisibility());
        StringBuilder photoPaths = new StringBuilder();
        for (MultipartFile photo : postRequest.getPhotoLists()) {
            String uniqueFilename = UUID.randomUUID().toString() + "-" + photo.getOriginalFilename();
            try {
                s3Service.putFile(uniqueFilename, photo.getContentType(), photo.getBytes());
                String photoUrl = s3Service.getFileUrl(uniqueFilename);
                photoPaths.append(photoUrl).append(",");
            } catch (Exception e) {
                throw new RuntimeException("Error while uploading file to S3", e);
            }
        }
        // Remove trailing comma
        if (!photoPaths.isEmpty()) {
            photoPaths.deleteCharAt(photoPaths.length() - 1);
        }
        postDomain.setPhotoLists(photoPaths.toString());
        postDomain.setUpdatedAt(LocalDateTime.now());
        return postDatabasePort.updatePost(postDomain,authentication);
    }

    @Override
    public void deletePost(Long postId) {
        postDatabasePort.deletePost(postId);
    }

    @Override
    public List<PostResponse> getAllPosts(Long userId) {
        return postDatabasePort.getAllPosts(userId).stream().map(PostMapper.INSTANCE::postDomainToPostResponse).collect(Collectors.toList());
    }

    @Override
    public Page<PostDomain> getAllPosts(Long userId, int offset, int pageSize) {
        return postDatabasePort.getAllPosts(userId, offset, pageSize);
    }


}
