package com.maks.model;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OffersDTO {

    @SerializedName("result")
    @Expose
    private String result;
    @SerializedName("responseCode")
    @Expose
    private Integer responseCode;
    @SerializedName("data")
    @Expose
    private List<Offer> data = new ArrayList<Offer>();

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public Integer getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(Integer responseCode) {
        this.responseCode = responseCode;
    }

    public List<Offer> getData() {
        return data;
    }

    public void setData(List<Offer> data) {
        this.data = data;
    }

}