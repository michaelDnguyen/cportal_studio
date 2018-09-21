package com.dlvn.mcustomerportal.services.model.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MasterData_Order {

    @SerializedName("OrderID")
    @Expose
    private String orderID;
    @SerializedName("OrderStatusNameEN")
    @Expose
    private String orderStatusNameEN;
    @SerializedName("OrderStatusNameVN")
    @Expose
    private String orderStatusNameVN;

    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    public String getOrderStatusNameEN() {
        return orderStatusNameEN;
    }

    public void setOrderStatusNameEN(String orderStatusNameEN) {
        this.orderStatusNameEN = orderStatusNameEN;
    }

    public String getOrderStatusNameVN() {
        return orderStatusNameVN;
    }

    public void setOrderStatusNameVN(String orderStatusNameVN) {
        this.orderStatusNameVN = orderStatusNameVN;
    }
}
