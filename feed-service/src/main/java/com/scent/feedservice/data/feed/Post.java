package com.scent.feedservice.data.feed;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Document(collection = "post")
public class Post{
   @Id
   private String postId;
   private String createdDate;
   private String expiryDate;
   private String content;
   private long votes;
   private String imageUrl;
   private String userId;
   private PrivacyType privacy;

   private Boolean isLocationHidden;
   private PostType postType;
   private Boolean flagToDelete;
   private Double[] location;
   public Post(){

   }

   public String getPostId() {
      return postId;
   }

   public void setPostId(String postId) {
      this.postId = postId;
   }

   public String getCreatedDate() {
      return createdDate;
   }

   public void setCreatedDate(String createdDate) {
      this.createdDate = createdDate;
   }

   public String getExpiryDate() {
      return expiryDate;
   }

   public void setExpiryDate(String expiryDate) {
      this.expiryDate = expiryDate;
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

    @Override
    public String toString() {
        return "Post{" +
                "postId='" + postId + '\'' +
                ", createdDate='" + createdDate + '\'' +
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
                '}';
    }
}
