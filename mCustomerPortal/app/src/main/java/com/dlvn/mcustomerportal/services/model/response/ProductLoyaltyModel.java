package com.dlvn.mcustomerportal.services.model.response;

import android.os.Parcel;
import android.os.Parcelable;

import com.dlvn.mcustomerportal.common.Constant;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProductLoyaltyModel implements Parcelable {

    @SerializedName("ProductID")
    @Expose
    private String productID;
    @SerializedName("Quantity")
    @Expose
    private Integer quantity;
    @SerializedName("Price")
    @Expose
    private Integer price;
    @SerializedName("Shipping")
    @Expose
    private Integer shipping;
    @SerializedName("Discount")
    @Expose
    private Integer discount;
    @SerializedName("strDetail")
    @Expose
    private String strDetail;
    @SerializedName("shortDescription")
    @Expose
    private String shortDescription;
    @SerializedName("fullDescription")
    @Expose
    private String fullDescription;

    /**
     * Support multi checked
     */
    private String isChecked = Constant.CHECK_BOX_CHECKED_FALSE;

    public String getProductID() {
        return productID;
    }

    public void setProductID(String productID) {
        this.productID = productID;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Integer getShipping() {
        return shipping;
    }

    public void setShipping(Integer shipping) {
        this.shipping = shipping;
    }

    public Integer getDiscount() {
        return discount;
    }

    public void setDiscount(Integer discount) {
        this.discount = discount;
    }

    public String getStrDetail() {
        return strDetail;
    }

    public void setStrDetail(String strDetail) {
        this.strDetail = strDetail;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getFullDescription() {
        return fullDescription;
    }

    public void setFullDescription(String fullDescription) {
        this.fullDescription = fullDescription;
    }

    public String getIsChecked() {
        return isChecked;
    }

    public void setIsChecked(String isChecked) {
        this.isChecked = isChecked;
    }

    public ProductLoyaltyModel() {
        super();
    }

    public ProductLoyaltyModel(Parcel in) {
        this.productID = in.readString();
        this.strDetail = in.readString();
        this.price = in.readInt();
        this.quantity = in.readInt();
        this.shipping = in.readInt();
        this.discount = in.readInt();
        this.shortDescription = in.readString();
        this.fullDescription = in.readString();
        this.isChecked = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(productID);
        dest.writeString(strDetail);
        dest.writeInt(price);
        dest.writeInt(quantity);
        dest.writeInt(shipping);
        dest.writeInt(discount);
        dest.writeString(shortDescription);
        dest.writeString(fullDescription);
        dest.writeString(isChecked);
    }

    public static final Creator<ProductLoyaltyModel> CREATOR = new Creator<ProductLoyaltyModel>() {
        @Override
        public ProductLoyaltyModel createFromParcel(Parcel source) {
            return new ProductLoyaltyModel(source);
        }

        @Override
        public ProductLoyaltyModel[] newArray(int size) {
            return new ProductLoyaltyModel[size];
        }
    };
}
