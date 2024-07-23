package com.example.socialnetwork.domain.service;

import com.example.socialnetwork.application.request.PostRequest;
import com.example.socialnetwork.application.response.PageInfo;
import com.example.socialnetwork.application.response.PostResponse;
import com.example.socialnetwork.common.constant.FileType;
import com.example.socialnetwork.common.constant.Visibility;
import com.example.socialnetwork.common.mapper.PostMapper;
import com.example.socialnetwork.domain.model.PostDomain;
import com.example.socialnetwork.domain.port.api.PostServicePort;
import com.example.socialnetwork.domain.port.api.S3ServicePort;
import com.example.socialnetwork.domain.port.api.StorageServicePort;
import com.example.socialnetwork.domain.port.spi.PostDatabasePort;
import com.example.socialnetwork.exception.custom.ClientErrorException;
import com.example.socialnetwork.exception.custom.NotFoundException;
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
    private final StorageServicePort storageServicePort;

    @Override
    public PostDomain createPost(PostRequest postRequest, Authentication authentication) {
        PostDomain postDomain = new PostDomain();
        postDomain.setUserId(postRequest.getUserId());
        postDomain.setContent(postRequest.getContent());
        postDomain.setVisibility(Visibility.valueOf(postRequest.getVisibility()));

        String photoPaths = loadFileImage(postRequest);
        postDomain.setPhotoLists(photoPaths);
        postDomain.setCreatedAt(LocalDateTime.now());
        return postDatabasePort.createPost(postDomain, authentication);
    }

    @Override
    public PostDomain updatePost(PostRequest postRequest, Authentication authentication) {
        PostDomain postDomain = postDatabasePort.findById(postRequest.getId());
        if (postRequest.getContent().isEmpty()) {
            throw new ClientErrorException("Content is empty");
        } else {
            postDomain.setContent(postRequest.getContent());
        }
        postDomain.setVisibility(Visibility.valueOf(postRequest.getVisibility()));

        String photoPaths = loadFileImage(postRequest);
        postDomain.setPhotoLists(photoPaths);
        postDomain.setUpdatedAt(LocalDateTime.now());
        return postDatabasePort.updatePost(postDomain,authentication);
    }

    @Override
    public void deletePost(Long userId, Long postId) {
        postDatabasePort.deletePost(userId,postId);
    }

//    @Override
//    public List<PostResponse> getAllPosts(Long userId) {
//        return postDatabasePort.getAllPosts(userId).stream().map(PostMapper.INSTANCE::postDomainToPostResponse).collect(Collectors.toList());
//    }

    @Override
    public Page<PostDomain> getAllPosts(Long userId, Long otherUserId, int offset, int pageSize) {
        return postDatabasePort.getAllPosts(userId,otherUserId, offset, pageSize);
    }

    public String loadFileImage(PostRequest postRequest){
        StringBuilder photoPaths = new StringBuilder();
        if (postRequest.getPhotoLists() != null) {
            if(postRequest.getPhotoLists().length < 4){
                for (MultipartFile photo : postRequest.getPhotoLists()) {
                    String filePath = storageServicePort.store(FileType.IMAGE, photo);
                    String photoUrl = storageServicePort.getUrl(filePath);
                    photoPaths.append(photoUrl).append(",");
                }
                // Remove trailing comma
                if (!photoPaths.isEmpty()) {
                    photoPaths.deleteCharAt(photoPaths.length() - 1);
                }
            }else{
                throw new ClientErrorException("Exceeded number of allowed files");
            }
        }
        return photoPaths.toString();
    }


}
