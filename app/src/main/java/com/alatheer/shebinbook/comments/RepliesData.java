package com.alatheer.shebinbook.comments;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RepliesData {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("post_id_fk")
    @Expose
    private Integer postIdFk;
    @SerializedName("post_user_id_fk")
    @Expose
    private Integer postUserIdFk;
    @SerializedName("comment_id_fk")
    @Expose
    private Integer commentIdFk;
    @SerializedName("comment_user_id_fk")
    @Expose
    private Integer commentUserIdFk;
    @SerializedName("user_id_fk")
    @Expose
    private Integer userIdFk;
    @SerializedName("replay_message")
    @Expose
    private String replayMessage;
    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("time")
    @Expose
    private String time;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("last_name")
    @Expose
    private String lastName;
    @SerializedName("user_img")
    @Expose
    private Object userImg;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getPostIdFk() {
        return postIdFk;
    }

    public void setPostIdFk(Integer postIdFk) {
        this.postIdFk = postIdFk;
    }

    public Integer getPostUserIdFk() {
        return postUserIdFk;
    }

    public void setPostUserIdFk(Integer postUserIdFk) {
        this.postUserIdFk = postUserIdFk;
    }

    public Integer getCommentIdFk() {
        return commentIdFk;
    }

    public void setCommentIdFk(Integer commentIdFk) {
        this.commentIdFk = commentIdFk;
    }

    public Integer getCommentUserIdFk() {
        return commentUserIdFk;
    }

    public void setCommentUserIdFk(Integer commentUserIdFk) {
        this.commentUserIdFk = commentUserIdFk;
    }

    public Integer getUserIdFk() {
        return userIdFk;
    }

    public void setUserIdFk(Integer userIdFk) {
        this.userIdFk = userIdFk;
    }

    public String getReplayMessage() {
        return replayMessage;
    }

    public void setReplayMessage(String replayMessage) {
        this.replayMessage = replayMessage;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
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
}
