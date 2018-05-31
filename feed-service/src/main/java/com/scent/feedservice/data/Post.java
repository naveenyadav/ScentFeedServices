package com.scent.feedservice.data;


import org.json.JSONObject;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

import static com.scent.feedservice.Util.Constants.*;


@Document(collection = "post")
public class Post {
   @Id
   private String postId;
   private String commentId;
   private String createdDate;
   private String timeZone;
   private String expiryDate;
   private String content;
   private long upVote = 0;
   private long downVote = 0;
   private String imageUrl;
   private String userId;
   private String commentUrl;
   private List<String> reportedBy;
   private PrivacyType privacy;
   private Location location;
   private Boolean isLocationHidden;
   private PostType postType;
   private Boolean flagToDelete;
   public Post(){ }
   public JSONObject toJSONObject(Post post){
      JSONObject jsonObject = new JSONObject();
      jsonObject.put(POST_ID, post.getPostId());
      jsonObject.put(TIMEZONE, post.getTimeZone());
      jsonObject.put(USER_ID, post.getUserId());
      jsonObject.put(LOCATION, post.getLocation().getLocationJson());
      return jsonObject;
   }
   public String getPostId() {
      return postId;
   }

   public void setPostId(String postId) {
      this.postId = postId;
   }

   public String getCommentId() {
      return commentId;
   }

   public void setCommentId(String commentId) {
      this.commentId = commentId;
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

   public long getUpVote() {
      return upVote;
   }

   public void setUpVote(long upVote) {
      this.upVote = upVote;
   }

   public long getDownVote() {
      return downVote;
   }

   public void setDownVote(long downVote) {
      this.downVote = downVote;
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

   public String getCommentUrl() {
      return commentUrl;
   }

   public void setCommentUrl(String commentUrl) {
      this.commentUrl = commentUrl;
   }

   public List<String> getReportedBy() {
      return reportedBy;
   }

   public void setReportedBy(List<String> reportedBy) {
      this.reportedBy = reportedBy;
   }

   public PrivacyType getPrivacy() {
      return privacy;
   }

   public void setPrivacy(PrivacyType privacy) {
      this.privacy = privacy;
   }

   public Location getLocation() {
      return location;
   }

   public void setLocation(Location location) {
      this.location = location;
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


}
