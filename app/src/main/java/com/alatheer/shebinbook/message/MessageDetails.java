package com.alatheer.shebinbook.message;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MessageDetails {
    @SerializedName("data")
    @Expose
    private Message data;
    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("error")
    @Expose
    private Object error;

    public Message getData() {
        return data;
    }

    public void setData(Message data) {
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
