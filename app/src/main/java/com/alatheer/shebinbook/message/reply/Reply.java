package com.alatheer.shebinbook.message.reply;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Reply {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("message_id_fk")
    @Expose
    private Integer messageIdFk;
    @SerializedName("user_id_fk")
    @Expose
    private Integer userIdFk;
    @SerializedName("trader_id_fk")
    @Expose
    private Integer traderIdFk;
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
    private String traderImg;
    @SerializedName("trader_name")
    @Expose
    private String traderName;
    @SerializedName("product_name")
    @Expose
    private String productName;
    @SerializedName("img")
    @Expose
    private String img;
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

    public Integer getTraderIdFk() {
        return traderIdFk;
    }

    public void setTraderIdFk(Integer traderIdFk) {
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

    public String getTraderImg() {
        return traderImg;
    }

    public void setTraderImg(String traderImg) {
        this.traderImg = traderImg;
    }

    public String getTraderName() {
        return traderName;
    }

    public void setTraderName(String traderName) {
        this.traderName = traderName;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
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
