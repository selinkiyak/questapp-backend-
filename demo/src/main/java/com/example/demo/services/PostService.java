package com.example.demo.services;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.entity.Post;
import com.example.demo.entity.User;
import com.example.demo.repos.PostRepository;
import com.example.demo.requests.PostCreateRequest;
import com.example.demo.requests.PostUpdateRequest;
import com.example.demo.response.LikeResponse;
import com.example.demo.response.PostResponse;

@Service
public class PostService {

    private final PostRepository postRepository;
    private final UserService userService;
    private LikeService likeService;

    public PostService(PostRepository postRepository, UserService userService) {
        this.postRepository = postRepository;
        this.userService = userService;
    }

    @Autowired
    public void setLikeService(LikeService likeService) {
        this.likeService = likeService;
    }

    public List<PostResponse> getAllPosts(Optional<Long> userId) {
        List<Post> list;
        if (userId.isPresent()) {
            list = postRepository.findByUserId(userId.get());
        } else {
            list = postRepository.findAll();
        }
        return list.stream().map(p -> { 
            List<LikeResponse> likes = likeService.getAllLikesWithParam(Optional.ofNullable(null), Optional.of(p.getId()));
            return new PostResponse(p, likes);
        }).collect(Collectors.toList());
    }

    public Post getOnePostById(Long postId) {
        if (postId == null) {
            throw new IllegalArgumentException("Post ID must not be null");
        }
        return postRepository.findById(postId).orElse(null);
    }

    public PostResponse getOnePostByIdWithLikes(Long postId) {
        if (postId == null) {
            throw new IllegalArgumentException("Post ID must not be null");
        }
        Post post = postRepository.findById(postId).orElse(null);
        List<LikeResponse> likes = likeService.getAllLikesWithParam(Optional.ofNullable(null), Optional.of(postId));
        return new PostResponse(post, likes); 
    }

    public Post createOnePost(PostCreateRequest newPostRequest) {
        if (newPostRequest.getUserId() == null) {
            throw new IllegalArgumentException("UserId must not be null");
        }

        User user = userService.getOneUserById(newPostRequest.getUserId());
        if (user == null) {
            return null;
        }

        Post toSave = new Post();
        toSave.setText(newPostRequest.getText());
        toSave.setTitle(newPostRequest.getTitle());
        toSave.setUser(user);
        toSave.setCreateDate(new Date());
        return postRepository.save(toSave);
    }

    public Post updateOnePostById(Long postId, PostUpdateRequest updatePost) {
        if (postId == null) {
            throw new IllegalArgumentException("Post ID must not be null");
        }

        Optional<Post> post = postRepository.findById(postId);
        if (post.isPresent()) {
            Post toUpdate = post.get();
            toUpdate.setText(updatePost.getText());
            toUpdate.setTitle(updatePost.getTitle());
            postRepository.save(toUpdate);
            return toUpdate;
        }
        return null;
    }

    public void deleteOnePostById(Long postId) {
        if (postId == null) {
            throw new IllegalArgumentException("Post ID must not be null");
        }
        postRepository.deleteById(postId);
    }
}
