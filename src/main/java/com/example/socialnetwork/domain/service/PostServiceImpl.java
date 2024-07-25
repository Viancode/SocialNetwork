package com.example.socialnetwork.domain.service;

import com.example.socialnetwork.application.request.PostRequest;
import com.example.socialnetwork.common.constant.Visibility;
import com.example.socialnetwork.domain.model.PostDomain;
import com.example.socialnetwork.domain.port.api.PostServicePort;
import com.example.socialnetwork.domain.port.api.StorageServicePort;
import com.example.socialnetwork.domain.port.spi.PostDatabasePort;
import com.example.socialnetwork.exception.custom.ClientErrorException;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@RequiredArgsConstructor
public class PostServiceImpl implements PostServicePort {

    private final PostDatabasePort postDatabasePort;
    private final StorageServicePort storageServicePort;

    @Override
    public PostDomain createPost(PostRequest postRequest) {
        PostDomain postDomain = new PostDomain();
        postDomain.setUserId(postRequest.getUserId());
        postDomain.setContent(postRequest.getContent());
        postDomain.setVisibility(Visibility.valueOf(postRequest.getVisibility()));
        postDomain.setPhotoLists(postRequest.getPhotoLists());
        postDomain.setCreatedAt(LocalDateTime.now());
        return postDatabasePort.createPost(postDomain);
    }

    @Override
    public PostDomain updatePost(PostRequest postRequest) {
        PostDomain postDomain = postDatabasePort.findById(postRequest.getId());
        if (postRequest.getContent().isEmpty()) {
            throw new ClientErrorException("Content is empty");
        } else {
            postDomain.setContent(postRequest.getContent());
        }
        postDomain.setVisibility(Visibility.valueOf(postRequest.getVisibility()));
        postDomain.setPhotoLists(postRequest.getPhotoLists());
        postDomain.setUpdatedAt(LocalDateTime.now());
        return postDatabasePort.updatePost(postDomain);
    }

    @Override
    public void deletePost( Long postId) {
        postDatabasePort.deletePost(postId);
    }

//    @Override
//    public Page<PostResponse> getAllPostByUserId(int offset, int pageSize) {
//        return postDatabasePort.getAllPostByUserId( offset,  pageSize);
//    }

//    @Override
//    public Page<PostDomain> getAllPostsOfOtherUser(Long otherUserId, int offset, int pageSize) {
//        return postDatabasePort.getAllPostsOfOtherUser(otherUserId, offset, pageSize);
//    }

//    public String loadFileImage(PostRequest postRequest){
//        StringBuilder photoPaths = new StringBuilder();
//        if (postRequest.getPhotoLists() != null) {
//            if(postRequest.getPhotoLists().length < 4){
//                for (MultipartFile photo : postRequest.getPhotoLists()) {
//                    String filePath = storageServicePort.store(FileType.IMAGE, photo);
//                    String photoUrl = storageServicePort.getUrl(filePath);
//                    photoPaths.append(photoUrl).append(",");
//                }
//                // Remove trailing comma
//                if (!photoPaths.isEmpty()) {
//                    photoPaths.deleteCharAt(photoPaths.length() - 1);
//                }
//            }else{
//                throw new ClientErrorException("Exceeded number of allowed files");
//            }
//        }
//        return photoPaths.toString();
//    }


}
