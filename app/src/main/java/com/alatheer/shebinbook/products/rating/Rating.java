package com.alatheer.shebinbook.products.rating;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Rating {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("user_id_fk")
    @Expose
    private Integer userIdFk;
    @SerializedName("store_id_fk")
    @Expose
    private Integer storeIdFk;
    @SerializedName("rate")
    @Expose
    private String rate;
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
    @SerializedName("trader_id_fk")
    @Expose
    private Integer trader_id_fk;

    public String getDescription() {
        return description;
    }

    @SerializedName("description")
    @Expose
    private String description;

    public Integer getTrader_id_fk() {
        return trader_id_fk;
    }

    public void setTrader_id_fk(Integer trader_id_fk) {
        this.trader_id_fk = trader_id_fk;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserIdFk() {
        return userIdFk;
    }

    public void setUserIdFk(Integer userIdFk) {
        this.userIdFk = userIdFk;
    }

    public Integer getStoreIdFk() {
        return storeIdFk;
    }

    public void setStoreIdFk(Integer storeIdFk) {
        this.storeIdFk = storeIdFk;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
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
