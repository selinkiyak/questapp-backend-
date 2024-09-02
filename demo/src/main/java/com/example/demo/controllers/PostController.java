package com.example.demo.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entity.Post;
import com.example.demo.requests.PostCreateRequest;
import com.example.demo.requests.PostUpdateRequest;
import com.example.demo.response.PostResponse;
import com.example.demo.services.PostService;

@RestController
@RequestMapping("/posts")
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @PostMapping
    public ResponseEntity<Post> createOnePost(@RequestBody PostCreateRequest newPostRequest) {
        System.out.println("Received data:");
        System.out.println("Title: " + newPostRequest.getTitle());
        System.out.println("UserId: " + newPostRequest.getUserId());
        System.out.println("Text: " + newPostRequest.getText());

        try {
            if (newPostRequest.getUserId() == null) {
                return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
            }

            Post createdPost = postService.createOnePost(newPostRequest);
            return new ResponseEntity<>(createdPost, HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace(); // Hata detaylarını görmek için
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping
    public ResponseEntity<List<PostResponse>> getAllPosts(@RequestParam Optional<Long> userId) {
        try {
            List<PostResponse> posts = postService.getAllPosts(userId);
            return new ResponseEntity<>(posts, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace(); // Hata detaylarını görmek için
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{postId}")
    public ResponseEntity<Post> getOnePost(@PathVariable Long postId) {
        try {
            Post post = postService.getOnePostById(postId);
            if (post != null) {
                return new ResponseEntity<>(post, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            e.printStackTrace(); // Hata detaylarını görmek için
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{postId}")
    public ResponseEntity<Post> updateOnePost(@PathVariable Long postId, @RequestBody PostUpdateRequest updatePost) {
        try {
            Post updatedPost = postService.updateOnePostById(postId, updatePost);
            if (updatedPost != null) {
                return new ResponseEntity<>(updatedPost, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            e.printStackTrace(); // Hata detaylarını görmek için
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> deleteOnePost(@PathVariable Long postId) {
        try {
            postService.deleteOnePostById(postId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            e.printStackTrace(); // Hata detaylarını görmek için
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
