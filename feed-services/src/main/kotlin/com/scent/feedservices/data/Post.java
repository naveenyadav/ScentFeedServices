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
   }
   @Id
   private String _postId;
   private Date _postedDate;
   private String _content;
   private Long _upVote = 0;
   private Long _downVote = 0;
   private String postImagePath;
   private String _userId;
   private String _commentUrl;
   private List<User> _reportedBy;
   private Privacy _privacy;
   public Post(){

   }
   
}
