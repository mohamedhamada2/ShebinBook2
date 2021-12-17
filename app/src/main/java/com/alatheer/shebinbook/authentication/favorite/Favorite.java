package com.alatheer.shebinbook.authentication.favorite;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Favorite {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("tsnef")
    @Expose
    private Integer tsnef;
    @SerializedName("store_name")
    @Expose
    private String storeName;
    @SerializedName("store_mobile")
    @Expose
    private String storeMobile;
    @SerializedName("store_address")
    @Expose
    private String storeAddress;
    @SerializedName("Governorate")
    @Expose
    private Integer governorate;
    @SerializedName("City")
    @Expose
    private Integer city;
    @SerializedName("logo")
    @Expose
    private String logo;
    @SerializedName("long_map")
    @Expose
    private String longMap;
    @SerializedName("lat_map")
    @Expose
    private String latMap;
    @SerializedName("special")
    @Expose
    private String special;
    @SerializedName("trade_id")
    @Expose
    private Integer tradeId;
    @SerializedName("view")
    @Expose
    private Integer view;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
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

    public Integer getTsnef() {
        return tsnef;
    }

    public void setTsnef(Integer tsnef) {
        this.tsnef = tsnef;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getStoreMobile() {
        return storeMobile;
    }

    public void setStoreMobile(String storeMobile) {
        this.storeMobile = storeMobile;
    }

    public String getStoreAddress() {
        return storeAddress;
    }

    public void setStoreAddress(String storeAddress) {
        this.storeAddress = storeAddress;
    }

    public Integer getGovernorate() {
        return governorate;
    }

    public void setGovernorate(Integer governorate) {
        this.governorate = governorate;
    }

    public Integer getCity() {
        return city;
    }

    public void setCity(Integer city) {
        this.city = city;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getLongMap() {
        return longMap;
    }

    public void setLongMap(String longMap) {
        this.longMap = longMap;
    }

    public String getLatMap() {
        return latMap;
    }

    public void setLatMap(String latMap) {
        this.latMap = latMap;
    }

    public String getSpecial() {
        return special;
    }

    public void setSpecial(String special) {
        this.special = special;
    }

    public Integer getTradeId() {
        return tradeId;
    }

    public void setTradeId(Integer tradeId) {
        this.tradeId = tradeId;
    }

    public Integer getView() {
        return view;
    }

    public void setView(Integer view) {
        this.view = view;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
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
