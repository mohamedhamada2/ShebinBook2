package com.alatheer.shebinbook.trader.updateproduct;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SubImage {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("product_id_fk")
    @Expose
    private Integer productIdFk;
    @SerializedName("alboum_id_fk")
    @Expose
    private Integer alboumIdFk;
    @SerializedName("image")
    @Expose
    private String image;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getProductIdFk() {
        return productIdFk;
    }

    public void setProductIdFk(Integer productIdFk) {
        this.productIdFk = productIdFk;
    }

    public Integer getAlboumIdFk() {
        return alboumIdFk;
    }

    public void setAlboumIdFk(Integer alboumIdFk) {
        this.alboumIdFk = alboumIdFk;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }


}
