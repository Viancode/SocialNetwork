package com.example.socialnetwork.application.controller;

import com.example.socialnetwork.application.request.CommentRequest;
import com.example.socialnetwork.application.response.CommentResponse;
import com.example.socialnetwork.application.response.ResultResponse;
import com.example.socialnetwork.common.mapper.CommentMapper;
import com.example.socialnetwork.common.util.SecurityUtil;
import com.example.socialnetwork.domain.model.CommentDomain;
import com.example.socialnetwork.domain.port.api.CommentServicePort;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/v1/comment")
@RequiredArgsConstructor
public class CommentController extends BaseController {
    private final CommentServicePort commentServicePort;

//    @GetMapping("/{postId}")
//    public ResponseEntity<ResultResponse> getComments(@PathVariable Long postId,
//                                                      @RequestParam(defaultValue = "1") int page,
//                                                      @RequestParam(defaultValue = "5") int pageSize,
//                                                      @RequestParam(defaultValue = "createdAt") String sortBy,
//                                                      @RequestParam(defaultValue = "desc") String sortDirection) {
//        Page<CommentResponse> comments = commentServicePort.getAllComments(postId, page, pageSize, sortBy, sortDirection);
//        return buildResponse("Get comments successfully", comments);
//    }

    @PostMapping("/")
    public ResponseEntity<?> createComment(@ModelAttribute CommentRequest commentRequest, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        CommentDomain newComment = commentServicePort.createComment(Long.valueOf(user.getUsername()),commentRequest);
        return buildResponse("Create comment successfully", CommentMapper.INSTANCE.commentDomainToCommentResponse(newComment));
    }

    @PutMapping("/")
    public ResponseEntity<?> updateComment(@ModelAttribute CommentRequest commentRequest, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        CommentDomain commentDomain = commentServicePort.updateComment(Long.valueOf(user.getUsername()), commentRequest);
        return buildResponse("Update comment successfully", CommentMapper.INSTANCE.commentDomainToCommentResponse(commentDomain));
    }

    @DeleteMapping("/")
    public ResponseEntity<?> deleteComment(
            @RequestParam("commentId") Long commentId,
            Authentication authentication
    ) {
        User user = (User) authentication.getPrincipal();
        Long userId = Long.valueOf(user.getUsername());
        commentServicePort.deleteComment(userId,commentId);
        return buildResponse("Delete comment successfully");
    }
}
