package com.alatheer.shebinbook.posts;

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
    private Object lastName;
    @SerializedName("user_img")
    @Expose
    private Object userImg;

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

    public Object getLastName() {
        return lastName;
    }

    public void setLastName(Object lastName) {
        this.lastName = lastName;
    }

    public Object getUserImg() {
        return userImg;
    }

    public void setUserImg(Object userImg) {
        this.userImg = userImg;
    }

}
