package com.alatheer.shebinbook.comments;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Comment {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("comment_user_id_fk")
    @Expose
    private Integer commentUserIdFk;
    @SerializedName("post_user_id_fk")
    @Expose
    private Integer postUserIdFk;
    @SerializedName("post_id_fk")
    @Expose
    private Integer postIdFk;
    @SerializedName("comment")
    @Expose
    private String comment;
    @SerializedName("img")
    @Expose
    private String img;
    @SerializedName("time")
    @Expose
    private String time;
    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("last_name")
    @Expose
    private String lastName;
    @SerializedName("user_img")
    @Expose
    private Object userImg;
    @SerializedName("replies")
    @Expose
    private Replies replies;
    @SerializedName("replies_count")
    @Expose
    private Integer repliesCount;

    public Integer getRepliesCount() {
        return repliesCount;
    }

    public void setRepliesCount(Integer repliesCount) {
        this.repliesCount = repliesCount;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCommentUserIdFk() {
        return commentUserIdFk;
    }

    public void setCommentUserIdFk(Integer commentUserIdFk) {
        this.commentUserIdFk = commentUserIdFk;
    }

    public Integer getPostUserIdFk() {
        return postUserIdFk;
    }

    public void setPostUserIdFk(Integer postUserIdFk) {
        this.postUserIdFk = postUserIdFk;
    }

    public Integer getPostIdFk() {
        return postIdFk;
    }

    public void setPostIdFk(Integer postIdFk) {
        this.postIdFk = postIdFk;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Object getUserImg() {
        return userImg;
    }

    public void setUserImg(Object userImg) {
        this.userImg = userImg;
    }

    public Replies getReplies() {
        return replies;
    }

    public void setReplies(Replies replies) {
        this.replies = replies;
    }
}
