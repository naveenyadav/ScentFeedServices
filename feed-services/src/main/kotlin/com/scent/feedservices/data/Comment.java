package com.scent.feedservices.data;

import org.springframework.data.annotation.Id;

import java.util.Date;

public class Comment {
    @Id
    private String commentId;
    private String commentContent;
    private String postId;
    private String userId;
    private Date commentedDate;
}
