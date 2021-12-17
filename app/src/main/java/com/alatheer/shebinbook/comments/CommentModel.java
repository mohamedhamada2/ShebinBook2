package com.alatheer.shebinbook.comments;

import com.alatheer.shebinbook.posts.Data;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CommentModel {
    @SerializedName("data")
    @Expose
    private Data data;
    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("error")
    @Expose
    private Object error;

    public com.alatheer.shebinbook.posts.Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public Object getError() {
        return error;
    }

    public void setError(Object error) {
        this.error = error;
    }
}
