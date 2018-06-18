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

    private Set<String> upVotes;
    private Set<String> downVotes;
    public Like(){
        upVotes = new HashSet<>();
        downVotes = new HashSet<>();
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }


    public Set<String> getUpVotes() {
        return upVotes;
    }

    public void setUpVotes(Set<String> upVotes) {
        this.upVotes = upVotes;
    }

    public Set<String> getDownVotes() {
        return downVotes;
    }

    public void setDownVotes(Set<String> downVotes) {
        this.downVotes = downVotes;
    }

    public boolean addUpVotes(String postId){
        return upVotes.add(postId);
    }

    public boolean removeUpVotes(String postId){
        return upVotes.remove(postId);
    }

    public boolean addDownVotes(String postId){
        return downVotes.add(postId);
    }

    public boolean removeDownVotes(String postId){
        return downVotes.remove(postId);
    }

    @Override
    public String toString() {
        return "Like{" +
                "userId='" + userId + '\'' +
                ", upVotes=" + upVotes +
                ", downVotes=" + downVotes +
                '}';
    }
}
