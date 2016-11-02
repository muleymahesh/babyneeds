package com.maks.model;


import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("org.jsonschema2pojo")
public class ServiceDTO {

    @SerializedName("result")
    @Expose
    private String result;
    @SerializedName("responseCode")
    @Expose
    private Integer responseCode;
    @SerializedName("data")
    @Expose
    private List<Service> data = new ArrayList<Service>();

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
    public List<Service> getData() {
        return data;
    }

    /**
     *
     * @param data
     * The data
     */
    public void setData(List<Service> data) {
        this.data = data;
    }

}
