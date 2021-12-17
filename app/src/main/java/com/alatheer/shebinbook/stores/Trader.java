
package com.alatheer.shebinbook.stores;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Trader implements Serializable {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("trader_name")
    @Expose
    private String traderName;
    @SerializedName("national_num")
    @Expose
    private String nationalNum;
    @SerializedName("adress")
    @Expose
    private String adress;
    @SerializedName("phone")
    @Expose
    private String phone;
    @SerializedName("trader_img")
    @Expose
    private Object traderImg;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTraderName() {
        return traderName;
    }

    public void setTraderName(String traderName) {
        this.traderName = traderName;
    }

    public String getNationalNum() {
        return nationalNum;
    }

    public void setNationalNum(String nationalNum) {
        this.nationalNum = nationalNum;
    }

    public String getAdress() {
        return adress;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Object getTraderImg() {
        return traderImg;
    }

    public void setTraderImg(Object traderImg) {
        this.traderImg = traderImg;
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

}
