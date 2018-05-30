package com.scent.feedservices.data;

import org.springframework.data.annotation.Id;

import java.util.Date;

public class Comment {
    @Id
    private String commentId;
    private String content;
    private String postId;
    private String userId;
    private Date createdDate;
    private String timeZone;
    private Boolean flagToDelete;
    public Comment(){

    }

    public String getCommentId() {
        return commentId;
    }

    public void setCommentId(String commentId) {
        this.commentId = commentId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public String getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
    }

    public Boolean getFlagToDelete() {
        return flagToDelete;
    }

    public void setFlagToDelete(Boolean flagToDelete) {
        this.flagToDelete = flagToDelete;
    }
}
