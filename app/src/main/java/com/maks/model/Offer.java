package com.maks.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by maks on 11/2/18.
 */

public class Offer {
    @SerializedName("offer_id")
    @Expose
    private String offerId;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("per_discount")
    @Expose
    private String perDiscount;
    @SerializedName("offer_img")
    @Expose
    private String offerImg;

    public String getOfferId() {
        return offerId;
    }

    public void setOfferId(String offerId) {
        this.offerId = offerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPerDiscount() {
        return perDiscount;
    }

    public void setPerDiscount(String perDiscount) {
        this.perDiscount = perDiscount;
    }

    public String getOfferImg() {
        return offerImg;
    }

    public void setOfferImg(String offerImg) {
        this.offerImg = offerImg;
    }
}
