package com.example.socialnetwork.common.mapper;
import com.example.socialnetwork.application.response.PostResponse;
import com.example.socialnetwork.domain.model.PostDomain;
import com.example.socialnetwork.domain.port.api.UserServicePort;
import com.example.socialnetwork.infrastructure.entity.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PostMapper {

    private final UserServicePort userServicePort;

    public PostDomain postToPostDomain(Post post) {
        if (post == null) {
            return null;
        } else {
            PostDomain postDomain = new PostDomain();
            postDomain.setUserId(this.postUserId(post));
            postDomain.setPostReactionsIds(this.postReactionsToIds(post.getPostReactions()));
            postDomain.setCommentsIds(this.commentsToIds(post.getComments()));
            postDomain.setTagsIds(this.tagsToIds(post.getTags()));
            postDomain.setId(post.getId());
            postDomain.setContent(post.getContent());
            postDomain.setVisibility(post.getVisibility());
            postDomain.setCreatedAt(post.getCreatedAt());
            postDomain.setUpdatedAt(post.getUpdatedAt());
            postDomain.setLastComment(post.getLastComment());
            postDomain.setPhotoLists(post.getPhotoLists());
            return postDomain;
        }
    }

    public List<PostDomain> toPostDomains(List<Post> posts) {
        if (posts == null) {
            return null;
        } else {
            List<PostDomain> postDomains = new ArrayList<>();
            for (Post post : posts) {
                PostDomain postDomain = this.postToPostDomain(post);
                postDomains.add(postDomain);
            }
            return postDomains;
        }
    }

    public Post postDomainToPost(PostDomain postDomain) {
        if (postDomain == null) {
            return null;
        } else {
            Post post = new Post();
            post.setUser(this.postDomainToUser(postDomain));
            post.setId(postDomain.getId());
            post.setContent(postDomain.getContent());
            post.setVisibility(postDomain.getVisibility());
            post.setCreatedAt(postDomain.getCreatedAt());
            post.setUpdatedAt(postDomain.getUpdatedAt());
            post.setLastComment(postDomain.getLastComment());
            post.setPhotoLists(postDomain.getPhotoLists());
            return post;
        }
    }

    public PostResponse postDomainToPostResponse(PostDomain postDomain) {
        if (postDomain == null) {
            return null;
        } else {
            PostResponse postResponse = new PostResponse();
            postResponse.setNumberOfComments(this.commentsToNumber(postDomain.getCommentsIds()));
            postResponse.setNumberOfReacts(this.postReactionsIdsToNumber(postDomain.getPostReactionsIds()));
            if(postDomain.getPhotoLists() != null) {
                postResponse.setPhotoLists(this.photoToList(postDomain.getPhotoLists()));
            }
            postResponse.setId(postDomain.getId());
            postResponse.setUserId(postDomain.getUserId());
            postResponse.setContent(postDomain.getContent());
            if (postDomain.getVisibility() != null) {
                postResponse.setVisibility(postDomain.getVisibility().name());
            }

            postResponse.setUsername(getUsername(postDomain.getUserId()));
            postResponse.setAvatar(getAvatar(postDomain.getUserId()));

            postResponse.setCreatedAt(postDomain.getCreatedAt());
            postResponse.setUpdatedAt(postDomain.getUpdatedAt());
            List<Long> list1 = postDomain.getTagsIds();
            if (list1 != null) {
                postResponse.setTagsIds(new ArrayList<>(list1));
            }

            return postResponse;
        }
    }

    public List<PostResponse> toPostResponses(List<PostDomain> postDomains) {
        if (postDomains == null) {
            return null;
        } else {
            List<PostResponse> postResponses = new ArrayList<>();
            for (PostDomain postDomain : postDomains) {
                PostResponse postResponse = this.postDomainToPostResponse(postDomain);
                postResponses.add(postResponse);
            }
            return postResponses;
        }
    }

    public String getUsername(Long userId) {
        return userServicePort.findUserById(userId).getUsername();
    }
    public String getAvatar(Long userId) {
        return userServicePort.findUserById(userId).getAvatar();
    }



    private Long postUserId(Post post) {
        if (post == null) {
            return null;
        } else {
            User user = post.getUser();
            if (user == null) {
                return null;
            } else {
                return user.getId();
            }
        }
    }

    protected User postDomainToUser(PostDomain postDomain) {
        if (postDomain == null) {
            return null;
        } else {
            User.UserBuilder user = User.builder();
            user.id(postDomain.getUserId());
            return user.build();
        }
    }

    public Long commentsToNumber(List<Long> comments) {
        return (long) comments.size();
    }

    public Long postReactionsIdsToNumber(List<Long> reactions) {
        return (long) reactions.size();
    }

    public List<String> photoToList(String photo) {
        String[] split = photo.split(",");
        return new ArrayList<>(List.of(split));
    }

    public List<Long> postReactionsToIds(List<PostReaction> reactions) {
        return reactions != null ? reactions.stream().map(PostReaction::getId).collect(Collectors.toList()) : null;
    }

    public List<Long> commentsToIds(List<Comment> comments) {
        return comments != null ? comments.stream().map(Comment::getId).collect(Collectors.toList()) : null;
    }

    public List<Long> tagsToIds(List<Tag> tags) {
        return tags != null ? tags.stream().map(Tag::getId).collect(Collectors.toList()) : null;
    }
}
