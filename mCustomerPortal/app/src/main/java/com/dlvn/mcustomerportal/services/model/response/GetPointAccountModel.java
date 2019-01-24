package com.dlvn.mcustomerportal.services.model.response;

import android.os.Parcel;
import android.os.Parcelable;

import com.dlvn.mcustomerportal.adapter.PointAccountAdapter;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetPointAccountModel implements Parcelable {

    @SerializedName("PointID")
    @Expose
    private double pointID;
    @SerializedName("OrderID")
    @Expose
    private double orderID;
    @SerializedName("Point")
    @Expose
    private double point;
    @SerializedName("PointBalance")
    @Expose
    private double pointBalance;
    @SerializedName("CustomerID")
    @Expose
    private String customerID;
    @SerializedName("TransTypeCD")
    @Expose
    private String transTypeCD;
    @SerializedName("CreateDate")
    @Expose
    private String createDate;
    @SerializedName("Description")
    @Expose
    private String description;
    @SerializedName("PolicyNo")
    @Expose
    private String policyNo;

    public GetPointAccountModel(String title, int code) {
        this.titleGroup = title;
        this.codeGroup = code;
    }

    private String titleGroup;
    private int codeGroup = PointAccountAdapter.CODE_ITEM;

    public double getPointID() {
        return pointID;
    }

    public void setPointID(double pointID) {
        this.pointID = pointID;
    }

    public double getOrderID() {
        return orderID;
    }

    public void setOrderID(double orderID) {
        this.orderID = orderID;
    }

    public double getPoint() {
        return point;
    }

    public void setPoint(double point) {
        this.point = point;
    }

    public double getPointBalance() {
        return pointBalance;
    }

    public void setPointBalance(double pointBalance) {
        this.pointBalance = pointBalance;
    }

    public String getCustomerID() {
        return customerID;
    }

    public void setCustomerID(String customerID) {
        this.customerID = customerID;
    }

    public String getTransTypeCD() {
        return transTypeCD;
    }

    public void setTransTypeCD(String transTypeCD) {
        this.transTypeCD = transTypeCD;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPolicyNo() {
        return policyNo;
    }

    public void setPolicyNo(String policyNo) {
        this.policyNo = policyNo;
    }

    public String getTitleGroup() {
        return titleGroup;
    }

    public void setTitleGroup(String titleGroup) {
        this.titleGroup = titleGroup;
    }

    public int getCodeGroup() {
        return codeGroup;
    }

    public void setCodeGroup(int codeGroup) {
        this.codeGroup = codeGroup;
    }

    public GetPointAccountModel() {

    }

    public GetPointAccountModel(Parcel in) {
        pointID = in.readDouble();
        orderID = in.readDouble();
        customerID = in.readString();
        transTypeCD = in.readString();
        policyNo = in.readString();
        description = in.readString();
        point = in.readDouble();
        pointBalance = in.readDouble();
        createDate = in.readString();
        titleGroup = in.readString();
        codeGroup = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(pointID);
        dest.writeDouble(orderID);
        dest.writeString(customerID);
        dest.writeString(transTypeCD);
        dest.writeString(policyNo);
        dest.writeString(description);
        dest.writeDouble(point);
        dest.writeDouble(pointBalance);
        dest.writeString(createDate);
        dest.writeString(titleGroup);
        dest.writeInt(codeGroup);
    }

    public static final Creator<GetPointAccountModel> CREATOR = new Creator<GetPointAccountModel>() {
        @Override
        public GetPointAccountModel createFromParcel(Parcel source) {
            return new GetPointAccountModel(source);
        }

        @Override
        public GetPointAccountModel[] newArray(int size) {
            return new GetPointAccountModel[size];
        }
    };
}
