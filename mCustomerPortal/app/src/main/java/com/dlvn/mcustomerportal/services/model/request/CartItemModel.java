package com.dlvn.mcustomerportal.services.model.request;

import android.os.Parcel;
import android.os.Parcelable;

import com.dlvn.mcustomerportal.common.Constant;
import com.dlvn.mcustomerportal.services.model.response.ProductLoyaltyModel;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class CartItemModel implements Parcelable {

    @SerializedName("category")
    @Expose
    private String category;

    @SerializedName("categoryName")
    @Expose
    private String categoryName;

    private String isChecked = Constant.CHECK_BOX_CHECKED_FALSE;

    /**
     * Support for multi checked Cart
     */
    @SerializedName("productItems")
    @Expose
    private List<ProductLoyaltyModel> lsItems;

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getIsChecked() {
        return isChecked;
    }

    public void setIsChecked(String isChecked) {
        this.isChecked = isChecked;
    }

    public List<ProductLoyaltyModel> getLsItems() {
        return lsItems;
    }

    public void setLsItems(List<ProductLoyaltyModel> lsItems) {
        this.lsItems = lsItems;
    }

    public CartItemModel() {
        this.lsItems = new ArrayList<>();
    }

    public CartItemModel(Parcel in) {
        category = in.readString();
        categoryName = in.readString();
        isChecked = in.readString();

        lsItems = new ArrayList<>();
        in.readList(lsItems, ProductLoyaltyModel.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(category);
        dest.writeString(categoryName);
        dest.writeString(isChecked);
        dest.writeList(lsItems);
    }

    public static final Creator<CartItemModel> CREATOR = new Creator<CartItemModel>() {
        @Override
        public CartItemModel createFromParcel(Parcel source) {
            return new CartItemModel(source);
        }

        @Override
        public CartItemModel[] newArray(int size) {
            return new CartItemModel[size];
        }
    };
}
