package com.example.demo.response;

import java.util.List;
import com.example.demo.entity.Post;

public class PostResponse {
    private Long id;
    private Long userId;
    private String userName;
    private String title;
    private String text;
    private List<LikeResponse> postLikes;

    public PostResponse(Post entity, List<LikeResponse> likes) {
        this.id = entity.getId();
        if (entity.getUser() != null) {
            this.userId = entity.getUser().getId();
            this.userName = entity.getUser().getUserName();
        } else {
            this.userId = null;  // veya uygun bir varsayılan değer
            this.userName = "Unknown";  // veya uygun bir varsayılan değer
        }
        this.title = entity.getTitle();
        this.text = entity.getText();
        this.postLikes = likes;
    }

    // Getter ve Setter metodları
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public List<LikeResponse> getPostLikes() {
        return postLikes;
    }

    public void setPostLikes(List<LikeResponse> postLikes) {
        this.postLikes = postLikes;
    }
}
