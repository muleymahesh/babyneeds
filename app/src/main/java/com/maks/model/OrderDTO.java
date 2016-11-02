package com.maks.model;

/**
 * Created by maks on 16/6/16.
 */


import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("org.jsonschema2pojo")
public class OrderDTO {

    @SerializedName("result")
    @Expose
    private String result;
    @SerializedName("orders")
    @Expose
    private List<OrderPojo> orders = new ArrayList<OrderPojo>();

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
     * The orders
     */
    public List<OrderPojo> getOrders() {
        return orders;
    }

    /**
     *
     * @param orders
     * The orders
     */
    public void setOrders(List<OrderPojo> orders) {
        this.orders = orders;
    }

}
