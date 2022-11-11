package com.alatheer.shebinbook.subcategory;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Data1 {
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("sub")
    @Expose
    private List<Datum> sub = null;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Datum> getSub() {
        return sub;
    }

    public void setSub(List<Datum> sub) {
        this.sub = sub;
    }
}
