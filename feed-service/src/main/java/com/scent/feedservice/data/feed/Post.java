package com.scent.feedservice.data.feed;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Document(collection = "post")
public class Post {
   @Id
   private String postId;
   private Date createdDate;
   private Date expiryDate;
   private String content;
   private long votes;
   private String imageUrl;
   private String userId;

   private PrivacyType privacy;
   private Boolean isLocationHidden;
   private PostType postType;
   private Boolean flagToDelete;
   private Double[] location;
   private String locationName;

   public Post() {
      location = new Double[2];
   }

   public String getPostId() {
      return postId;
   }

   public void setPostId(String postId) {
      this.postId = postId;
   }


   public String getContent() {
      return content;
   }

   public void setContent(String content) {
      this.content = content;
   }

   public long getVotes() {
      return votes;
   }

   public void setVotes(long votes) {
      this.votes = votes;
   }

   public String getImageUrl() {
      return imageUrl;
   }

   public void setImageUrl(String imageUrl) {
      this.imageUrl = imageUrl;
   }

   public String getUserId() {
      return userId;
   }

   public void setUserId(String userId) {
      this.userId = userId;
   }

   public PrivacyType getPrivacy() {
      return privacy;
   }

   public void setPrivacy(PrivacyType privacy) {
      this.privacy = privacy;
   }


   public Boolean getLocationHidden() {
      return isLocationHidden;
   }

   public void setLocationHidden(Boolean locationHidden) {
      isLocationHidden = locationHidden;
   }

   public PostType getPostType() {
      return postType;
   }

   public void setPostType(PostType postType) {
      this.postType = postType;
   }

   public Boolean getFlagToDelete() {
      return flagToDelete;
   }

   public void setFlagToDelete(Boolean flagToDelete) {
      this.flagToDelete = flagToDelete;
   }

   public Double[] getLocation() {
      return location;
   }

   public void setLocation(Double[] location) {
      this.location = location;
   }

   public String getLocationName() {
      return locationName;
   }

   public void setLocationName(String locationName) {
      this.locationName = locationName;
   }

   public Date getCreatedDate() {
      return createdDate;
   }

   public void setCreatedDate(Date createdDate) {
      this.createdDate = createdDate;
   }

   public Date getExpiryDate() {
      return expiryDate;
   }

   public void setExpiryDate(Date expiryDate) {
      this.expiryDate = expiryDate;
   }

   @Override
   public String toString() {
      return "Post{" +
              "postId='" + postId + '\'' +
              ", createdDate=" + createdDate +
              ", expiryDate='" + expiryDate + '\'' +
              ", content='" + content + '\'' +
              ", votes=" + votes +
              ", imageUrl='" + imageUrl + '\'' +
              ", userId='" + userId + '\'' +
              ", privacy=" + privacy +
              ", isLocationHidden=" + isLocationHidden +
              ", postType=" + postType +
              ", flagToDelete=" + flagToDelete +
              ", location=" + Arrays.toString(location) +
              ", locationName='" + locationName + '\'' +
              '}';
   }
}



