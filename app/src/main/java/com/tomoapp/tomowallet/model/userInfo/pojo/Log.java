package com.tomoapp.tomowallet.model.userInfo.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Log {

    @SerializedName("msg")
    @Expose
    public String msg;
    @SerializedName("type")
    @Expose
    public String type;
    @SerializedName("change")
    @Expose
    public String change;
    @SerializedName("time")
    @Expose
    public String time;
    @SerializedName("tmcSidechain")
    @Expose
    public Double tmcSidechain;
    @SerializedName("tmcMainchain")
    @Expose
    public Double tmcMainchain;
    @SerializedName("txSidechain")
    @Expose
    public String txSidechain;
    @SerializedName("txMainchain")
    @Expose
    public String txMainchain;
    @SerializedName("total")
    @Expose
    public Double total;
}
