package com.maks.model;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ServicesCategoryDTO {

    @SerializedName("result")
    @Expose
    private String result;
    @SerializedName("responseCode")
    @Expose
    private Integer responseCode;
    @SerializedName("data")
    @Expose
    private List<ServicesCategory> data = new ArrayList<ServicesCategory>();

    /**
     *
     * @return
     * The result
     */
    public String getResult() {
        return result;
    }

    /**
     *
     * @param result
     * The result
     */
    public void setResult(String result) {
        this.result = result;
    }

    /**
     *
     * @return
     * The responseCode
     */
    public Integer getResponseCode() {
        return responseCode;
    }

    /**
     *
     * @param responseCode
     * The responseCode
     */
    public void setResponseCode(Integer responseCode) {
        this.responseCode = responseCode;
    }

    /**
     *
     * @return
     * The data
     */
    public List<ServicesCategory> getData() {
        return data;
    }

    /**
     *
     * @param data
     * The data
     */
    public void setData(List<ServicesCategory> data) {
        this.data = data;
    }

}