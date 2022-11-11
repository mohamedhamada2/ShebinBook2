package com.alatheer.shebinbook.trader.updateproduct;

import com.alatheer.shebinbook.allproducts.SubImage;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Data {
    @SerializedName("success")
    @Expose
    private Integer success;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("sub_images")
    @Expose
    private List<SubImage> subImages = null;

    public Integer getSuccess() {
        return success;
    }

    public void setSuccess(Integer success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<SubImage> getSubImages() {
        return subImages;
    }

    public void setSubImages(List<SubImage> subImages) {
        this.subImages = subImages;
    }

}
