package com.example.demo.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entity.Like;
import com.example.demo.requests.LikeCreateRequest;
import com.example.demo.response.LikeResponse;
import com.example.demo.services.LikeService;
@CrossOrigin("*")
@RestController
@RequestMapping("/likes")
public class LikeController {

    private final LikeService likeService;

    @Autowired
    public LikeController(LikeService likeService) {
        this.likeService = likeService;
    }

    @GetMapping
    public List<LikeResponse> getAllLikes(@RequestParam Optional<Long> userId, 
                                           @RequestParam Optional<Long> postId) {
        return likeService.getAllLikesWithParam(userId, postId);
    }
    @PostMapping
    public ResponseEntity<?> createOneLike(@RequestBody LikeCreateRequest request) {
        if (request.getUserId() == null || request.getPostId() == null) {
            return ResponseEntity.badRequest().body("User ID and Post ID must not be null");
        }

        Like createdLike = likeService.createOneLike(request);
        if (createdLike == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User or Post not found");
        }

        return ResponseEntity.ok(createdLike);
    }


    @GetMapping("/{likeId}")
    public Like getOneLike(@PathVariable Long likeId) {
        return likeService.getOneLikeById(likeId);
    }

    @DeleteMapping("/{likeId}")
    public void deleteOneLike(@PathVariable Long likeId) {
        likeService.deleteOneLikeById(likeId);
    }
}
