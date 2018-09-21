package com.dlvn.mcustomerportal.services.model.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class CartModel {

    @SerializedName("categoryItems")
    @Expose
    private List<CartItemModel> lsCategory;

    public CartModel() {
        lsCategory = new ArrayList<>();
    }

    public List<CartItemModel> getLsCategory() {
        return lsCategory;
    }

    public void setLsCategory(List<CartItemModel> lsCategory) {
        this.lsCategory = lsCategory;
    }
}
