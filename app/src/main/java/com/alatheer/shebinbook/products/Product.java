package com.alatheer.shebinbook.products;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Product implements Serializable {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("price")
    @Expose
    private Integer price;
    @SerializedName("img")
    @Expose
    private String img;
    @SerializedName("details")
    @Expose
    private String details;

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    @SerializedName("alboum_id_fk")
    @Expose
    private Integer alboumIdFk;
    @SerializedName("store_id_fk")
    @Expose
    private Integer storeIdFk;
    @SerializedName("trader_id_fk")
    @Expose
    private Integer traderIdFk;
    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("time")
    @Expose
    private String time;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public Integer getAlboumIdFk() {
        return alboumIdFk;
    }

    public void setAlboumIdFk(Integer alboumIdFk) {
        this.alboumIdFk = alboumIdFk;
    }

    public Integer getStoreIdFk() {
        return storeIdFk;
    }

    public void setStoreIdFk(Integer storeIdFk) {
        this.storeIdFk = storeIdFk;
    }

    public Integer getTraderIdFk() {
        return traderIdFk;
    }

    public void setTraderIdFk(Integer traderIdFk) {
        this.traderIdFk = traderIdFk;
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

}
