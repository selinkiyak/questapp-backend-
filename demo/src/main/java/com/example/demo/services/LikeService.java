package com.example.demo.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


import org.springframework.stereotype.Service;

import com.example.demo.entity.Like;
import com.example.demo.entity.Post;
import com.example.demo.entity.User;
import com.example.demo.repos.LikeRepository;
import com.example.demo.requests.LikeCreateRequest;
import com.example.demo.response.LikeResponse;
@Service
public class LikeService {

	private LikeRepository likeRepository;
	private UserService userService;
	private PostService postService;

	public LikeService(LikeRepository likeRepository, UserService userService, 
			PostService postService) {
		this.likeRepository = likeRepository;
		this.userService = userService;
		this.postService = postService;
	}

	public List<LikeResponse> getAllLikesWithParam(Optional<Long> userId, Optional<Long> postId) {
		List<Like> list;
		if(userId.isPresent() && postId.isPresent()) {
			list = likeRepository.findByUserIdAndPostId(userId.get(), postId.get());
		}else if(userId.isPresent()) {
			list = likeRepository.findByUserId(userId.get());
		}else if(postId.isPresent()) {
			list = likeRepository.findByPostId(postId.get());
		}else
			list = likeRepository.findAll();
		return list.stream().map(like -> new LikeResponse(like)).collect(Collectors.toList());
	}

	public Like getOneLikeById(Long LikeId) {
		return likeRepository.findById(LikeId).orElse(null);
	}

	public Like createOneLike(LikeCreateRequest request) {
	    // Kullanıcıyı ve gönderiyi veritabanından al
	    User user = userService.getOneUserById(request.getUserId());
	    Post post = postService.getOnePostById(request.getPostId());

	    // Kullanıcı veya gönderi bulunamazsa uygun istisna fırlat
	    if (user == null) {
	        throw new IllegalArgumentException("User not found with ID: " + request.getUserId());
	    }
	    if (post == null) {
	        throw new IllegalArgumentException("Post not found with ID: " + request.getPostId());
	    }

	    // Like nesnesini oluştur ve kaydet
	    Like likeToSave = new Like();
	    likeToSave.setId(request.getUserId());
	    likeToSave.setPost(post);
	    likeToSave.setUser(user);

	    return likeRepository.save(likeToSave);
	}


	public void deleteOneLikeById(Long likeId) {
		likeRepository.deleteById(likeId);
	}
	
	
}