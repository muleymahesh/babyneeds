package com.maks.model;

/**
 * Created by maks on 28/8/16.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Generated;

@Generated("org.jsonschema2pojo")
public class AgeGroupDTO {

    @SerializedName("result")
    @Expose
    private String result;
    @SerializedName("responseCode")
    @Expose
    private Integer responseCode;
    @SerializedName("data")
    @Expose
    private List<AgeGroup> data = new ArrayList<AgeGroup>();

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
    public List<AgeGroup> getData() {
        return data;
    }

    /**
     *
     * @param data
     * The data
     */
    public void setData(List<AgeGroup> data) {
        this.data = data;
    }

}