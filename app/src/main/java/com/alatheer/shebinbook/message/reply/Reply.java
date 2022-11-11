package com.alatheer.shebinbook.message.reply;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Reply {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("type")
    @Expose
    private Integer type;
    @SerializedName("message_id_fk")
    @Expose
    private Integer messageIdFk;
    @SerializedName("user_id_fk")
    @Expose
    private Integer userIdFk;
    @SerializedName("trader_id_fk")
    @Expose
    private Object traderIdFk;
    @SerializedName("store_id_fk")
    @Expose
    private Integer storeIdFk;
    @SerializedName("product_id_fk")
    @Expose
    private Integer productIdFk;
    @SerializedName("replay")
    @Expose
    private String replay;
    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("last_name")
    @Expose
    private String lastName;
    @SerializedName("user_img")
    @Expose
    private String userImg;
    @SerializedName("trader_img")
    @Expose
    private Object traderImg;
    @SerializedName("trader_name")
    @Expose
    private Object traderName;
    @SerializedName("product_name")
    @Expose
    private Object productName;
    @SerializedName("img")
    @Expose
    private Object img;
    @SerializedName("logo")
    @Expose
    private String logo;
    @SerializedName("store_name")
    @Expose
    private String storeName;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getMessageIdFk() {
        return messageIdFk;
    }

    public void setMessageIdFk(Integer messageIdFk) {
        this.messageIdFk = messageIdFk;
    }

    public Integer getUserIdFk() {
        return userIdFk;
    }

    public void setUserIdFk(Integer userIdFk) {
        this.userIdFk = userIdFk;
    }

    public Object getTraderIdFk() {
        return traderIdFk;
    }

    public void setTraderIdFk(Object traderIdFk) {
        this.traderIdFk = traderIdFk;
    }

    public Integer getStoreIdFk() {
        return storeIdFk;
    }

    public void setStoreIdFk(Integer storeIdFk) {
        this.storeIdFk = storeIdFk;
    }

    public Integer getProductIdFk() {
        return productIdFk;
    }

    public void setProductIdFk(Integer productIdFk) {
        this.productIdFk = productIdFk;
    }

    public String getReplay() {
        return replay;
    }

    public void setReplay(String replay) {
        this.replay = replay;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
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

    public String getUserImg() {
        return userImg;
    }

    public void setUserImg(String userImg) {
        this.userImg = userImg;
    }

    public Object getTraderImg() {
        return traderImg;
    }

    public void setTraderImg(Object traderImg) {
        this.traderImg = traderImg;
    }

    public Object getTraderName() {
        return traderName;
    }

    public void setTraderName(Object traderName) {
        this.traderName = traderName;
    }

    public Object getProductName() {
        return productName;
    }

    public void setProductName(Object productName) {
        this.productName = productName;
    }

    public Object getImg() {
        return img;
    }

    public void setImg(Object img) {
        this.img = img;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }
}
