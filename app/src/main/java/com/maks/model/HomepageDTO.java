package com.maks.model;

/**
 * Created by maks on 11/6/16.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Collections;
import java.util.List;

import javax.annotation.Generated;

public class HomepageDTO {

    @SerializedName("result")
    String result;
    @SerializedName("responseCode")
    String responseCode;
    @SerializedName("data")
    List<BannerPojo> data;
    @SerializedName("offer_data")
    List<Offer> offer_data;
    @SerializedName("new_data")
//    List<Product> new_data;
    List<Category> new_data;
    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    public List<Offer> getOffer_data() {
        Collections.shuffle(offer_data);
        return offer_data;}

    public void setOffer_data(List<Offer> offer_data) {
        this.offer_data = offer_data;
    }

    public List<BannerPojo> getData() {
        return data;
    }

    public void setData(List<BannerPojo> data) {
        this.data = data;
    }

    public List<Category> getNew_data() {
        return new_data;
    }

    public void setNew_data(List<Category> new_data) {
        this.new_data = new_data;
    }

}