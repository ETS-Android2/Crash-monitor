package com.lol.crashmonitor.model.constant;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ConstantsResponse {
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("appmode")
    @Expose
    private String appMode;
    @SerializedName("razorpaykey")
    @Expose
    private String razorPayKey;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getAppMode() {
        return appMode;
    }

    public void setAppMode(String appMode) {
        this.appMode = appMode;
    }

    public String getRazorPayKey() {
        return razorPayKey;
    }

    public void setRazorPayKey(String razorPayKey) {
        this.razorPayKey = razorPayKey;
    }
}
