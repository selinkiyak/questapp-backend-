package com.example.demo.controllers;

import org.springframework.web.bind.annotation.*;
import com.example.demo.entity.Comment;
import com.example.demo.requests.CommentCreateRequest;
import com.example.demo.requests.CommentUpdateRequest;
import com.example.demo.response.CommentResponse;
import com.example.demo.services.CommentService;

import java.util.List;
import java.util.Optional;
@CrossOrigin("*")
@RestController
@RequestMapping("/comments")
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @GetMapping
    public List<CommentResponse> getAllComments(@RequestParam Optional<Long> userId, @RequestParam Optional<Long> postId) {
        return commentService.getAllCommentsWithParam(userId, postId);
    }

    @PostMapping
    public Comment createOneComment(@RequestBody CommentCreateRequest request) {
        if (request.getUserId() == null || request.getPostId() == null) {
            throw new IllegalArgumentException("UserId and PostId must not be null");
        }
        return commentService.createOneComment(request);
    }

    @GetMapping("/{commentId}")
    public Comment getOneComment(@PathVariable Long commentId) {
        return commentService.getOneCommentById(commentId);
    }

    @PutMapping("/{commentId}")
    public Comment updateOneComment(@PathVariable Long commentId, @RequestBody CommentUpdateRequest request) {
        return commentService.updateOneCommentById(commentId, request);
    }

    @DeleteMapping("/{commentId}")
    public void deleteOneCommentById(@PathVariable Long commentId) {
        commentService.deleteOneCommentById(commentId);
    }
}
