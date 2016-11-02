package com.maks.model;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("org.jsonschema2pojo")
public class Brand {

    @SerializedName("brand_id")
    @Expose
    private String brandId;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("brand_img")
    @Expose
    private String brandImg;

    /**
     *
     * @return
     * The brandId
     */
    public String getBrandId() {
        return brandId;
    }

    /**
     *
     * @param brandId
     * The brand_id
     */
    public void setBrandId(String brandId) {
        this.brandId = brandId;
    }

    /**
     *
     * @return
     * The name
     */
    public String getName() {
        return name;
    }

    /**
     *
     * @param name
     * The name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     *
     * @return
     * The brandImg
     */
    public String getBrandImg() {
        return brandImg;
    }

    /**
     *
     * @param brandImg
     * The brand_img
     */
    public void setBrandImg(String brandImg) {
        this.brandImg = brandImg;
    }

}