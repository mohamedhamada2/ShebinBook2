
package com.alatheer.shebinbook.stores;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
public class Store implements Serializable {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("tsnef")
    @Expose
    private Integer tsnef;
    @SerializedName("sub_tsnef")
    @Expose
    private Integer subTsnef;
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
    @SerializedName("offers_words")
    @Expose
    private String offersWords;
    @SerializedName("offers_products")
    @Expose
    private String offersProducts;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("mini_description")
    @Expose
    private String mini_description;

    public String getMini_description() {
        return mini_description;
    }

    public void setMini_description(String mini_description) {
        this.mini_description = mini_description;
    }

    @SerializedName("appointments_work")
    @Expose
    private String appointmentsWork;
    @SerializedName("facebook")
    @Expose
    private String facebook;
    @SerializedName("store_whats")
    @Expose
    private String storeWhats;
    @SerializedName("view")
    @Expose
    private Integer view;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("favourite")
    @Expose
    private Integer favourite;
    @SerializedName("trader")
    @Expose
    private Trader trader;
    @SerializedName("instagram")
    @Expose
    private String instagram;

    public String getInstagram() {
        return instagram;
    }

    public void setInstagram(String instagram) {
        this.instagram = instagram;
    }

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

    public Integer getSubTsnef() {
        return subTsnef;
    }

    public void setSubTsnef(Integer subTsnef) {
        this.subTsnef = subTsnef;
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

    public String getOffersWords() {
        return offersWords;
    }

    public void setOffersWords(String offersWords) {
        this.offersWords = offersWords;
    }

    public String getOffersProducts() {
        return offersProducts;
    }

    public void setOffersProducts(String offersProducts) {
        this.offersProducts = offersProducts;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAppointmentsWork() {
        return appointmentsWork;
    }

    public void setAppointmentsWork(String appointmentsWork) {
        this.appointmentsWork = appointmentsWork;
    }

    public String getFacebook() {
        return facebook;
    }

    public void setFacebook(String facebook) {
        this.facebook = facebook;
    }

    public String getStoreWhats() {
        return storeWhats;
    }

    public void setStoreWhats(String storeWhats) {
        this.storeWhats = storeWhats;
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

    public Integer getFavourite() {
        return favourite;
    }

    public void setFavourite(Integer favourite) {
        this.favourite = favourite;
    }

    public Trader getTrader() {
        return trader;
    }

    public void setTrader(Trader trader) {
        this.trader = trader;
    }


}
