package com.example.demo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.entity.User;
import com.example.demo.repos.CommentRepository;
import com.example.demo.repos.LikeRepository;
import com.example.demo.repos.PostRepository;
import com.example.demo.repos.UserRepository;
import com.example.demo.utils.PasswordUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final LikeRepository likeRepository;
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;

    @Autowired
    public UserService(UserRepository userRepository, 
                       LikeRepository likeRepository, 
                       CommentRepository commentRepository, 
                       PostRepository postRepository) {
        this.userRepository = userRepository;
        this.likeRepository = likeRepository;
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User saveOneUser(User newUser) {
        return userRepository.save(newUser);
    }

    public User getOneUserById(Long userId) {
        return userRepository.findById(userId).orElse(null);
    }

    public User updateOneUser(Long userId, User newUser) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent()) {
            User foundUser = userOptional.get();
            foundUser.setUserName(newUser.getUserName());
            if (newUser.getPassword() != null && !newUser.getPassword().isEmpty()) {
                foundUser.setPassword(PasswordUtils.hashPassword(newUser.getPassword()));
            }
            foundUser.setSurname(newUser.getSurname());
            foundUser.setBirthDate(newUser.getBirthDate());
            foundUser.setZodiac(newUser.getZodiac());
            foundUser.setProfileImage(newUser.getProfileImage());
            foundUser.setAvatar(newUser.getAvatar());
            return userRepository.save(foundUser);
        }
        return null;
    }

    public void deleteById(Long userId) {
        try {
            userRepository.deleteById(userId);
        } catch (EmptyResultDataAccessException e) {
            System.out.println("User " + userId + " doesn't exist");
        }
    }

    public User getOneUserByUserName(String userName) {
        return userRepository.findByUserName(userName);
    }

    public List<Object> getUserActivity(Long userId) {
        List<Long> postIds = postRepository.findTopByUserId(userId);
        if (postIds.isEmpty()) {
            return new ArrayList<>();
        }
        List<Object> comments = commentRepository.findUserCommentsByPostId(postIds);
        List<Object> likes = likeRepository.findUserLikesByPostId(postIds);
        List<Object> result = new ArrayList<>();
        result.addAll(comments);
        result.addAll(likes);
        return result;
    }

    public User registerUser(String userName, String password) {
        Assert.notNull(userName, "Username must not be null");
        Assert.notNull(password, "Password must not be null");

        if (userRepository.findByUserName(userName) != null) {
            throw new IllegalArgumentException("Username already exists");
        }

        String hashedPassword = PasswordUtils.hashPassword(password);
        User newUser = new User();
        newUser.setUserName(userName);
        newUser.setPassword(hashedPassword);

        return userRepository.save(newUser);
    }

    public User loginUser(String userName, String password) {
        User user = userRepository.findByUserName(userName);
        if (user != null && PasswordUtils.matches(password, user.getPassword())) {
            return user;
        }
        return null;
    }

    public User updateProfile(Long userId, String username, String surname, 
                              String birthDate, String zodiac, MultipartFile profileImage, MultipartFile avatar) {
        Optional<User> existingUserOpt = userRepository.findById(userId);
        if (existingUserOpt.isPresent()) {
            User existingUser = existingUserOpt.get();
            existingUser.setUserName(username);
            existingUser.setSurname(surname);
            existingUser.setBirthDate(birthDate);
            existingUser.setZodiac(zodiac);

            // Profil fotoğrafı güncellenirse
            if (profileImage != null && !profileImage.isEmpty()) {
                try {
                    existingUser.setProfileImage(profileImage.getBytes());
                } catch (IOException e) {
                    e.printStackTrace(); // Daha iyi hata yönetimi yapılabilir
                }
            }

            // Avatar güncellenirse
            if (avatar != null && !avatar.isEmpty()) {
                try {
                    existingUser.setAvatar(avatar.getBytes());
                } catch (IOException e) {
                    e.printStackTrace(); // Daha iyi hata yönetimi yapılabilir
                }
            }

            return userRepository.save(existingUser);
        }
        return null;
    }

    public User registerUser(User newUser) {
        Assert.notNull(newUser.getUserName(), "Username must not be null");
        Assert.notNull(newUser.getPassword(), "Password must not be null");

        if (userRepository.findByUserName(newUser.getUserName()) != null) {
            throw new IllegalArgumentException("Username already exists");
        }

        String hashedPassword = PasswordUtils.hashPassword(newUser.getPassword());
        newUser.setPassword(hashedPassword);

        return userRepository.save(newUser);
    }
}
