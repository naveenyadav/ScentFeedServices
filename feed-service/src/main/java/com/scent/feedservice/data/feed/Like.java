package com.scent.feedservice.data.feed;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.List;


@Document(collection = "Likes")
public class Like {
    @Id
    private String userId;
    private List<String> posts;
    public Like(){

    }
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public List<String> getPosts() {
        return posts;
    }

    public void setPosts(List<String> posts) {
        this.posts = posts;
    }

    @Override
    public String toString() {
        return "Like{" +
                "userId='" + userId + '\'' +
                ", posts=" + posts +
                '}';
    }
}
