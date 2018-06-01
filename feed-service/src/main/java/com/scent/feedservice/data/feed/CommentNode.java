package com.scent.feedservice.data.feed;

import org.springframework.data.annotation.Id;

import java.util.Date;

public class CommentNode {
    private String commentId;
    private String content;
    private String userId;
    private String createdDate;
    private String timeZone;
    private Boolean flagToDelete;
    public CommentNode(){

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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
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
