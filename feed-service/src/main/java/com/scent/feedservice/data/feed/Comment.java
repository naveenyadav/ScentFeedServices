package com.scent.feedservice.data.feed;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.LinkedList;
import java.util.List;

@Document
public class Comment {
    @Id
    private String commentId;
    private String ownerUserId;
    private List<CommentNode> commentNode;
    private  Comment(){
        commentNode =  new LinkedList<>();
    }

    public String getCommentId() {
        return commentId;
    }

    public void setCommentId(String commentId) {
        this.commentId = commentId;
    }

    public String getOwnerUserId() {
        return ownerUserId;
    }

    public void setOwnerUserId(String ownerUserId) {
        this.ownerUserId = ownerUserId;
    }

    public CommentNode getCommentNode() {
        return commentNode;
    }

    public void setCommentNode(CommentNode commentNode) {
        this.commentNode = commentNode;
    }
}
