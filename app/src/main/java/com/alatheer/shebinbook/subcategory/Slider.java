package com.alatheer.shebinbook.subcategory;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Slider {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("gender")
    @Expose
    private Integer gender;
    @SerializedName("store_id_fk")
    @Expose
    private Object storeIdFk;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("trader_id_fk")
    @Expose
    private Integer traderIdFk;
    @SerializedName("from_date")
    @Expose
    private String fromDate;
    @SerializedName("to_date")
    @Expose
    private String toDate;
    @SerializedName("img")
    @Expose
    private String img;
    @SerializedName("price_before_offer")
    @Expose
    private String priceBeforeOffer;
    @SerializedName("price_after_offer")
    @Expose
    private String priceAfterOffer;
    @SerializedName("publisher")
    @Expose
    private Integer publisher;
    @SerializedName("screen")
    @Expose
    private Integer screen;
    @SerializedName("offer_id_fk")
    @Expose
    private Integer offerIdFk;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    public Object getStoreIdFk() {
        return storeIdFk;
    }

    public void setStoreIdFk(Object storeIdFk) {
        this.storeIdFk = storeIdFk;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getTraderIdFk() {
        return traderIdFk;
    }

    public void setTraderIdFk(Integer traderIdFk) {
        this.traderIdFk = traderIdFk;
    }

    public String getFromDate() {
        return fromDate;
    }

    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }

    public String getToDate() {
        return toDate;
    }

    public void setToDate(String toDate) {
        this.toDate = toDate;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getPriceBeforeOffer() {
        return priceBeforeOffer;
    }

    public void setPriceBeforeOffer(String priceBeforeOffer) {
        this.priceBeforeOffer = priceBeforeOffer;
    }

    public String getPriceAfterOffer() {
        return priceAfterOffer;
    }

    public void setPriceAfterOffer(String priceAfterOffer) {
        this.priceAfterOffer = priceAfterOffer;
    }

    public Integer getPublisher() {
        return publisher;
    }

    public void setPublisher(Integer publisher) {
        this.publisher = publisher;
    }

    public Integer getScreen() {
        return screen;
    }

    public void setScreen(Integer screen) {
        this.screen = screen;
    }

    public Integer getOfferIdFk() {
        return offerIdFk;
    }

    public void setOfferIdFk(Integer offerIdFk) {
        this.offerIdFk = offerIdFk;
    }
}
