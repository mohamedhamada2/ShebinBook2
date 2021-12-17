package com.alatheer.shebinbook.products;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Gallery {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("trader_id_fk")
    @Expose
    private Integer traderIdFk;
    @SerializedName("store_id_fk")
    @Expose
    private Integer storeIdFk;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("time")
    @Expose
    private String time;
    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("products")
    @Expose
    private List<Product> products = null;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }
}
