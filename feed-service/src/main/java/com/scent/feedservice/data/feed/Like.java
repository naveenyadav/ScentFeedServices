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
    private Set<String> upVotePosts;
    private Set<String> downVotePosts;
    public Like(){
        upVotePosts = new HashSet<>();
        downVotePosts = new HashSet<>();

    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Set<String> getUpVotePosts() {
        return upVotePosts;
    }

    public void setUpVotePosts(Set<String> upVotePosts) {
        this.upVotePosts = upVotePosts;
    }

    public Set<String> getDownVotePosts() {
        return downVotePosts;
    }

    public void setDownVotePosts(Set<String> downVotePosts) {
        this.downVotePosts = downVotePosts;
    }

    public boolean addPosts(String postId, boolean upVoted){
        if(upVoted) {
            return this.upVotePosts.add(postId);
        }else{
            return this.downVotePosts.add(postId);
        }
    }

    @Override
    public String toString() {
        return "Like{" +
                "userId='" + userId + '\'' +
                ", upVotePosts=" + upVotePosts +
                ", downVotePosts=" + downVotePosts +
                '}';
    }
}
