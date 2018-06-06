package com.scent.feedservice.data.feed;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Document(collection = "Likes")
public class Like {
    @Id
    private String userId;
    private Set<String> posts;
    public Like(){
        posts = new HashSet<>();
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Set<String> getPosts() {
        return posts;
    }

    public void setPosts(Set<String> posts) {
        this.posts = posts;
    }
    public void addPosts(String postId){
        this.posts.add(userId);
    }
}
