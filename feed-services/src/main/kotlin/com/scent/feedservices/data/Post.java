package com.scent.feedservices.data;


import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;


@Document
public class Post {
   public enum Privacy {
      PRIVATE,
      PUBLIC,
      FRIEDNS,
      CUSTOM
   };
   public enum PostType {
      VIDEO,
      IMAGE,
      POST,
      URL
   };
   @Id
   private String postId;
   private Date postedDate;
   private String content;
   private Long upVote = 0;
   private Long downVote = 0;
   private String imageUrl;
   private String userId;
   private String commentUrl;
   private List<User> reportedBy;
   private Privacy privacy;
   private Location location;
   private PostType postType;
   public Post(){

   }
   
}
