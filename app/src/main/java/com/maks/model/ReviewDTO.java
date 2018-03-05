package com.maks.model;

/**
 * Created by maks on 5/3/18.
 */
import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.SerializedName;
import com.maks.babyneeds.Utility.Constants;

public class ReviewDTO {

    @SerializedName("result")
    @Expose
    private String result;
    @SerializedName("responseCode")
    @Expose
    private Integer responseCode;
    @SerializedName("data")
    @Expose
    private List<Review> data = new ArrayList<Review>();

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

    public List<Review> getData() {
        return data;
    }

    public void setData(List<Review> data) {
        this.data = data;
    }

}
