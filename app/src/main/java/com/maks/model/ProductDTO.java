package com.maks.model;

import com.google.gson.annotations.SerializedName;
import com.maks.babyneeds.Utility.Constants;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by maks on 7/2/16.
 */
public class ProductDTO implements Serializable
{

    @SerializedName("result")
    String result;
    @SerializedName("responseCode")
    String responseCode;
    @SerializedName("data")
    List<Product> data =new ArrayList<>();

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

    public List<Product> getData() {
        Collections.shuffle(data);
        return data;
    }

    public void setData(List<Product> data) {
        this.data = data;
    }
}
